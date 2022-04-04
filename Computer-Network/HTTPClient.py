# Author: Yuan Chao 19722073
#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

import socket
import re
import select


def start_Client(URL):

    # Get the IP address and Port
    array = re.split("/", URL, 1)
    ip_port = array[0]

    url = array[1]
    ip, port = re.split(":", ip_port, 1)
    port = int(port)
    # Connect to the server.
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect((ip, port))

    # Generate HTTP GET request.
    request = "GET /" + url + " HTTP/1.1\r\n" + "Host: " + ip_port + "\r\n\r\n"
    # print(request)
    client_socket.send(request.encode())

    # Receive the response.
    client_socket.setblocking(0)

    select.select([client_socket], [], [], 2)

    response = client_socket.recv(1024).decode("utf-8", "ignore")
    print(response)
    client_socket.close()


if __name__ == '__main__':
    start_Client('127.0.0.1:8000/index.html')
