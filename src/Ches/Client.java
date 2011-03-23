package Ches;

import java.io.*;
import java.net.*;

public class Client {

    public Socket socket = null;
    public PrintWriter out = null;
    public BufferedReader in = null;
    public BufferedReader stdIn = null;

    public Client(String ip, int port) throws IOException {

        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + ip + ".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ip+".");
            System.exit(1);
        }

        stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        /*while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            if (fromServer.equals("Bye."))
                break;
		    
            fromUser = stdIn.readLine();
	    if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                out.println(fromUser);
	    }
        }*/


    }

    public void closeClient() throws IOException {
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }

    public void sendData(String data) {
        out.println(data);
    }

    public String receiveData() throws IOException {
        String data = "";
        System.out.println("Client -> receive data called");
        while ((data = in.readLine()) == null){
            System.out.println("Client waiting to receive data");
        }
        //data = in.readLine();
        return data;
    }

}
