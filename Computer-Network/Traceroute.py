# Author: Yuan Chao 19722073
#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

import socket
import os
import struct
import time
import binascii
import select

ICMP_ECHO_REQUEST = 8  #ICMP type code for echo request messages
ICMP_ECHO_REPLY = 0  #ICMP type code for echo reply messages

DESTINATION_REACHED = 1
SOCKET_TIMEOUT = 2


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


def receiveOnePing(icmpSocket, pid, timeout, sent_time):
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
            return None, None, None

        # 3. Compare the time of receipt to time of sending, producing the total network delay
        delay = received_time - sent_time

        # 4. Unpack the packet header for useful information, including the ID
        received_packet, (ip, _) = icmpSocket.recvfrom(1024)
        # get ID and ip type
        icmp_header = received_packet[20:28]
        ip_type, code, checksum, packet_ID, sequence = struct.unpack("bbHHh", icmp_header)

        # 5. Check that the ID matches between the request and reply
        if ip_type == 11 and code == 0:
            return delay, ip, None
        # cannot reached the port
        elif ip_type == ICMP_ECHO_REPLY and code == 0 and pid == packet_ID:
            return delay, ip, DESTINATION_REACHED

    return None, None, None


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


def doOnePingWithUDP(destAddr, timeout, ttl):
    # Create UDP socket
    udpSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    udpSocket.setsockopt(socket.SOL_IP, socket.IP_TTL, ttl)

    # sendOnePing
    udpSocket.sendto(b"", (destAddr, 80))
    # Record time of sending
    sent_time = time.perf_counter()
    # Close UDP socket
    udpSocket.close()

    # Create ICMP socket
    icmp = socket.getprotobyname("icmp")
    icmpSocket = socket.socket(socket.AF_INET, socket.SOCK_RAW, icmp)
    icmpSocket.bind(('', 80))
    icmpSocket.settimeout(timeout)

    # get the process id
    pid = os.getpid() & 0xFFFF
    delay, ip, state = receiveOnePing(icmpSocket, pid, timeout, sent_time)

    # Close ICMP socket
    icmpSocket.close()

    return delay, ip, state

def doOnePingWithICMP(destAddr, timeout, ttl):
    # Create ICMP socket  
    icmp = socket.getprotobyname("icmp")
    icmpSocket = socket.socket(socket.AF_INET, socket.SOCK_RAW, icmp)
    icmpSocket.setsockopt(socket.SOL_IP, socket.IP_TTL, ttl)
    icmpSocket.settimeout(timeout)

    # Call sendOnePing function
    pid = os.getpid() & 0xFFFF
    sent_time = sendOnePing(icmpSocket, destAddr, pid, data_size=32)

    # Call receiveOnePing function
    delay, ip, state = receiveOnePing(icmpSocket, pid, timeout, sent_time)

    # Close ICMP socket
    icmpSocket.close()

    return delay, ip, state


def printInfo(delay):
    if not delay:
        print('*', end='\t\t', flush=True)
        return None

    delay *= 1000
    print('{:.3f} ms'.format(delay), end='\t', flush=True)


def traceroute(host, timeout, maxHops, times=3, method='icmp'):
    dest = socket.gethostbyname(host)
    print("""Tracing route to %s (%s) 
over a maximum of %d hops\n""" % (host, dest, maxHops))
    # Send ping requests to a server separated by approximately one second
    print("TTL\t Delay1\t\tDelay2\t\tDelay3\t\tAddress")
    for ttl in range(1, maxHops + 1):
        print('{:2}\t'.format(ttl), end=' ', flush=True)
        ip = None
        for i in range(times):
            if method == 'icmp':
                delay, temip, state = doOnePingWithICMP(dest, timeout, ttl)
            else:
                delay, temip, state = doOnePingWithUDP(dest, timeout, ttl)
                
            printInfo(delay)
            if not ip:
                ip = temip
        if ip:
            # Resolve the IP addresses found in the responses to their respective hostnames
            try:
                addr, _, _ = socket.gethostbyaddr(ip)
            except:
                addr = ip
            print('{} ({})'.format(ip, addr), flush=True)
        else:
            print("Request timed out.")

        # reached the destination
        if state == DESTINATION_REACHED:
            break


if __name__ == '__main__':
    address ='lancaster.ac.uk'
    # address = input("Input the host address: ")
    traceroute(host = address, timeout = 10, maxHops = 30, method = 'udp')
