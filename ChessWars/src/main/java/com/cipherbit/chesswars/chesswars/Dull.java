/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cipherbit.chesswars.chesswars;

/**
 *
 * @author Kaan
 */
public class Dull extends Piece {
    
    public Dull (String colour, int r, int c, char rep) {
        super.setRow(r);
        super.setCol(c);
        super.setValue(5);
        super.setRepresentation(' ');
        super.setColor(colour);
        super.setMoveCounter(0);
    }
    
    public boolean canMove (int row2, int col2){
    	return false;
    }     
    
    public void Move(int row2, int col2) {
        super.Move(row2, col2);
    }
}
