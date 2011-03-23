/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Ches;

/**
 *
 * @author Kaan
 */
public class Knight extends Piece {
	
    public Knight (String colour, int r, int c)
    {
        super.setRow(r);
        super.setCol(c);
        super.setValue(30);
        super.setRepresentation('N');
        super.setColor(colour);
    }

    @Override
    public int getDirection (int row2, int col2) {
         int direction = 0;
	
	//North
	if (super.gRow()-row2==2 & col2-super.gCol()==1)
            direction = 0;
	//North east
	else if (super.gRow()-row2==1 & col2-super.gCol()==2)
            direction = 1;
	//East
        else if (row2-super.gRow()==1 & col2-super.gCol()==2)
            direction = 2;
	//South east		
	else if (row2-super.gRow()==2 & col2-super.gCol()==1)
            direction = 3;	
	//South
	else if (row2-super.gRow()==2 & super.gCol()-col2==1)
            direction = 4;
	//South west
	else if (row2-super.gRow()==1 & super.gCol()-col2==2)
            direction = 5;
        //West
	else if (super.gRow()-row2==1 & super.gCol()-col2==2)
            direction = 6;
        //North west
        else if (super.gRow()-row2==2 & super.gCol()-col2==1)
            direction = 7;
        else {
            direction = -1;
        }
            	
        return direction;
    }
    
    public boolean canMove(int row2, int col2)  {
        if ((row2==super.gRow()) && (col2==super.gCol())){
            return false;
        }
        else if (((Math.abs(super.gRow()-row2)==2)&&(Math.abs(super.gCol()-col2)==1))||((Math.abs(super.gRow()-row2)==1)&&(Math.abs(super.gCol()-col2)==2))){
            return true;
        }
        else{
		return false;
        }
    }
}
