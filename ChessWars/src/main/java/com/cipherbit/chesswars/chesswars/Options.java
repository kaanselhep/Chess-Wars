/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cipherbit.chesswars.chesswars;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Kaan
 */
public class Options extends JFrame {
    
    private BufferedImage boardImage;
    private Image cursorImage, Logo;
    private ButtonGroup group;
    private JRadioButton singlePlayer, multiPlayer;
    private JButton okButton, cancelButton;
    private JTextField ipText, portText;
    private JLabel ip, ipInfo, port, infoMsg;

    public Server server;
    public Client client;
    public boolean connection;
    
    //0=single, 1=server, 2=client
    public int gameType = 0;
    
    public Options() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        cursorImage = tk.getImage("images/chesswars2.gif");
        Point cursorHotSpot = new Point(0,0);
        Cursor customCursor = tk.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");

        Image icon = Toolkit.getDefaultToolkit().getImage("images/nuke.jpg");
        this.setIconImage(icon);
        ipText = new JTextField();
        ipInfo = new JLabel("(leave blank to be server)");
        ip = new JLabel("ip #:");
        portText = new JTextField();
        port = new JLabel("port #:");
        infoMsg = new JLabel("Waiting for client connection...");
        infoMsg.setForeground(Color.RED);
        infoMsg.setVisible(false);
        okButton = new JButton("OK");
        okButton.setMnemonic(KeyEvent.VK_ENTER);    
        cancelButton = new JButton("Cancel");
        singlePlayer = new JRadioButton("Single-Player Game");
        multiPlayer = new JRadioButton("Multi-Player Game");
        group = new ButtonGroup();
        
        this.setLocation(((dim.width/2)-(350/2)), (dim.height/2)-(350/2));
        this.setCursor(customCursor);        
        this.setTitle("Chess Wars : Options");
        this.setSize(350, 350);
        
        ipText.setLocation(70, 150);
        ipText.setSize(100, 30);
        ipText.setEnabled(false);
        ip.setLocation(30, 150);
        ip.setSize(50,30);
        ipInfo.setLocation(180, 150);
        ipInfo.setSize(150, 30);
        portText.setLocation(70, 200);
        portText.setSize(50,30);
        portText.setText("1024");
        portText.setEnabled(false);
        port.setLocation(30, 200);
        port.setSize(50,30);
        infoMsg.setLocation(((this.getSize().width)/2)-85, (this.getSize().height)-100);
        infoMsg.setSize(200, 30);
        okButton.setLocation(((this.getSize().width)/2)-105, (this.getSize().height)-70);
        okButton.setSize(90, 30);
        cancelButton.setLocation(((this.getSize().width)/2)+25, (this.getSize().height)-70);
        cancelButton.setSize(90,30);
        singlePlayer.setLocation(20, 20);
        singlePlayer.setSize((this.getSize().width)-20,30);
        singlePlayer.setSelected(true);
        multiPlayer.setLocation(20, 90);
        multiPlayer.setSize((this.getSize().width)-20,30);
        
        group.add(singlePlayer);
        group.add(multiPlayer);
        
        panel.add(okButton);
        panel.add(cancelButton);
        panel.add(singlePlayer);
        panel.add(multiPlayer);
        panel.add(ipText);
        panel.add(portText);
        panel.add(ip);
        panel.add(port);
        panel.add(infoMsg);
        panel.add(ipInfo);
        this.add(panel);
        
        cancelButton.addActionListener(new ActionListener() {
            public synchronized void actionPerformed(ActionEvent event){
                setVisible(false);
            }
        });
        
        
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                 if (multiPlayer.isSelected() && ipText.getText().equalsIgnoreCase("")) {
                     infoMsg.setVisible(true);
                     repaint();
                 }
            }
            @Override
            public void mouseReleased(MouseEvent event){
                if (multiPlayer.isSelected()) {
                    try {
                        final int port = Integer.parseInt(portText.getText());
                        if (ipText.getText().equalsIgnoreCase("")) {
                            System.out.println("Starting server");
                            gameType = 1;
                            server = new Server();
                            if (server.createServer(port)) {
                                connection = server.connect();
                            }
                            if (connection == true) {
                                System.out.println("Server ready.");
                            }
                            else {
                                singlePlayer.setSelected(true);
                                infoMsg.setVisible(false);
                                server.closeServer();
                                gameType = 0;
                                setVisible(false);
                            }
                        }
                        else {
                            System.out.println("Connecting to server");
                            gameType = 2;
                            client = new Client(ipText.getText(), port);
                            System.out.println("Connected to: " + ipText.getText() + " on port: " + String.valueOf(port));
                        }
                    } catch (IOException ex) {
                        System.err.println("Couldn't establish connection.");
                        System.exit(1);
                    }

                }
                setVisible(false);
            }
        });
        
        multiPlayer.addActionListener(new ActionListener() {
            public synchronized void actionPerformed(ActionEvent event){
                portText.setEnabled(true);
                ipText.setEnabled(true);
            }
        });
        singlePlayer.addActionListener(new ActionListener() {
            public synchronized void actionPerformed(ActionEvent event){
                portText.setEnabled(false);
                ipText.setEnabled(false);
            }
        });
        
        
        this.setResizable(false);
        this.setVisible(false);
        this.repaint();
        
    }
}
