/*
 * Program written by Adam Hudson for CSE 4344
 * ID#: 1000991758
 * Date: 2017-10-06
 *
 * Program Objectives:
 *   - Develop a multi-thread web server to handle requests in parallel
 *   - First part of the program will be a multi-thread server that simply displays the contents of the HTTP request message that it receives.
 *   - Second part will have the code to generate the appropriate response to the client.
 */
package web.server;

// Import the required libraries to allow the program to run properly.
import java.util.*;
import java.net.*;
import java.io.*;

// Again, the Offical Java Docs had the ClientWorker impliment the Runnable interface.
// That has one "run" medthod. I made my program slightly diffrent.
public class HttpRequest implements Runnable {
    // Init the socket
    Socket socket;

    // Constructor
    public HttpRequest(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Init the varables that are used through out the process
            String contentBody = "";        // Used for storing HTML content.
            String contentType = "";        // Used for storing what type of documnet we are reading from the header.
            String statusLine = "";         // Stores the HTTP protocol.
            FileInputStream fileInputStream = null;     // Inits the stream that will read the requested HTML file.
            boolean fileExists = true;      // This will determin if there is a 404 error.
            byte[] buffer = new byte[1024]; // This is the buffer that will write the byte code to the web browser.
                                            // I want to use this buffer size so that I do not run out of memory.
                                            // and I know that I will not write more data than that to the buffer.
            int bytes = 0;                  // Used later on for a buffer loop.

            // This will pull information down from the socket
            InputStream inputStream = socket.getInputStream();
            // It will put the information into the outStream
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

            // The BufferedReader takes the request sent by the browser
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            // Format of the request line is GET /Page http-ver
            String requestLine = bufferedReader.readLine();

            // The java.util.* StringTokenizer to break apart the full request string
            StringTokenizer stringToken = new StringTokenizer(requestLine);
            // The second token from the string tokenizer of the request line has the
            // file that the user wants.
            stringToken.nextToken();
            String file = stringToken.nextToken();

            // I could not figure out why my index.html was not read by the server
            // After hours of troubleshooting, turns out the file request has to have
            // a dot in front of it...
            file = "." + file;

            // Try to open the file
            try {
                fileInputStream = new FileInputStream(file);
            }
            catch (FileNotFoundException e) {
                // If the file fails to open, this will trigger the 404 error
                fileExists = false;
            }

            // Prepare an HTTP response:
            //   HTTP/1.1 200 OK
            //   HTTP/1.1 301 Moved Permanently
            //      The redirect should be stored in the html file's meta data.
            //      For some reason, my error 301 keeps reporting as 404.
            //   HTTP/1.0 404 Not Found
            if(fileExists) {
                statusLine = "HTTP/1.1 200 OK: ";
                contentType = "Content-Type: text/html\r\n";
            }
            else {
                statusLine = "HTTP/1.1 404 Not Found: ";
                contentType = "Content-Type: text/html\r\n";
                // For some reason, my server does not always interpret this html code
                // It instead shows up as exactly this in the web browser
                contentBody = "<!DOCTYPE html><html><body><h1>404 Not Found</h1></body></html>";
            }

            // Send the computed data back to the client. The \r\n tells the browser when to stop
            // looking for more data from the server socket
            outStream.writeBytes(statusLine);
            outStream.writeBytes(contentType);
            outStream.writeBytes("\r\n");

            if(fileExists){
                while((bytes = fileInputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytes);
                }
                fileInputStream.close();
            }
            else {
                outStream.writeBytes(contentBody);
            }

            // Before the thread ends, clean up and close out all of the things still open.
            outStream.close();
            bufferedReader.close();
            socket.close();
        }
        catch(IOException e) {
            System.out.println("System could not process the request.");
        }
    }
}