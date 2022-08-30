/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cipherbit.chesswars.chesswars;

public class Player {

    private int row1, row2, col1, col2;
    private boolean myTurn;     //if it's the player's turn or not
    private int numpieces;      //number of pieces, the player owns
    private String color;       //the color chosen by the player

    public Player(int np, String colour) {
        np = this.numpieces;
        colour = this.color;
    }
    public int getRow1 () {
        return row1;
    }
    public int getRow2 () {
        return row2;
    }
    public int getCol1 () {
        return col1;
    }
    public int getCol2 () {
        return col2;
    }
    public boolean getTurn () {
        return myTurn;
    }
    public String getColor () {
        return color;
    }
    public int getNumPieces () {
        return numpieces;
    }
    public void setNumPieces (int pieces) {
        numpieces = pieces;
    }
    public void setRow1 (int rowone) {
        row1 = rowone ;
    }
    public void setRow2 (int rowtwo) {
        row2 = rowtwo;
    }
    public void setCol1 (int colone) {
        col1 = colone;
    }
    public void setCol2 (int coltwo) {
        col2 = coltwo;
    }
    public void setTurn (boolean turn) {
        myTurn = turn;
    }
    public void setColor (String colour){
        color = colour;
    }
}
