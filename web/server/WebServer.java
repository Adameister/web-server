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

// must have main class extend Thread so that each command can be handled in parallel.
public class WebServer extends Thread {

    /**
     * @param args the command line arguments
     * There should not be any command line arguments though.
     * This is the main thread of the server that will listen for clients
     */
    public static void main(String[] args) {
        // init the port number. I picked the one mentioned in the Lab 1 PDF
        int portNumber = 6789;

        try {
            // My TCP socket is started. Clients will look for my server's service
            ServerSocket myService = new ServerSocket(portNumber);
            // The server runs in a while loop so that the server does not shutdown.
            // This will cause the server to keep making sockets to satisfy the users.
            while(true) {
                // The thread will pause here and wait for a connection
                System.out.println("Waiting for client on port:" + myService.getLocalPort());
                // This will accept the clients connection to the server on the socket called client
                Socket client = myService.accept();
                System.out.println("Client is connected to the server");

                // There is another class in the HttpRequest.java file that contains all of the
                // instructions for the thread request on how to handle the request.
                HttpRequest request = new HttpRequest(client);

                // Once the new request has been made, execute the request with a new thread
                // so that the system may handle these requests in parallel
                Thread thread = new Thread(request);
                thread.start();
            }
        }
        catch(IOException e) {
            // I put the whole server in a try catch to help gain information on why my program
            // was crashing all of the time. I did not quite understand how to handle the client
            // until I consulted the Java Docs @ http://www.oracle.com/technetwork/java/socket-140484.html
            
            // System.out.println("System could not create new thread");
            System.out.println(e);
        }
    }
}
