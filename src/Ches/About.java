/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Ches;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Kaan
 */
public class About extends JFrame implements MouseListener {
    private BufferedImage boardImage;
    private Image cursorImage;
    private Image Logo;
    
    public About () {
        JPanel panel = new JPanel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        cursorImage = tk.getImage("images/chesswars2.gif");
        Point cursorHotSpot = new Point(0,0);
        Cursor customCursor = tk.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
        
        Image icon = Toolkit.getDefaultToolkit().getImage("images/nuke.jpg");
        this.setIconImage(icon);
        
        try{
            Logo = ImageIO.read(new File("images/ChessLogo.gif"));
        } catch (IOException ioe) {ioe.toString();}

        this.addMouseListener(this);
        this.setLocation(((dim.width/2)-(350/2)), (dim.height/2)-(350/2));
        this.setCursor(customCursor);        
        this.setTitle("Chess Wars : About");
        this.setSize(350, 350);
        this.setVisible(false);
        this.repaint();
    }
    
    public void paint(Graphics g){

        super.paintComponents(g);
        
        g.setColor(Color.RED);
        g.drawImage(Logo, 134, -20, this);
        g.drawString("CHESS WARS", 15, 45);
        g.setColor(Color.BLACK);
        g.drawString("Chess Wars is", 15, 80);
        g.drawString("Created by Artificial", 15, 95);
        g.drawString("Minds(TM). It can only be", 15, 110);
        g.drawString("distributed and produced", 15, 125);
        g.drawString("by the company itself.", 15, 140);
        g.drawString("The purpose of this software", 15, 155);
        g.drawString("is to help people learn", 15, 170);
        g.drawString("and enjoy the classic", 15, 185);
        g.drawString("chess game.", 15, 200);
        g.drawString("Thank you for playing!", 15, 240);
        g.drawString("Please contact: ", 15, 270);
        g.drawString("Artificial Minds for inquiries", 15, 285);
        g.drawString("Lead Design & Programming:", 15, 315);
        g.drawString("Kaan Ersan", 15, 330);
    }

    

    public void mouseReleased(MouseEvent event){
        this.setVisible(false);
        this.dispose();
    }

    public void mouseClicked(MouseEvent e) {
        
    }

    public void mousePressed(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
        
    }

}
