# Author: Yuan Chao 19722073
#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

import socket
import sys
import threading
import socket
import re
import select
import os


def handleRequest(clientSocket):
    # Receive request message from the client on connection socket
    receiveMsg = clientSocket.recv(4096)
    receiveMsg = receiveMsg.decode('utf-8', 'ignore')

    # Extract the path of the requested object from the message (second part of the HTTP header)
    firstLine = receiveMsg[:receiveMsg.find('\r\n')]
    print(firstLine)
    head = firstLine.split(" ")
    if len(head) < 2:  # empty message
        return
    url = head[1]

    method = head[0]
    host = ''
    port = 80  # HTTP default port

    # get the host and port
    if method != 'CONNECT':
        url = url[url.find('/') + 2:]
        host = url.split('/')[0]
        file = url[url.find('/'):]
    else:
        port = int(url.split(':')[1])
        host = url.split(':')[0]

    print('   Host: ' + host)
    print('   Port: ' + str(port))

    # Connect to the server.
    serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    try:
        serverSocket.connect((host, port))
        # CONNECT method (HTTPS)
        if method == 'CONNECT':
            clientSocket.send('HTTP/1.1 200 Connection established\nProxy-agent: 1.0\n\n'.encode('utf-8'))
            exMessage(clientSocket, serverSocket)
			# Close the connection socket
            clientSocket.close()
            return
        try:
            version = head[2]
        except IndexError:
            version = 'HTTP/1.1'
        # Other methods

        serverSocket.send((method+' '+ file+' '+version).encode('utf-8'))
        serverSocket.send(receiveMsg[receiveMsg.find('\r\n'):].encode('utf-8'))
    except:
        serverSocket.close()
        return
    exMessage(clientSocket, serverSocket)

    # Close the connection socket
    clientSocket.close()
    serverSocket.close()


def exMessage(client, server):
    over = False
    while not over:
        recv, _, error = select.select([client, server], [], [client, server],
                                       3)
        if error or len(recv) == 0:
            break
        try:
            if recv:
                for s in recv:
                    print
                    data = s.recv(4096)
                    if len(data) == 0:
                        over = True
                        break
                    if s == client:
                        server.send(data)
                    else:
                        client.send(data)
        except:
            raise


def startProxy(serverAddress, serverPort):

    # 1. Create server socket
    tcpsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM,
                              socket.IPPROTO_TCP)

    # 2. Bind the server socket to server address and server port
    tcpsocket.bind((serverAddress, serverPort))

    # 3. Continuously listen for connections to server socket
    tcpsocket.listen(1)
    print("=====The proxy is already=====")

    # 4. When a connection is accepted, call handleRequest function, passing new connection socket
    while True:
        threading.Thread(target=proxy_thread, args=(tcpsocket.accept(), )).start()

    # 5. Close server socket
    tcpsocket.close()


def proxy_thread(accept):
    clientSocket, clientAddress = accept
    handleRequest(clientSocket)


if __name__ == '__main__':
    if not os.path.exists('cache'):
        os.makedirs('cache')
    # port = input("Input the port: ")
    startProxy("", 8000)
