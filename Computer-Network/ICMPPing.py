# Author: Yuan Chao 19722073
#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

import socket
import os
import struct
import time
import select
import binascii

ICMP_ECHO_REQUEST = 8  #ICMP type code for echo request messages
ICMP_ECHO_REPLY = 0  #ICMP type code for echo reply messages


def checksum(string):
    csum = 0
    countTo = (len(string) // 2) * 2
    count = 0

    while count < countTo:
        thisVal = string[count + 1] * 256 + string[count]
        csum = csum + thisVal
        csum = csum & 0xffffffff
        count = count + 2

    if countTo < len(string):
        csum = csum + string[len(string) - 1]
        csum = csum & 0xffffffff

    csum = (csum >> 16) + (csum & 0xffff)
    csum = csum + (csum >> 16)
    answer = ~csum
    answer = answer & 0xffff
    answer = answer >> 8 | (answer << 8 & 0xff00)

    answer = socket.htons(answer)

    return answer


def receiveOnePing(icmpSocket, ID, timeout, sent_time):
    '''
    receive the ping from the socket
    '''

    delay = 0

    while timeout - delay > 0:
        # 1. Wait for the socket to receive a reply
        response = select.select([icmpSocket], [], [], timeout)

        # 2. Once received, record time of receipt, otherwise, handle a timeout
        received_time = time.perf_counter()

        if response[0] == []:
            return -1, -1

        # 3. Compare the time of receipt to time of sending, producing the total network delay
        delay = received_time - sent_time

        # 4. Unpack the packet header for useful information, including the ID
        # get ttl
        received_packet, addr = icmpSocket.recvfrom(1024)
        ip_header = received_packet[0:20]
        ip_header_list = struct.unpack('!BBHHHBBHII', ip_header)
        ttl = ip_header_list[5]
        # get ID and ip type
        icmp_header = received_packet[20:28]
        ip_type, code, checksum, packet_ID, sequence = struct.unpack(
            "bbHHh", icmp_header)

        # 5. Check that the ID matches between the request and reply
        if ip_type == ICMP_ECHO_REPLY and packet_ID == ID:  # ip_type should be 0
            # 6. Return total network delay
            return delay, ttl
        # print(ip_type)

        # Handle errors
        if ip_type == 3:
            raise ICMP_Error_type3(code)

    return -1, -1


def sendOnePing(icmpSocket, destinationAddress, ID, data_size):
    '''
    send ping to the given destinationAddress
    '''

    # 1. Build ICMP header
    my_checksum = 0
    header = struct.pack('bbHHh', ICMP_ECHO_REQUEST, 0, my_checksum, ID, 1)

    # 2. Checksum ICMP packet using given function
    data = binascii.a2b_qp('a' * data_size)  # send a 32 bytes data as default
    my_checksum = checksum(header + data)

    # 3. Insert checksum into packet
    header = struct.pack("bbHHh", ICMP_ECHO_REQUEST, 0, my_checksum, ID, 1)
    packet = header + data

    # 4. Send packet using socket
    icmpSocket.sendto(packet, (destinationAddress, 80))

    # 5. Record time of sending
    sent_time = time.perf_counter()

    return sent_time


def doOnePing(destinationAddress, timeout, data_size=32):
    '''
    send ping to destinationAddress for one time
    '''

    # 1. Create ICMP socket
    try:
        icmpSocket = socket.socket(socket.AF_INET, socket.SOCK_RAW, socket.IPPROTO_ICMP)
    except socket.error:
        raise

    # get the process id.
    pid = os.getpid() & 0xFFFF

    # 2. Call sendOnePing function
    sent_time = sendOnePing(icmpSocket, destinationAddress, pid, data_size)

    # 3. Call receiveOnePing function
    delay, ttl = receiveOnePing(icmpSocket, pid, timeout, sent_time)

    # 4. Close ICMP socket
    icmpSocket.close()

    # 5. Return total network delay
    return delay, ttl


def ping(host, times=4, timeout=1, data_size=32):
    '''
    send ping to destinationAddress for some times with the given timeout
    '''

    # 1. Look up hostname, resolving it to an IP address
    ip = socket.gethostbyname(host)
    print('ping ' + host + ' [' + ip + '] with {} bytes of data: '.format(data_size))
    print()
    delay_list = []
    loss_packet = 0
    error = False
    for i in range(times):
        # 2. Call doOnePing function, approximately every second
        try:
            delay, ttl = doOnePing(ip, timeout, data_size)
        except Exception as e:
            print(e)
            error = True
            break

        time.sleep(1 - delay)  # wait about one second

        # 3. Print out the returned delay
        if delay == -1:
            print('failed. (timeout within %s second.)' % timeout)
            loss_packet += 1
        else:
            print('get reply from ' + ip + ' in %0.4f ms' % (delay * 1000) +
                  ': bytes: {}, TTL: {}'.format(data_size, ttl))
            delay_list.append(delay * 1000)

        # 4. Continue this process until stopped
        
    if not error:
        print(
            "\nPackets: Sent: {}, Received: {}, Lost: {} ({}% loss)\n".format(
                times, times - loss_packet, loss_packet,
                loss_packet / times * 100))
        if delay_list:
            print("Delay: Average: %0.4f ms, " % (sum(delay_list) / times) +
                  "Maximum: %0.4f ms, " % max(delay_list) +
                  "Minimum: %0.4f ms\n" % min(delay_list))


# Type3 Errors
class ICMP_Error_type3(Exception):
    def __init__(self, code):
        if code == 0:
            self.message = "Network Unreachable"
        elif code == 1:
            self.message = "Host Unreachable"
        elif code == 2:
            self.message = "Protocol Unreachable"
        elif code == 3:
            self.message = "Port Unreachable"
        elif code == 4:
            self.message = "Fragmentation needed but no frag. bit set"
        elif code == 5:
            self.message = "Source routing failed"
        elif code == 6:
            self.message = "Destination network unknown"
        elif code == 7:
            self.message = "Destination host unknown"
        elif code == 8:
            self.message = "Source host isolated (obsolete)"
        elif code == 9:
            self.message = "Destination network administratively prohibited"
        elif code == 10:
            self.message = "Destination host administratively prohibited"
        elif code == 11:
            self.message = "Network unreachable for TOS"
        elif code == 12:
            self.message = "Host unreachable for TOS"
        elif code == 13:
            self.message = "Communication administratively prohibited by filtering"
        elif code == 14:
            self.message = "Host precedence violation"
        elif code == 15:
            self.message = "Precedence cutoff in effect"
        else:
            self.message = "Unkown Error"

    def __str__(self):
        return self.message


if __name__ == '__main__':
    # ping("lancaster.ac.uk", times=4)
    host = input("Please input the host you want to ping: ")
    ping(host, times=4, timeout=1)
