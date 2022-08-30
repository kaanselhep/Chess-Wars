/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cipherbit.chesswars.chesswars;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Kaan
 */
public class King extends Piece {
    
    public boolean isCastling = false;
    public BufferedImage checkImage;

    public King (String colour, int r, int c){
        super.setRow(r);
        super.setCol(c);
        super.setValue(500);
        super.setRepresentation('K');
        super.setColor(colour);
    }
   
    public boolean getCastling() {
        return this.isCastling;
    }
    
    public void setCastling(boolean castling) {
        this.isCastling = castling;
    }
    
    public boolean canMove (int row2, int col2) {
        
        if ((row2==super.gRow()) && (col2==super.gCol())){
            return false;
        }
        else if ((super.getMoveCounter() == 0) && (super.gRow()==row2) && (Math.abs(super.gCol()-col2)==2)){
            this.setCastling(true);
            return true;
        }
        else if (Math.abs(super.gCol()-col2)<=1 && Math.abs(super.gRow()-row2)<=1){
            return true;
        }
        else {
            return false;
        }
    }

    public void getCheckImage()  {
        try{
            this.image = ImageIO.read(new File("images/"+this.getColor()+this.getRepresentation()+"CHECK.PNG"));
        }
        catch (IOException e) {e.toString();}
    }
}
