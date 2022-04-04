# Author: Yuan Chao 19722073
#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

import socket
import threading


def handleRequest(tcpSocket):
    # 1. Receive request message from the client on connection socket
    receiveMsg = tcpSocket.recv(1024)
    receiveMsg = receiveMsg.decode('utf-8')
    # print(receiveMsg)

    # 2. Extract the path of the requested object from the message (second part of the HTTP header)
    lineaArray = receiveMsg.split('\r\n')
    url = lineaArray[0].split(" ")[1]
    
    # 3. Read the corresponding file from disk
    try:
        if url == "/":
            url = '/index.html'
        url = url[1:]
        http_response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n"
        with open(url, 'r') as fp:
            file = fp.read()
            # 4. Store in temporary buffer
            http_response += "\n" + file
            fp.close()
            print('Thread ' + str(threading.currentThread().ident) + ': request for ' + url + ' successfully.')
    except Exception as e:
        # 5. Send the correct HTTP response error
        http_response = "HTTP/1.1 404 NOTFOUND\r\nContent-Type: text/html\r\n\n404 NOTFOUND"
        print('Thread ' + str(threading.currentThread().ident) + ': request for ' + url + ' failed.')

    # 6. Send the content of the file to the socket
    tcpSocket.send(http_response.encode())

    # 7. Close the connection socket
    tcpSocket.close()


def startServer(serverAddress, serverPort):

    # 1. Create server socket
    tcpsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM,
                              socket.IPPROTO_TCP)

    # 2. Bind the server socket to server address and server port
    tcpsocket.bind((serverAddress, serverPort))

    # 3. Continuously listen for connections to server socket
    tcpsocket.listen(3)
    print("=====The server is already=====")

    # 4. When a connection is accepted, call handleRequest function, passing new connection socket
    while True:
		# multithreaded
        threading.Thread(target=thread, args=(tcpsocket.accept(), )).start()

    # 5. Close server socket
    tcpsocket.close()


def thread(accept):
    clientConn, clientAddress = accept
    handleRequest(clientConn)


if __name__ == '__main__':
    startServer('127.0.0.1', 8000)
