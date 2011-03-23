
package Ches;

public class Pawn extends Piece {
	
    public int startingRow;
    public int enPassantAble;

    public Pawn (String colour, int r, int c) {
       
        super.setRow(r);
        super.setCol(c);
        super.setValue(10);
        super.setRepresentation('P');
        super.setColor(colour);
        if (this.getColor() == "WHITE") {
            this.startingRow = 6;
        }
        else if (this.getColor() == "BLACK") {
            this.startingRow = 1;
        }
        else {
            this.startingRow = r;
        }
        this.enPassantAble = 0;
        
    }
    
    public boolean canMove (int row2, int col2) {
    	if ((row2==super.gRow()) && (col2==super.gCol())){
            return false;
	}
        
    	//two step forward by pawn at initial start
        else if(Math.abs(super.gRow()-row2)==2 && super.gCol()==col2 && super.getMoveCounter()<=0){
            return true;
        }
        
        //regular move up by any pawn
        else if (((this.startingRow==1)&&((row2-super.gRow())==1)&&(super.gCol()==col2))||((this.startingRow==6)&&((super.gRow()-row2)==1)&&(super.gCol()==col2))) {
            return true;
	}
		
	//attacking move by a pawn (doesn't check the enemy location if enemy exists or on whose team)
	else if((((row2-super.gRow())==1)&&(Math.abs(super.gCol()-col2)==1)&&this.startingRow==1)||(((super.gRow()-row2)==1)&&(Math.abs(super.gCol()-col2)==1)&&this.startingRow==6)){
            return true;
	}
	else {
            return false;
	}
    }
}
