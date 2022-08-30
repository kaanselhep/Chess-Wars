/**
 *
 * @author Kaan
 */

package com.cipherbit.chesswars.chesswars;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Piece {
	
    private String color;
    private char representation;
    private int value;
    private int row;
    private int col;
    private int moveCounter = 0;    //counts the number of moves the piece has taken

    public BufferedImage image;
    
    public boolean selected = false;
    
    public Piece() {
        this.moveCounter = 0;
    }
    
    public void setColor(String colour){
        this.color = colour;
    }
    
    @Override
    public String toString() {
        String result = "";
        result += color.substring(0,1);
        result += Character.toString(this.representation);
        result += Integer.toString(this.row);
        result += Integer.toString(this.col);
        result += Integer.toString(this.moveCounter);
        return result;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public void getImage()  {
        try{
            this.image = ImageIO.read(new File("images/"+this.color+this.representation+".PNG"));
        }
        catch (IOException e) {e.toString();}
    }

    public void playSound() {
        new AePlayWave("sounds/" + this.color + this.representation + ".wav").start();
    }

    public int gRow() {
        return this.row;
    }
    
    public int gCol() {
        return this.col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setValue (int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setMoveCounter(int counter) {
        this.moveCounter = counter;
    }
    
    public int getMoveCounter() {
        return this.moveCounter;
    }

    public char getRepresentation () {
        return representation;
    }

    public void setRepresentation (char r) {
        this.representation = r;
    }

    public boolean canMove (int row2, int col2) {
        return true;
    }
    
    public double getDistance (Piece p) {
        double distance = 0;
        
        if (this.row == p.row)
            distance = Math.abs(this.col - p.col);
        else if (this.col == p.col)
            distance = Math.abs(this.row - p.row);
        else if (Math.abs(this.col-p.col)<=1 && Math.abs(this.row-p.row)<=1)
            distance = 1;
        else
            distance = Math.sqrt(Math.pow(Math.abs(this.row-p.row),2) + Math.pow(Math.abs(this.col-p.col),2));
        
        return distance;
    }
    
    public int getDirection(int row2, int col2) {
        int direction = 0;
        
        if (row2==this.row & col2 == this.col){
            return 8;
        }
        //North west
        else if (row2<this.row & col2<this.col)
            direction = 7;	
	//North
	else if (row2<this.row & col2==this.col)
            direction = 0;
	//North east
	else if (row2<this.row & col2>this.col)
            direction = 1;
	//East
        else if (row2==this.row & col2>this.col)
            direction = 2;
	//South east		
	else if (row2>this.row & col2>this.col)
            direction = 3;	
	//South
	else if (row2>this.row & col2==this.col)
            direction = 4;
	//South west
	else if (row2>this.row & col2<this.col)
            direction = 5;	
	else 
            direction = 6;
            
        return direction;
    }
    
    public String getAttackingColor (){
        if (this.color == "WHITE")
            return "BLACK";
        else if (this.color == "BLACK")
            return "WHITE";
        else
            return "NOCOLOR";
    }
    
    public void Move (int row2, int col2){
        this.setRow(row2);
        this.setCol(col2);
        this.moveCounter++;
        this.setMoveCounter(moveCounter);
    }
}
