#
import sys
from socket import *
import os.path
import time
import threading
from datetime import datetime
from email.utils import parsedate, formatdate


# Each incoming connection is handled in its own thread,
# allowing for multiple concurrent connections
class HandleClient(threading.Thread):
    def __init__(this, client):
        super().__init__()
        this.client = client
        
    def run(this):        
        # Read the request from the socket
        in_file = this.client.makefile('r', newline='\r\n')  # create file wrapper and specify line endings

        try:
            # Loop to keep handling requests in a persistent connection
            while True:
                #  Parse the request headers to a HttpRequest class
                request = HttpRequest(in_file)
                
                # Handle the request and send the response
                this.handleHttpRequest(this.client, request)

                # Just close connection after handling one request for HTTP/1.0
                if request.protocol == "HTTP/1.0":
                    print("Closing connection")
                    break
        except:
            print("Client disconnected")            
            
        finally:
            this.client.close()

                    
    def handleHttpRequest(this, client, request):
        """
        Sends a response back to the client
        :param client: Socket that handles the client connection
        :param response: the response that should be send to the client
        """
        client.sendall(bytes(request.protocol + " ", "ASCII"))
        http11 = request.protocol == "HTTP/1.1"
        # look for resource
        try:
            mtime = int(os.path.getmtime("."+request.url))

            # Handle Condition GET            
            if "If-Modified-Since" in request.fields:
                # Use library to convert to correct date format
                if_mod_since = datetime(*parsedate(request.fields["If-Modified-Since"])[:6])

                # check if request date is earlier
                if if_mod_since >= datetime.utcfromtimestamp(mtime):
                    client.sendall(b"304 Not Modified\r\nContent-Length: 0\r\n\r\n")  # no content is sent
                    return
            
            with open("."+request.url, "rb") as f:
                # resource exists
                client.sendall(b"200 OK\r\n")
                client.sendall(bytes(f'Last-Modified: {formatdate(mtime, usegmt=True)}\r\n', 'ASCII'))

                # Do chunked transfer for HTTP/1.1 to handle streaming content where content length is unknown
                if http11:
                    client.sendall(b"Transfer-Encoding: chunked\r\n");
                else:
                    client.sendall(b"\r\n")

                # read/send in 1KB chunks
                buff = f.read(1024);
                while buff:
                    if http11:                        
                        client.sendall(b"\r\n%x\r\n"%len(buff))  # chuck header is just size of chunk in hex
                        
                    client.sendall(buff)
                    buff = f.read(1024)
            if http11:
                client.sendall(b"\r\n0\r\n\r\n")  # send closing chunk of 0 bytes
                    
        except FileNotFoundError:
            client.sendall(b"404 Not Found\r\nContent-Length: 0\r\n\r\n")

        except Exception as e:
            print(e)




class HttpRequest:
    def __init__(this, in_file):
        this.fields = {}
        (this.req, this.url, this.protocol) = in_file.readline().split()
        #print(f"{this.protocol}")

        # read each line from the stream. This function blocks and reads until newline,
        # thus it is not affected by how the stream is segmented
        line = in_file.readline()
        
        while line != '\r\n':  # HTTP headers end with a blank line
            #print(f'"{line}"')
            key, val = line.split(":", 1)
            this.fields[key.title()] = val.strip()  # remove surrounding whitespace
            line = in_file.readline()  # read next line



if __name__ == "__main__":
    try:
        port = int(sys.argv[1])
    except:
        print("Usage: python WebServer.py <port>")
        exit(1)

s_sock = socket()
s_sock.bind(("0.0.0.0", port))
s_sock.listen(5)

while True:
    # accept incoming client
    c_sock, addr = s_sock.accept()
    print(f"Client connected from {addr}")
    HandleClient(c_sock).start()
   


              
