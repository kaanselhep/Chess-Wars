/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cipherbit.chesswars.chesswars;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Kaan
 */
public class Promotion extends JFrame  {
    
    private Image cursorImage;
    private Image Logo;
    private ImageIcon queenIcon;
    private ImageIcon knightIcon;
    private ImageIcon bishopIcon;
    private ImageIcon towerIcon;
    private String color = "WHITE";
    public char piece = 'Q';
    public ButtonGroup group;
    public JRadioButton queenButton;
    public JRadioButton knightButton;
    public JRadioButton rookButton;
    public JRadioButton bishopButton;
    public boolean shouldWait = true;
    public JButton okButton;
    
    public Promotion () {
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        cursorImage = tk.getImage("images/chesswars2.gif");
        Point cursorHotSpot = new Point(0,0);
        Cursor customCursor = tk.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Image icon = Toolkit.getDefaultToolkit().getImage("images/nuke.jpg");
        this.setIconImage(icon);
       
        //bishopIcon = Toolkit.getDefaultToolkit().getImage("images/WHITEB.PNG");
        //towerIcon = Toolkit.getDefaultToolkit().getImage("images/WHITET.PNG");

        try{
            Logo = ImageIO.read(new File("images/ChessLogo.gif"));
        } catch (IOException ioe) {ioe.toString();}
        
        this.setLocation(((dim.width/2)-(350/2)), (dim.height/2)-(350/2));
        this.setCursor(customCursor);        
        this.setTitle("Chess Wars : Promote Pawn");
        this.setSize(350, 110);
        
        okButton = new JButton("OK");
        okButton.setMnemonic(KeyEvent.VK_ENTER);
        group = new ButtonGroup();
        JPanel panel = new JPanel();
        
        queenButton = new JRadioButton("Queen");
        queenButton.setActionCommand("Queen");
        queenButton.setMnemonic('q');
        queenButton.setSelected(true);
        
        knightButton = new JRadioButton("Knight");
        knightButton.setActionCommand("Knight");
        knightButton.setMnemonic('k');
        
        rookButton = new JRadioButton("Rook");
        rookButton.setActionCommand("Rook");
        rookButton.setMnemonic('r');
        
        bishopButton = new JRadioButton("Bishop");
        bishopButton.setActionCommand("Bishop");
        bishopButton.setMnemonic('b');
        

        this.getRootPane().setDefaultButton(okButton);
        
        group.add(queenButton);
        panel.add(queenButton);
        group.add(knightButton);
        panel.add(knightButton);
        group.add(rookButton);
        panel.add(rookButton);
        group.add(bishopButton);
        panel.add(bishopButton);
        
        panel.add(okButton);

        queenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                piece = 'Q';
                if (queenButton.isSelected()) {
                    queenIcon = new ImageIcon("images/GREENQ.PNG");
                    knightIcon = new ImageIcon("images/" + color + "N.PNG");
                    bishopIcon = new ImageIcon("images/" + color + "B.PNG");
                    towerIcon = new ImageIcon("images/" + color + "T.PNG");
                }
                queenButton.setIcon(queenIcon);
                knightButton.setIcon(knightIcon);
                bishopButton.setIcon(bishopIcon);
                rookButton.setIcon(towerIcon);
            }
        });
        rookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                piece = 'T';
                if (rookButton.isSelected()) {
                    queenIcon = new ImageIcon("images/" + color + "Q.PNG");
                    knightIcon = new ImageIcon("images/" + color + "N.PNG");
                    bishopIcon = new ImageIcon("images/" + color + "B.PNG");
                    towerIcon = new ImageIcon("images/GREENT.PNG");
                }
                queenButton.setIcon(queenIcon);
                knightButton.setIcon(knightIcon);
                bishopButton.setIcon(bishopIcon);
                rookButton.setIcon(towerIcon);
            }
        });
        knightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                piece = 'N';
                if (knightButton.isSelected()) {
                    queenIcon = new ImageIcon("images/" + color + "Q.PNG");
                    knightIcon = new ImageIcon("images/GREENN.PNG");
                    bishopIcon = new ImageIcon("images/" + color + "B.PNG");
                    towerIcon = new ImageIcon("images/" + color + "T.PNG");
                }
                queenButton.setIcon(queenIcon);
                knightButton.setIcon(knightIcon);
                bishopButton.setIcon(bishopIcon);
                rookButton.setIcon(towerIcon);
            }
        });
        bishopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                piece = 'B';
                if (bishopButton.isSelected()) {
                    queenIcon = new ImageIcon("images/" + color + "Q.PNG");
                    knightIcon = new ImageIcon("images/" + color + "N.PNG");
                    bishopIcon = new ImageIcon("images/GREENB.PNG");
                    towerIcon = new ImageIcon("images/" + color + "T.PNG");
                }
                queenButton.setIcon(queenIcon);
                knightButton.setIcon(knightIcon);
                bishopButton.setIcon(bishopIcon);
                rookButton.setIcon(towerIcon);
            }
        });

        this.add(panel);
        this.setResizable(false);
        this.setVisible(false);
        
       okButton.addActionListener(new ActionListener() {
            public synchronized void actionPerformed(ActionEvent event){
                group.clearSelection();
                shouldWait = false;
                setVisible(false);
                queenButton.setSelected(true);
            }
        });
        
    }
 
    public char getPiece() {
        return this.piece;
    }

    public void setIcons(String colour) {
        this.color = colour;

        if (colour == "WHITE") {
            queenIcon = new ImageIcon("images/GREENQ.PNG");
            knightIcon = new ImageIcon("images/WHITEN.PNG");
            bishopIcon = new ImageIcon("images/WHITEB.PNG");
            towerIcon = new ImageIcon("images/WHITET.PNG");

            queenButton.setIcon(queenIcon);
            queenButton.setSelected(true);
            knightButton.setIcon(knightIcon);
            knightButton.setSelected(false);
            rookButton.setIcon(towerIcon);
            rookButton.setSelected(false);
            bishopButton.setIcon(bishopIcon);
            bishopButton.setSelected(false);
        }
        else {
            queenIcon = new ImageIcon("images/GREENQ.PNG");
            knightIcon = new ImageIcon("images/BLACKN.PNG");
            bishopIcon = new ImageIcon("images/BLACKB.PNG");
            towerIcon = new ImageIcon("images/BLACKT.PNG");

            queenButton.setIcon(queenIcon);
            queenButton.setSelected(true);
            knightButton.setIcon(knightIcon);
            knightButton.setSelected(false);
            rookButton.setIcon(towerIcon);
            rookButton.setSelected(false);
            bishopButton.setIcon(bishopIcon);
            bishopButton.setSelected(false);
        }
    }
}
