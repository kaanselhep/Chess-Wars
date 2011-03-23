package Ches;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Server {
    
    public PrintWriter out;
    public BufferedReader in;
    public BufferedReader br;
    public Socket clientSocket = null;
    public ServerSocket serverSocket = null;
    public String message = "";
    
    //constructor1: empty
    public Server ()  {

    }
    //constructor2: create server
    public Server(String ip, int port) throws IOException {

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(15000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            message = "Could not listen on port: " + port + ".";
            JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
            JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public boolean createServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(15000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            message = "Could not listen on port: " + port + ".";
            JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
            JOptionPane.ERROR_MESSAGE);
            try {
                this.closeServer();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
        return true;
    }

    public boolean connect() throws IOException {

        try {
            clientSocket = serverSocket.accept();
        }
        catch (SocketTimeoutException  e) {
            System.err.println("Connection timed out: " + e.getMessage());
            message = "Connection timed out.\nTry again.";
            JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                JOptionPane.ERROR_MESSAGE);
            this.closeServer();
            return false;
        }
        catch (IOException e) {
            System.err.println("Accept failed: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("over here2");
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        br = new BufferedReader(new InputStreamReader(System.in));

        return true;
    }

    public void closeServer() throws IOException {
        if (out != null)
            out.close();
        if (in != null)
            in.close();
        if (clientSocket != null)
            clientSocket.close();
        if (serverSocket != null)
            serverSocket.close();
    }

    public void sendData(String data) {
        out.println(data);
    }

    public String receiveData() throws IOException {
        String data;
        data = in.readLine();
        return data;
    }

}
