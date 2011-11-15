package Ches;

import java.util.LinkedList;

public class AIEngine {
	
    private Piece[][] board;	//actual game board
    private Piece[][][] AIBoard;//layers for keeping track of scores
    private int gameCounter;
    private King WKing;        //"counter" for White King
    private King BKing;        //"counter" for Black King

    //This is the previous piece that got erased when we were updating the board
    private Piece prevP2;

    LinkedList<Move> moves;
    public Move bestMove;
    public int bestScore = 0;
    private int position = 0; //0 if black is on top, else 1

    //Global pieces for taking back a board update
    Piece p1, p2;   //Used for isCreatingCheck and isStoppingCheck
    
    public AIEngine () {
        board = new Piece[8][8];
        initializeBoard();  //actual game board gets updated by getBoard
        if (board[0][4].getColor() == "BLACK") {
            position = 0;
        }
        else {
            position = 1;
        }
        moves = new LinkedList<Move>();
        //this.generateAllMoves("WHITE");
        //System.out.println("Moves:");
        //System.out.println("Move (1,0)" +board[1][0].getRepresentation()+ " can move to: " +
                //moves.get(0).rowtwo + " " + moves.get(0).coltwo + " with score:" + moves.get(0).score);
    }
    
    //Creates the given piece on the local board for AI (not on AIBoard)
    public Piece createPiece(Piece p) {
        Piece p2 = null;
        
        if (p.getColor() == "WHITE"){
            switch (p.getRepresentation()){
                case 'P':
                    p2 = new Pawn("WHITE", p.gRow(), p.gCol());
                    ((Pawn)p2).enPassantAble = ((Pawn)p).enPassantAble;
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Pawn("WHITE", p.gRow(), p.gCol());
                    ((Pawn)board[p.gRow()][p.gCol()]).enPassantAble = ((Pawn)p).enPassantAble;
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
                case 'T':
                    p2 = new Tower("WHITE", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Tower("WHITE", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
                case 'N':
                    p2 = new Knight("WHITE", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Knight("WHITE", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
                case 'B':
                    p2 = new Bishop("WHITE", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Bishop("WHITE", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
                case 'Q':
                    p2 = new Queen("WHITE", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Queen("WHITE", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
                case 'K':
                    p2 = new King("WHITE", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new King("WHITE", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
            }
        }
        else if (p.getColor() == "BLACK"){
            switch (p.getRepresentation()){
                case 'P':
                    p2 = new Pawn("BLACK", p.gRow(), p.gCol());
                    ((Pawn)p2).enPassantAble = ((Pawn)p).enPassantAble;
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Pawn("BLACK", p.gRow(), p.gCol());
                    ((Pawn)board[p.gRow()][p.gCol()]).enPassantAble = ((Pawn)p).enPassantAble;
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
               case 'T':
                    p2 = new Tower("BLACK", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Tower("BLACK", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
                case 'N':
                    p2 = new Knight("BLACK", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Knight("BLACK", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
                case 'B':
                    p2 = new Bishop("BLACK", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Bishop("BLACK", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
                case 'Q':
                    p2 = new Queen("BLACK", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new Queen("BLACK", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
                case 'K':
                    p2 = new King("BLACK", p.gRow(), p.gCol());
                    p2.setMoveCounter(p.getMoveCounter());
                    board[p.gRow()][p.gCol()] = new King("BLACK", p.gRow(), p.gCol());
                    board[p.gRow()][p.gCol()].setMoveCounter(p.getMoveCounter());
                    break;
            }
        }
        else {
            p2 = new Dull("NOCOLOR", p.gRow(), p.gCol(), ' ');
            board[p.gRow()][p.gCol()] = new Dull("NOCOLOR", p.gRow(), p.gCol(), ' ');
        }
        return p2;
    }
    
    //Puts the pieces on the board
    public void initializeBoard() {
        board[0][0] = new Tower("BLACK",0,0);
        board[0][1] = new Knight("BLACK",0,1);
        board[0][2] = new Bishop("BLACK",0,2);
	board[0][3] = new Queen("BLACK",0,3);
	board[0][4] = new King("BLACK",0,4);
	board[0][5] = new Bishop("BLACK",0,5);
	board[0][6] = new Knight("BLACK",0,6);
	board[0][7] = new Tower("BLACK",0,7);
	for (int i=0; i<8; i++) {
            board[1][i] = new Pawn("BLACK",1,i);
            board[6][i] = new Pawn("WHITE",6,i);
	}
	for (int i=0; i<8; i++) {
            for (int j=2; j<6; j++) {
                board[j][i] = new Dull("NOCOLOR",j,i,' ');
            }
	}
	board[7][0] = new Tower("WHITE",7,0);
	board[7][1] = new Knight("WHITE",7,1);
	board[7][2] = new Bishop("WHITE",7,2);
	board[7][3] = new Queen("WHITE",7,3);
	board[7][4] = new King("WHITE",7,4);
	board[7][5] = new Bishop("WHITE",7,5);
	board[7][6] = new Knight("WHITE",7,6);
	board[7][7] = new Tower("WHITE",7,7);
        
        WKing = (King)board[7][4];
        BKing = (King)board[0][4];
        
        for (int i=0; i<8; i++)
            for (int j=0; j<8; j++)
                board[i][j].setMoveCounter(0);
    }

    //Updates the AIEngine's board	
    public void getBoard(Piece[][] bord){
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                createPiece(bord[i][j]);
            }
        }
        //this.board = bord;
    }

    //gets the counter
    public void getGameCounter(int counter) {
        this.gameCounter = counter;
    }
    
    //Updates the global king to point to the one in ChessFrame class
    public boolean getKing(King k){
        boolean result = true;
        
        if (k.getColor() == "WHITE" && board[k.gRow()][k.gCol()].getRepresentation() == 'K')
            WKing = (King)board[k.gRow()][k.gCol()];
        else if (k.getColor() == "BLACK" && board[k.gRow()][k.gCol()].getRepresentation() == 'K')
            BKing = (King)board[k.gRow()][k.gCol()];
        else{
            System.out.println("SOMETHING WRONG WITH GLOBAL KINGS");
            result = false;
        }
        return result;
    }
    
    //Prints whatever is in AIEngine's board
    public void printAIboard() {
        int k=0;
        System.out.println(" -----AI BOARD----");
        for (int i=0; i<8; i++){
            System.out.print(k);
            k++;
            for (int j=0; j<8; j++){
                System.out.print("|" + this.board[i][j].getRepresentation());
            }
            System.out.println("|");
            System.out.println(" -----------------");
        }
        System.out.println(" -0-1-2-3-4-5-6-7-");
        System.out.println("");
    }	

    //Starts the AI engine
    public Move StartAI() {
        Move m = new Move(0,0,0,0,0);
        
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                for (int k=0; k<8; k++) {
                    for (int l=0; l<8; l++) {
                        if (board[i][j].canMove(k, l) && this.isMoveValid(board[i][j], board[k][l])>0){
                            m.rowone = i;
                            m.colone = j;
                            m.rowtwo = k;
                            m.coltwo = l;
                        }
                    }
                }
            }
        }
        
        return m;
    }
    
    //Checks if the made move is putting the King under Check
    public boolean isCreatingCheck(Piece p1, Piece p2){
        boolean result = false;

        //prevP2 = this.createPiece(p2);
        updateBoard(board[p1.gRow()][p1.gCol()], board[p2.gRow()][p2.gCol()]);
        if (this.gameCounter % 2 == 1){
            if (this.isUnderAttack(WKing, WKing.getAttackingColor())>=0){
                result = true;
            }
        }
        else if (this.gameCounter % 2 == 0) {
            if (this.isUnderAttack(BKing, BKing.getAttackingColor())>=0){
                result = true;
            }
        }
        else {
            System.out.println("Something wrong with game counter in AI class: isCreatingCheck()");
        }
        strictUpdate(board[p2.gRow()][p2.gCol()], board[p1.gRow()][p1.gCol()]);
        return result;
    }
    
    //Checks if the made move is saving an existing king under check
    public boolean isStoppingCheck(Piece p1, Piece p2) {
        boolean result = true;
        
        //prevP2 = this.createPiece(p2);
        //White's turn & white king is under attack
        if (this.gameCounter % 2 == 1 && (this.isUnderAttack(WKing, WKing.getAttackingColor())>=0)){
            updateBoard(board[p1.gRow()][p1.gCol()], board[p2.gRow()][p2.gCol()]);
            if (this.isUnderAttack(WKing, WKing.getAttackingColor())>=0){
                result = false;
            }
            else{
                result = true;
            }
            strictUpdate(board[p2.gRow()][p2.gCol()], board[p1.gRow()][p1.gCol()]);
        }
        
        //Black's turn & black king is under attack
        if (this.gameCounter % 2 == 0 && (this.isUnderAttack(BKing, BKing.getAttackingColor())>=0)){
            updateBoard(board[p1.gRow()][p1.gCol()], board[p2.gRow()][p2.gCol()]);
            if (this.isUnderAttack(BKing, BKing.getAttackingColor())>=0){
                result = false;
            }
            else {
                result = true;
            }
            strictUpdate(board[p2.gRow()][p2.gCol()], board[p1.gRow()][p1.gCol()]);
        }
        return result;
    }
    
    //Updates the board based on the game rules
    public void updateBoard(Piece p1, Piece p2) { 

        int row1=0, row2=0, col1=0, col2 = 0;
        row1 = p1.gRow();
        row2 = p2.gRow();
        col1 = p1.gCol();
        col2 = p2.gCol();
       
        if (p1.canMove(row2, col2) && this.isMoveValid(p1, p2)>0 &&
            this.goAllTheWay(p1, row2, col2)){
            //this.p1 = board[row1][col1];
            //this.p2 = board[row2][col2];

            prevP2 = board[row2][col2];
            board[row2][col2] = this.createPiece(p1);
            board[row2][col2].Move(row2, col2);
            //prevP2 = board[row1][col1];
            board[row1][col1] = new Dull("NOCOLOR", row1, col1, ' ');
            board[row2][col2].selected = false;
            
            /*Piece temp = board[row1][col1];
            board[row1][col1].Move(row2, col2);
            temp.Move(row2, col2);
            prevP2 = board[row2][col2];
            //board[row1][col1] = board[row2][col2];
            board[row2][col2] = temp;
            board[row1][col1] = new Dull("NOCOLOR", row1, col1, ' ');
            //board[row1][col1] = prevP2;
            board[row2][col2].selected = false;
            //p1 = new Dull("NOCOLOR", row1, col1, ' ');*/
            if (board[row2][col2].getRepresentation() == 'K') {
                if (board[row2][col2].getColor() == "WHITE") {
                    this.WKing = ((King)board[row2][col2]);
                }
                if (board[row2][col2].getColor() == "BLACK") {
                    this.BKing = ((King)board[row2][col2]);
                }
            }

        }
    }
    
    //Does an update no matter what... only used to update back
    public void strictUpdate (Piece p1, Piece p2) {

        int row1=0, row2=0, col1=0, col2 = 0;
        row1 = p1.gRow();
        row2 = p2.gRow();
        col1 = p1.gCol();
        col2 = p2.gCol();
        
        if (p1.gRow() != p2.gRow() || p1.gCol() != p2.gCol()){

            board[row2][col2] = this.createPiece(p1);
            board[row1][col1] = prevP2;
            board[row2][col2].selected = false;
            board[row2][col2].Move(row2, col2);
            /*Piece temp = board[row1][col1];
            //board[row1][col1].Move(row2, col2);
            //board[row1][col1] = board[row2][col2];
            board[row2][col2] = temp;
            board[row1][col1] = prevP2;
            //temp.Move(row2, col2);
            temp.setMoveCounter(temp.getMoveCounter()-2);
            //temp.moveCounter--;
            //[row2][col2] = temp;
            //board[row1][col1] = this.createPiece(this.prevP2);
            //board[row1][col1] = new Dull("NOCOLOR", row1, col1, ' ');
            board[row2][col2].selected = false;*/

            if (board[row2][col2].getRepresentation() == 'K') {
                if (board[row2][col2].getColor() == "WHITE") {
                    this.WKing = ((King)board[row2][col2]);
                }
                if (board[row2][col2].getColor() == "BLACK") {
                    this.BKing = ((King)board[row2][col2]);
                }
            }
        }
    }
    
    //Checks if the 2nd spot is occupied or can be attacked
    //This actually specifically checks for capturing moves
    public int isMoveValid(Piece p1, Piece p2) {
        //Pawns cannot capture straight
        if (p1.getRepresentation()=='P' && p2.getColor() != "NOCOLOR" && p1.getColor() != p2.getColor() && p1.gCol() == p2.gCol()){
            return 0;
        }
        //Pawn cannot move diagonal without capturing
        else if ((p1.getRepresentation()=='P') && (p2.getColor().equals("NOCOLOR")) && (p1.gCol() != p2.gCol())) {

            //En passant move by pawn (left)
            if (p1.gCol() > 0 && p1.gCol() <= 7 && board[p1.gRow()][p1.gCol()-1].getRepresentation()=='P' && board[p1.gRow()][p1.gCol()-1].getAttackingColor() == p1.getColor()
                    && (p2.gCol() < p1.gCol()) && ((Pawn)board[p1.gRow()][p1.gCol()-1]).enPassantAble == this.gameCounter-1){
                return 3;
            }
            //En passant move by pawn (right)
            else if (p1.gCol() >= 0 && p1.gCol() < 7 && board[p1.gRow()][p1.gCol()+1].getRepresentation()=='P' && board[p1.gRow()][p1.gCol()+1].getAttackingColor() == p1.getColor()
                    && (p2.gCol() > p1.gCol()) && ((Pawn)board[p1.gRow()][p1.gCol()+1]).enPassantAble == this.gameCounter-1){
                return 4;
            }
            //Pawn cannot move diagonal without capturing
            else {
                return 0;
            }
        }
        //And cannot move diagonal without capturing
        else if ((p1.getRepresentation()=='P') && (p2.getColor() == "NOCOLOR") && (p1.gCol() != p2.gCol())){
            return 0;
        }
        //If this is a castling move, this is used to check while drawing the green paths for castling
        //If the king can castle or not
        else if (p1.getRepresentation() == 'K' && ((King)p1).getCastling() == true && Math.abs(p2.gCol()-p1.gCol())==2) {
            //get direction 2=east, 6=west
            int direction = p1.getDirection(p2.gRow(), p2.gCol());
            int isKingUnderAttack = this.isUnderAttack(p1, p1.getAttackingColor());
            int isKingUnderAttack1;
            int isKingUnderAttack2;

            if (direction == 2 && p1.getMoveCounter() == 0 && this.board[p2.gRow()][p2.gCol()+1].getMoveCounter() == 0
                    && isKingUnderAttack < 0) {
                isKingUnderAttack1 = this.isUnderAttack(board[p1.gRow()][p1.gCol()+1], p1.getAttackingColor());
                isKingUnderAttack2 = this.isUnderAttack(board[p1.gRow()][p1.gCol()+2], p1.getAttackingColor());
                if (isKingUnderAttack1 < 0 && isKingUnderAttack2 < 0) {
                    return 1;
                }
                 else {
                    return 0;
                 }
            }
            else if (direction == 6 && p1.getMoveCounter() == 0 && this.board[p2.gRow()][p2.gCol()-2].getMoveCounter() == 0
                    && isKingUnderAttack < 0) {
                isKingUnderAttack1 = this.isUnderAttack(board[p1.gRow()][p1.gCol()-1], p1.getAttackingColor());
                isKingUnderAttack2 = this.isUnderAttack(board[p1.gRow()][p1.gCol()-2], p1.getAttackingColor());
                if (isKingUnderAttack1 < 0 && isKingUnderAttack2 < 0) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
             else {
                return 0;
             }

        }

        //This is where the capture occurs and the function returns 2!!!
        else if (p2.getColor() != p1.getColor()){
            if (p2.getColor()!="NOCOLOR"){
                    return 2;
            }
            return 1;
        }
        else {
            return 0;
        }
    }

    //Returns the score of a given move
    public int evaluate (int rowtwo, int coltwo) {
        int score = board[rowtwo][coltwo].getValue();
        
        return score;
    }
  
    //Checks if there is an enemy attack in the given direction for the given piece
    //attackingColor is the color which the piece is under attack by
    public int facingEnemy (int direction, Piece p, String attackingColor) {
        int i = p.gRow();
        int j = p.gCol();

        switch (direction) {

            case 0:	//Check north
                //Check for a knight attack
                if (i-2>=0 && j+1<=7 && board[i-2][j+1].getRepresentation()=='N' && board[i-2][j+1].getColor() == attackingColor){
                    return 0;
                }
                while (i>0){
                    i--;
                    if (board[i][j].getColor() == p.getColor() && p.getColor() != "NOCOLOR"){
                        return -1;
                    }
                    else if (board[i][j].getColor() == "NOCOLOR"){
                        continue;
                    }
                    else if (board[i][j].getColor() != attackingColor) {
                        return -1;
                    }
                    else if (board[i][j].getColor() == attackingColor){
                        if (board[i][j].getRepresentation()=='K' && board[i][j].getDistance(p)==1.00){
                            return 0;
                        }
                        else if (board[i][j].getRepresentation()=='T' || board[i][j].getRepresentation()=='Q'){
                            return 0;
                        }
                        else{
                            return -1;
                        }
                    }
                    else {
                        continue;
                    }
                }
                return -1;
            case 1: //Check north east
                //Check for a knight attack
                if (i-1>=0 && j+2<=7 && board[i-1][j+2].getRepresentation()=='N' && board[i-1][j+2].getColor()==attackingColor)
                    return 1;
                while (i>0 & j<7)	{
                    i--;
                    j++;
                    if (board[i][j].getColor() == p.getColor() && p.getColor() != "NOCOLOR")
                        return -1;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != attackingColor) {
                        return -1;
                    }
                    else if (board[i][j].getColor() == attackingColor){
                        if (board[i][j].getRepresentation()=='P' && ((this.position==0 && board[i][j].getColor() == "BLACK") ||
                                (this.position == 1 && board[i][j].getColor() == "WHITE")) && board[i][j].getDistance(p)==1.00)
                            return 1;    
                        else if (board[i][j].getRepresentation()=='K' && board[i][j].getDistance(p)==1.00)
                            return 1;
                        else if (board[i][j].getRepresentation()=='B' || board[i][j].getRepresentation()=='Q')
                            return 1;
                        else
                            return -1;
                    }	
                    else {
                        continue;
                    }
                }
                return -1;
            case 2: //Check east
                //Check for a knight attack
                if (i+1<=7 && j+2<=7 && board[i+1][j+2].getRepresentation()=='N' && board[i+1][j+2].getColor()==attackingColor)
                    return 2;
                while (j<7){
                    j++;
                    if (board[i][j].getColor() == p.getColor() && p.getColor() != "NOCOLOR")
                        return -1;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                   else if (board[i][j].getColor() != attackingColor) {
                        return -1;
                    }
                    else if (board[i][j].getColor() == attackingColor){
                        if (board[i][j].getRepresentation()=='K' && board[i][j].getDistance(p)==1.00)
                            return 2;
                        else if (board[i][j].getRepresentation()=='T' || board[i][j].getRepresentation()=='Q')
                            return 2;
                        else
                            return -1;
                    }
                    else {
                        continue;
                    }
                }
                return -1;
            case 3: //Check south east
                //Check for a knight attack
                if (i+2<=7 && j+1<=7 && board[i+2][j+1].getRepresentation()=='N' && board[i+2][j+1].getColor()==attackingColor)
                    return 3;
                while (i<7 && j<7){
                    i++;
                    j++;
                    if (board[i][j].getColor() == p.getColor() && p.getColor() != "NOCOLOR")
                        return -1;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != attackingColor) {
                        return -1;
                    }
                    else if (board[i][j].getColor() == attackingColor){
                        if (board[i][j].getRepresentation()=='P' && ((this.position==0 && board[i][j].getColor() == "WHITE") ||
                                (this.position == 1 && board[i][j].getColor() == "BLACK")) && board[i][j].getDistance(p)==1.00)
                            return 3;    
                        else if (board[i][j].getRepresentation()=='K' && board[i][j].getDistance(p)==1.00)
                            return 3;
                        else if (board[i][j].getRepresentation()=='B' || board[i][j].getRepresentation()=='Q')
                            return 3;
                        else
                            return -1;
                    }
                    else {
                        continue;
                    }
                }
                return -1;
            case 4: //Check south
                //Check for a knight attack
                if (i+2<=7 && j-1>=0 && board[i+2][j-1].getRepresentation()=='N' && board[i+2][j-1].getColor()==attackingColor)
                    return 4;
                while (i<7){
                    i++;
                    if (board[i][j].getColor() == p.getColor() && p.getColor() != "NOCOLOR")
                        return -1;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != attackingColor) {
                        return -1;
                    }
                    else if (board[i][j].getColor() == attackingColor)
                        if (board[i][j].getRepresentation()=='K' && board[i][j].getDistance(p)==1.00)
                            return 4;
                        else if (board[i][j].getRepresentation()=='T' || board[i][j].getRepresentation()=='Q')
                            return 4;
                        else
                            return -1;	
                    else {
                        continue;
                    }
                }
                return -1;
            case 5: //Check south west
                //Check for a knight attack
                if (i+1<=7 && j-2>=0 && board[i+1][j-2].getRepresentation()=='N' && board[i+1][j-2].getColor()==attackingColor)
                    return 5;
                while (i<7 && j>0){
                    i++;
                    j--;
                    if (board[i][j].getColor() == p.getColor() && p.getColor() != "NOCOLOR"){
                        return -1;
                    }
                    else if (board[i][j].getColor() == "NOCOLOR"){
                        continue;
                    }
                    else if (board[i][j].getColor() != attackingColor) {
                        return -1;
                    }
                    else if (board[i][j].getColor() == attackingColor){
                        if (board[i][j].getRepresentation()=='P' && ((this.position==0 && board[i][j].getColor() == "WHITE") ||
                                (this.position == 1 && board[i][j].getColor() == "BLACK")) && board[i][j].getDistance(p)==1.00){
                            return 5;
                        }
                        else if (board[i][j].getRepresentation()=='K' && board[i][j].getDistance(p)==1.00){
                            return 5;
                        }
                        else if (board[i][j].getRepresentation()=='B' || board[i][j].getRepresentation()=='Q'){
                            return 5;
                        }
                        else{
                            return -1;
                        }
                    }
                    else {
                        continue;
                    }
                }
                return -1;
            case 6: //Check west
                //Check for a knight attack
                if (i-1>=0 && j-2>=0 && board[i-1][j-2].getRepresentation()=='N' && board[i-1][j-2].getColor()==attackingColor)
                    return 6;
                while (j>0){
                    j--;
                    if (board[i][j].getColor() == p.getColor() && p.getColor() != "NOCOLOR")
                        return -1;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != attackingColor) {
                        return -1;
                    }
                    else if (board[i][j].getColor() == attackingColor)
                        if (board[i][j].getRepresentation()=='K' && board[i][j].getDistance(p)==1.00)
                            return 6;
                        else if (board[i][j].getRepresentation()=='T' || board[i][j].getRepresentation()=='Q')
                            return 6;
                        else
                            return -1;
                    else {
                        continue;
                    }
                }
                return -1;
            case 7: //Check north west
                //Check for a knight attack
                if (i-2>=0 && j-1>=0 && board[i-2][j-1].getRepresentation()=='N' && board[i-2][j-1].getColor()==attackingColor)
                    return 7;
                while (j>0 && i>0){
                    j--;
                    i--;
                    if (board[i][j].getColor() == p.getColor() && p.getColor() != "NOCOLOR")
                        return -1;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != attackingColor) {
                        return -1;
                    }
                    else if (board[i][j].getColor() == attackingColor){
                        if (board[i][j].getRepresentation()=='P' && ((this.position==0 && board[i][j].getColor() == "BLACK") ||
                                (this.position == 1 && board[i][j].getColor() == "WHITE")) && board[i][j].getDistance(p)==1.00)
                            return 7;    
                        else if (board[i][j].getRepresentation()=='K' && board[i][j].getDistance(p)==1.00)
                            return 7;
                        else if (board[i][j].getRepresentation()=='B' || board[i][j].getRepresentation()=='Q')
                            return 7;
                        else
                            return -1;
                    }
                    else {
                        continue;
                    }
                } 
                return -1;
            default:
                return 8;
        }     
    }    

    //Checks if the piece can run all the way to 2nd location
    public boolean goAllTheWay (Piece p1, int row2, int col2){
		
        int direction = 0;
        int i = p1.gRow();
        int j = p1.gCol();

        if (p1.getRepresentation() == 'N')
                return true;

        direction = p1.getDirection(row2, col2);

        switch (direction) {
            case 0:	//Check north
                while (i!=row2 && i>=0){
                    i--;
                    if (board[i][j].getColor() == p1.getColor())
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != p1.getColor() && i!=row2)
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 1: //Check north east
                while ((i!=row2 & j!=col2) && i>=0 && j<8)	{
                    i--;
                    j++;
                    if (board[i][j].getColor() == p1.getColor())
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != p1.getColor() && (i!=row2 & j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 2: //Check east
                while (j!=col2 && j<8){
                    j++;
                    if (board[i][j].getColor() == p1.getColor())
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != p1.getColor() && j!=col2)
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 3: //Check south east
                while ((i!=row2 & j!=col2) && i<8 && j<8){
                    i++;
                    j++;
                    if (board[i][j].getColor() == p1.getColor())
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != p1.getColor() && (i!=row2 & j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 4: //Check south
                while (i!=row2 && i<8){
                    i++;
                    if (board[i][j].getColor() == p1.getColor())
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != p1.getColor() && i!=row2)
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 5: //Check south west
                while ((i!=row2 & j!=col2) && i<8 && j>=0){
                    i++;
                    j--;
                    if (board[i][j].getColor() == p1.getColor())
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != p1.getColor() && (i!=row2 & j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 6: //Check west
                while (j!=col2 && j>=0){
                    j--;
                    if (board[i][j].getColor() == p1.getColor())
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != p1.getColor() && (j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 7: //Check north west
                while ((i!=row2 & j!=col2) && j>=0 && i>=0){
                    j--;
                    i--;
                    if (board[i][j].getColor() == p1.getColor())
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != p1.getColor() && (i!=row2 & j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            default:
                return true;
            }
    }

    //Checks if there is nobody on the way of the given piece until row2,col2
    //Used for castling bug
    public boolean noOneOnTheWay (Piece p1, int row2, int col2){
          
        int direction = 0;
        int i = p1.gRow();
        int j = p1.gCol();

        if (p1.getRepresentation() == 'N')
                return true;

        direction = p1.getDirection(row2, col2);

        switch (direction) {
            case 0:	//Check north
                while (i!=row2 && i>=0){
                    i--;
                    if (board[i][j].getColor() == "NOCOLOR")
                        return true;
                    else if (board[i][j].getColor() != "NOCOLOR" && i!=row2)
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else {
                        continue;
                    }
                }
                return true;
            case 1: //Check north east
                while ((i!=row2 & j!=col2) && i>=0 && j<8)	{
                    i--;
                    j++;
                    if (board[i][j].getColor() != "NOCOLOR")
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != "NOCOLOR" && (i!=row2 & j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 2: //Check east
                while (j!=col2 && j<8){
                    j++;
                    if (board[i][j].getColor() != "NOCOLOR")
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != "NOCOLOR" && j!=col2)
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 3: //Check south east
                while ((i!=row2 & j!=col2) && i<8 && j<8){
                    i++;
                    j++;
                    if (board[i][j].getColor() != "NOCOLOR")
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != "NOCOLOR" && (i!=row2 & j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
            case 4: //Check south
                while (i!=row2 && i<8){
                    i++;
                    if (board[i][j].getColor() != "NOCOLOR")
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != "NOCOLOR" && i!=row2)
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 5: //Check south west
                while ((i!=row2 & j!=col2) && i<8 && j>=0){
                    i++;
                    j--;
                    if (board[i][j].getColor() != "NOCOLOR")
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != "NOCOLOR" && (i!=row2 & j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 6: //Check west
                while (j!=col2 && j>=0){
                    j--;
                    if (board[i][j].getColor() != "NOCOLOR")
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != "NOCOLOR" && (j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            case 7: //Check north west
                while ((i!=row2 & j!=col2) && j>=0 && i>=0){
                    j--;
                    i--;
                    if (board[i][j].getColor() != "NOCOLOR")
                        return false;
                    else if (board[i][j].getColor() == "NOCOLOR")
                        continue;
                    else if (board[i][j].getColor() != "NOCOLOR" && (i!=row2 & j!=col2))
                        return false;	
                    else {
                        continue;
                    }
                }
                return true;
            default:
                return true;
            }
    }
    
    //Cheks if the given piece is under attack by the given AttackingColor
    public int isUnderAttack (Piece p, String AttackingColor){    
        int underAttack = -1;
        
        for (int i=0; i<8; i++){
            underAttack = facingEnemy(i, p, AttackingColor);
            if (underAttack >= 0)
                break;
        }
        return underAttack;
    }

    //returns the number of moves in our moves list
    public int getNumberOfMoves() {
        return moves.size();
    }

    //Generates valid moves for the given piece
    public void generateAllMoves(String color) {
        
        //A blank move object
        Move currentMove = new Move(0,0,0,0,0);

        //Empties the move list
        moves.clear();
        
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (board[i][j].getColor() == color) {
                    for (int k=0; k<8; k++) {
                        for (int l=0; l<8; l++) {
                            if (board[i][j].canMove(k,l) && (this.isMoveValid(board[i][j], board[k][l])> 0) &&
                                this.goAllTheWay(board[i][j], k, l)) {
                                boolean isCreatingCheck = this.isCreatingCheck(board[i][j], board[k][l]);
                                //this.getBoard(board);
                                boolean isStoppingCheck = this.isStoppingCheck(board[i][j], board[k][l]);
                                //this.getBoard(board);
                                if (!isCreatingCheck && isStoppingCheck) {
                                    currentMove.rowone = i;
                                    currentMove.rowtwo = k;
                                    currentMove.colone = j;
                                    currentMove.coltwo = l;
                                    currentMove.score = this.evaluate(i,j);
                                    moves.add(currentMove);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}