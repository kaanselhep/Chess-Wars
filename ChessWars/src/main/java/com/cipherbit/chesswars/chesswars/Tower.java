/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cipherbit.chesswars.chesswars;

/**
 *
 * @author Kaan
 */
public class Tower extends Piece {
	
    public Tower (String colour, int r, int c) {
        super.setRow(r);
        super.setCol(c);
        super.setValue(50);
        super.setRepresentation('T');
        super.setColor(colour);
    }
	
    public boolean canMove (int row2, int col2) {
        if ((row2==super.gRow()) && (col2==super.gCol())){
                return false;
        }
        else if ((super.gRow()==row2)||(super.gCol()==col2)){
                return true;	
        }
        else {
                return false;
        }
    }
}
