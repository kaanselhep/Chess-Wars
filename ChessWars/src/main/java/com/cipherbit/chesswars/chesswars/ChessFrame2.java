/*
 * ChessFrame.java
 * Created on 21-Feb-2009, 5:19:47 PM
 *
 * @author Kaan
 */

package com.cipherbit.chesswars.chesswars;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.awt.Font;
import java.awt.Graphics;

import java.awt.Point;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChessFrame2 extends JFrame implements KeyListener, MouseMotionListener,
 MouseListener, MouseWheelListener {

    private About about;
    private Options options;
    
    private int mouseRow = 9;
    private int mouseCol = 9;
    private int rowone = 0;		//row one
    private int rowtwo = 0;		//row two
    private int colone = 0;		//column one
    private int coltwo = 0;		//column two
    private boolean Winner = false;	//if there is a winner or not
    private int position = 0;           //if black is on top, else 1
    private Promotion prom;
    private Piece[][] board = new Piece[8][8];	//board made out of piece class
    private King WKing;
    private King BKing;
    private int pieceCounter = 32;		//number of pieces on the board

    LinkedList<Move> Turn;
    LinkedList<Piece> capturedWhitePieces = new LinkedList<Piece>();
    LinkedList<Piece> capturedBlackPieces = new LinkedList<Piece>();
    private int turnCounter = 1;//Counter for keeping track of whose turn it is
    private Player p1, p2;
    private AIEngine AI;		//the AI Engine
    private String message = "Hello World";
    private Dimension dim;

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public BufferStrategy strategy;

    JMenuBar bar;
    private BufferedImage boardImage;
    private BufferedImage MoveAbleDarkTile;
    private BufferedImage MoveAbleLightTile;
    private BufferedImage DarkRedTile;
    private Image cursorImage;
    private Image icon;
    private Image negIcon;
    private JPanel panel;
    
    public ChessFrame2() {

        panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(20, 50);
        panel.setLocation(10, 550);
        panel.setOpaque(false);
       
        //Set the cursor
        options = new Options();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        cursorImage = tk.getImage("images/chesswars2.gif");
        Point cursorHotSpot = new Point(0,0);
        Cursor customCursor = tk.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
        this.setCursor(customCursor);
        
        Turn = new LinkedList<Move>();
        icon = Toolkit.getDefaultToolkit().getImage("images/nuke.jpg");
        negIcon = Toolkit.getDefaultToolkit().getImage("images/nukeNeg.jpg");
        this.setIconImage(icon);
        this.setLocation(((dim.width/2)-(530/2)), (dim.height/2)-(570/2));
        
        this.setTitle("Chess Wars");
        this.setSize(680, 650);
        dim.setSize(680, 650);
        this.setMinimumSize(dim);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.bar = new JMenuBar();      //Menu bar

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        bar.add(fileMenu); 
        bar.add (editMenu);
        bar.add(helpMenu);
        
        JMenuItem NewGame = new JMenuItem("New");
        JMenuItem Load = new JMenuItem("Load");
        JMenuItem Save = new JMenuItem("Save");
        JMenuItem Exit = new JMenuItem("Exit");
        JMenuItem Options = new JMenuItem("Options");
        JMenuItem flipBoard = new JMenuItem("Flip Board");
        JMenuItem About = new JMenuItem("About");

        //Set escape to exit the game
        Exit.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(NewGame);
        fileMenu.add(Load);
        fileMenu.add(Save);
        fileMenu.add(Exit);
        editMenu.add(Options);
        editMenu.add(flipBoard);
        helpMenu.add(About);

        NewGame.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    newGame();
                }
            }
        );   
        About.addActionListener(
            new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    about = new About();
                    about.setVisible(true);
                }
            }
        );
        Options.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    options.setVisible(true);
                }
            }
        );
        flipBoard.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    flipBoard();
                }
            }
        );
        Exit.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exitForm();
                }
            }
        );
        Save.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try { 
                        saveGame();
                    }
                    catch (IOException ioe) {ioe.toString();}
                }
            }
        );
        Load.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        loadGame();
                    }
                    catch (IOException ioe) {ioe.toString();}
                }
            }
        );
        
        p1 = new Player(16, "WHITE");
        p2 = new Player(16, "BLACK");

        AI = new AIEngine();
        this.message = "";
        emptyBoard();
        initializeBoard();

        if (this.board[0][4].getColor() == "BLACK") {
            position = 0;
        }
        else {
            position = 1;
        }
        
        AI.getBoard(this.board);
        //this.setFont(Font.);
        this.setFont(new Font("Plain", Font.BOLD, 18));
        
        try {
            boardImage = ImageIO.read(new File("images/myboard2.png"));
            MoveAbleDarkTile = ImageIO.read(new File("images/MoveAbleDarkTile.png"));
            MoveAbleLightTile = ImageIO.read(new File("images/MoveAbleLightTile.png"));
            DarkRedTile = ImageIO.read(new File("images/DarkRedTile.png"));
            for (int i=0; i<8; i++){
                for (int j=0; j<8; j++){
                    if (board[i][j].getRepresentation() == ' '){}
                    else {
                        if (board[i][j].getRepresentation() == 'K') {
                            ((King)board[i][j]).checkImage = ImageIO.read(new File("images/"+board[i][j].getColor()+board[i][j].getRepresentation()+"CHECK.PNG"));
                        }
                        board[i][j].image = ImageIO.read(new File("images/"+board[i][j].getColor()+board[i][j].getRepresentation()+".PNG"));
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
        }
        //initComponents();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);

        this.setJMenuBar(bar);
        this.setVisible(true);

        this.createBufferStrategy(2);
        this.pack();
        strategy = getBufferStrategy();
        
        play();
    }

    private void chatHistoryCaretUpdate(javax.swing.event.CaretEvent evt) {
        System.out.println("Yes I can capture text change!");

    }

    private void newGame() {
        this.emptyBoard();
        this.initializeBoard();
        capturedWhitePieces.clear();
        capturedBlackPieces.clear();
        this.turnCounter = 1;            
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (board[i][j].getRepresentation() == ' '){}
                else
                    try{
                        if (board[i][j].getRepresentation() == 'K') {
                            ((King)board[i][j]).checkImage = ImageIO.read(new File("images/"+board[i][j].getColor()+board[i][j].getRepresentation()+"CHECK.PNG"));
                        }
                        board[i][j].image = ImageIO.read(new File("images/"+board[i][j].getColor()+board[i][j].getRepresentation()+".PNG"));
                    }catch(IOException e) {e.toString();}
            }
        }
        AI.initializeBoard();
        this.repaint();
    }

    //Flips the chess board upside down
    private void flipBoard() {
        for (int i=0; i<(board.length/2); i++) {
            for (int j=0; j<board[i].length; j++) {
                char[] p1 = new char[5];
                char[] p2 = new char[5];
                
                p1[0] = board[i][j].getColor().charAt(0);
                p1[1] = board[i][j].getRepresentation();
                p1[2] = (char)(board.length-(board[i][j].gRow()));
                p1[3] = (char) board[i][j].gCol();
                p1[4] = (char) board[i][j].getMoveCounter();
                
                p2[0] = board[board.length-i-1][j].getColor().charAt(0);
                p2[1] = board[board.length-i-1][j].getRepresentation();
                p2[2] = (char) board[i][j].gRow();
                p2[3] = (char) board[i][j].gCol();
                p2[4] = (char) board[board.length-i-1][j].getMoveCounter();
                
                this.createPiece(p1);

                //board[board.length-i][j] =
            }
        }
    }

    private void exitForm () {
        System.out.println("Goodbye Chicken");
        this.setVisible(false);
        dispose();
        System.exit(0);
    }
    
    private void saveGame () throws IOException {
        
        int result = 0;
        String filename = "";
        String path = "";
        JFileChooser fc = new JFileChooser(new File(filename));
        fc.addChoosableFileFilter(new GameFilter());
        fc.setAcceptAllFileFilterUsed(false);
        
        result = fc.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION){
            File selFile = fc.getSelectedFile();
            selFile.createNewFile();
            
            if (!selFile.getName().endsWith(".csw")) {
                path = selFile.getPath();
                path += ".csw";
                selFile.renameTo(new File(path));
                selFile = new File(path);
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(selFile.getAbsoluteFile()));
            bw.write(Integer.toString(this.turnCounter));
            bw.newLine();
            bw.write(Integer.toString(this.position));
            bw.newLine();
            for (int i=0; i<8; i++){
                for (int j=0; j<8; j++){
                    bw.write(board[i][j].toString());
                }
                bw.newLine();
            }
            Iterator wPieceIterator;
            if (!this.capturedWhitePieces.isEmpty()) {
                wPieceIterator = this.capturedWhitePieces.iterator();
                 //Write the captured white pieces
                while (wPieceIterator.hasNext()) {
                    bw.write(((Piece)wPieceIterator.next()).toString());
                    bw.newLine();
                }
            }
            Iterator bPieceIterator;
            if (!this.capturedBlackPieces.isEmpty()) {
                bPieceIterator = this.capturedBlackPieces.iterator();
                //Write the captured black pieces
                while (bPieceIterator.hasNext()) {
                    bw.write(((Piece)bPieceIterator.next()).toString());
                    bw.newLine();
                }
            }
            bw.close();
        }
        else {
            return;
        }
    }
    
    private Piece createPiece (char piece[]) {
        String color = "";

        Piece p;
        char type;
        int row, col, count;
        
        if (piece[0] == 'B')
            color = "BLACK";
        else if(piece[0] == 'N')
            color = "NOCOLOR";
        else
            color = "WHITE";
        
        type = piece[1];
        row = Character.getNumericValue(piece[2]);
        col = Character.getNumericValue(piece[3]);
        count = Character.getNumericValue(piece[4]);
        
        switch(type) {
            case 'T': 
                this.board[row][col] = null;
                this.board[row][col] = new Tower(color, row, col);
                this.board[row][col].getImage();
                this.board[row][col].setMoveCounter(count);
                break;
            case 'N':
                this.board[row][col] = null;
                this.board[row][col] = new Knight(color, row, col);
                this.board[row][col].getImage();
                this.board[row][col].setMoveCounter(count);
                break;
            case 'B':
                this.board[row][col] = null;
                this.board[row][col] = new Bishop(color, row, col);
                this.board[row][col].getImage();
                this.board[row][col].setMoveCounter(count);
                break;
            case 'Q':
                this.board[row][col] = null;
                this.board[row][col] = new Queen(color, row, col);
                this.board[row][col].getImage();
                this.board[row][col].setMoveCounter(count);
                break;
            case 'K':
                this.board[row][col] = null;
                this.board[row][col] = new King(color, row, col);
                this.board[row][col].getImage();
                ((King)this.board[row][col]).getCheckImage();
                this.board[row][col].setMoveCounter(count);
                if (this.board[row][col].getColor() == "WHITE")
                    WKing = (King)board[row][col];
                else if (this.board[row][col].getColor() == "BLACK")
                    BKing = (King)board[row][col];
                else
                    System.out.println("Global kings are not created properly");
                break;
            case 'P':
                this.board[row][col] = null;
                this.board[row][col] = new Pawn(color, row, col);
                this.board[row][col].getImage();
                this.board[row][col].setMoveCounter(count);
                if (this.position == 0){
                    if (board[row][col].getColor() == "BLACK")
                        ((Pawn)board[row][col]).startingRow = 1;
                    else
                        ((Pawn)board[row][col]).startingRow = 6;
                }
                else {
                    if (board[row][col].getColor() == "BLACK")
                        ((Pawn)board[row][col]).startingRow = 6;
                    else
                        ((Pawn)board[row][col]).startingRow = 1;
                }
                break;
            case ' ':
                this.board[row][col] = null;
                this.board[row][col] = new Dull(color, row, col, ' ');
                this.board[row][col].getImage();
                this.board[row][col].setMoveCounter(count);
                break;
            default:
                System.out.println("Wrong character read");
                break;
        }
        p = this.board[row][col];
        return p;
    }
    
    private void loadGame () throws IOException {
        int result = 0;
        String filename = "";
        String line = "";
        char piece[];
        Piece p;
        
        JFileChooser fc = new JFileChooser(new File(filename));
        fc.addChoosableFileFilter(new GameFilter());
        fc.setAcceptAllFileFilterUsed(false);
        
        result = fc.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION){
            File selFile = fc.getSelectedFile();
            BufferedReader br = new BufferedReader(new FileReader(selFile.getAbsoluteFile()));
            this.emptyBoard();
            //Read the turn counter
            line = br.readLine();
            this.turnCounter = Integer.parseInt(line);
            line = br.readLine();
            this.position = Integer.parseInt(line);
            for (int i=0; i<8; i++){
                line = br.readLine();
                for (int j=0; j<40; j=j+5){
                    piece = line.substring(j, j+5).toCharArray();
                    createPiece(piece);
                }         
            }
            do {
                line = br.readLine();
                if (line != null) {
                    piece = line.substring(0, 5).toCharArray();
                    p = createPiece(piece);
                    if (p.getColor() == "WHITE") {
                        this.capturedWhitePieces.add(p);
                    }
                    else if (p.getColor() == "BLACK") {
                        this.capturedBlackPieces.add(p);
                    }
                }
            } while (line != null);
        }
        else {
            return;
        }
        this.repaint();        
    }
    
    @Override
    public synchronized void paint (Graphics g) {

        boolean isCreatingCheck = true;
        boolean isStoppingCheck = false;

        g = strategy.getDrawGraphics();
        super.paintComponents(g);
        
        String attackingColor;
        panel.setVisible(false);
        panel.repaint();
        g.drawImage(boardImage, 10,50,this);
        attackingColor = board[rowone][colone].getAttackingColor();

        //If we have a piece selected, re-draw the board with possible places
        //to go
        if (rowone < 9 && colone < 9 && board[rowone][colone].selected==true){
            board[rowone][colone].playSound();
            for (int k=0; k<board.length; k++){
                for (int l=0; l<board[k].length; l++){
                    if (board[rowone][colone].canMove(k,l)) {
                        int validMove = AI.isMoveValid(board[rowone][colone], board[k][l]);
                        boolean goAllTheWay = AI.goAllTheWay(board[rowone][colone],k,l);

                        //En passant capture
                        if (validMove == 3 && goAllTheWay && board[rowone][colone-1].getColor() == board[rowone][colone].getAttackingColor()) {
                            g.drawImage(DarkRedTile, (l*64)+10, (k*64)+50, this);
                        }
                        else if (validMove == 4 && goAllTheWay && board[rowone][colone+1].getColor() == board[rowone][colone].getAttackingColor()) {
                            g.drawImage(DarkRedTile, (l*64)+10, (k*64)+50, this);
                        }
                        else if(validMove > 0 && goAllTheWay == true) {
                            isCreatingCheck = AI.isCreatingCheck(board[rowone][colone],board[k][l]);

                            AI.getKing(WKing);
                            AI.getKing(BKing);
                            AI.getBoard(board);

                            isStoppingCheck = AI.isStoppingCheck(board[rowone][colone],board[k][l]);

                            AI.getKing(WKing);
                            AI.getKing(BKing);
                            AI.getBoard(board);

                            if (!isCreatingCheck && isStoppingCheck) {
                                if(board[k][l].getColor() == attackingColor) {
                                    g.drawImage(DarkRedTile, (l*64)+10, (k*64)+50, this);
                                }
                                else if (Math.abs(l-k)%2==0 && board[k][l].getColor()=="NOCOLOR") {
                                    g.drawImage(MoveAbleDarkTile, (l*64)+10, (k*64)+50, 64,64,this);
                                }
                                else {
                                    g.drawImage(MoveAbleLightTile, (l*64)+10,(k*64)+50, 64,64,this);
                                }
                            }
                            
                        }
                    }
                }
            }
            //if we have a king selected and it is currently in castling mode, turn castling off
            if (board[rowone][colone].getRepresentation() == 'K' && ((King)board[rowone][colone]).isCastling == true){
                ((King)board[rowone][colone]).isCastling = false;
            }
        }
        //Then just draw the pieces on the board
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (board[j][i].selected == true){
                    g.drawImage(board[j][i].image, (i*64)+6, (j*64)+42,80,80, this);
                }
                else{
                    if (board[j][i].getRepresentation() == 'K' && AI.isUnderAttack(board[j][i], board[j][i].getAttackingColor())>=0) {
                        g.drawImage(((King)board[j][i]).checkImage, (i*64)+12, (j*64)+50, 64, 64, this);
                    }
                    else {
                        g.drawImage(board[j][i].image, (i*64)+12, (j*64)+50, 64, 64, this);
                    }
                }
            }
        }
        bar.repaint();
        
        //g.drawString(this.message, WIDTH, WIDTH);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 520, 562);
        g.clearRect(0, 564, 680, 68);
        g.drawString(this.message, 10, 600);

        //Then draw the captured pieces on the side
        Iterator wPieceIterator;
        if (!this.capturedWhitePieces.isEmpty()) {
            wPieceIterator = this.capturedWhitePieces.iterator();
            int o = 0;
             //Draw the captured white pieces
            while (wPieceIterator.hasNext()) {
                if (o<5)
                    g.drawImage(((Piece)wPieceIterator.next()).image, ((o%5)*27)+515, 50, 48, 48, this);
                else if (o<10)
                    g.drawImage(((Piece)wPieceIterator.next()).image, ((o%5)*27)+515, 80, 48, 48, this);
                else
                    g.drawImage(((Piece)wPieceIterator.next()).image, ((o%5)*27)+515, 110, 48, 48, this);
                o++;
            }
        }
        Iterator bPieceIterator;
        if (!this.capturedBlackPieces.isEmpty()) {
            bPieceIterator = this.capturedBlackPieces.iterator();
            int p = 0;
            //Draw the captured black pieces
            while (bPieceIterator.hasNext()) {
                if (p<5)
                    g.drawImage(((Piece)bPieceIterator.next()).image, ((p%5)*27)+515, 460, 48, 48, this);
                else if (p<10)
                    g.drawImage(((Piece)bPieceIterator.next()).image, ((p%5)*27)+515, 490, 48, 48, this);
                else
                    g.drawImage(((Piece)bPieceIterator.next()).image, ((p%5)*27)+515, 520, 48, 48, this);
                p++;
            }
        }
        //this.chatPanel.repaint();
        strategy.show();
    }

    //Sets the global message displayed under the chess board
    public void setMessage(String message) {
        this.message = message;
        this.repaint();
    }

   //This function empties (sets Dull) our chess board
    public synchronized void emptyBoard() {
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
            	board[i][j] = null;
                board[i][j] = new Dull("NOCOLOR", i,j,' ');
            }
        }
    }

    //Prints the board with ASCII characters
    public void printBoard () {
        int k=0;
        System.out.println(" -----------------");
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

    //Puts the pieces on the board
    public void initializeBoard() {
        board[0][0] = new Tower("BLACK",0,0);
        board[0][0].setMoveCounter(0);
        board[0][1] = new Knight("BLACK",0,1);
        board[0][1].setMoveCounter(0);
        board[0][2] = new Bishop("BLACK",0,2);
	board[0][2].setMoveCounter(0);
        board[0][3] = new Queen("BLACK",0,3);
	board[0][3].setMoveCounter(0);
        board[0][4] = new King("BLACK",0,4);
	board[0][4].setMoveCounter(0);
        board[0][5] = new Bishop("BLACK",0,5);
	board[0][5].setMoveCounter(0);
        board[0][6] = new Knight("BLACK",0,6);
	board[0][6].setMoveCounter(0);
        board[0][7] = new Tower("BLACK",0,7);
	board[0][7].setMoveCounter(0);
        
        for (int i=0; i<8; i++) {
            board[1][i] = new Pawn("BLACK",1,i);
            board[1][i].setMoveCounter(0);
            board[6][i] = new Pawn("WHITE",6,i);
            board[6][i].setMoveCounter(0);
	}
        
	for (int i=0; i<8; i++) {
            for (int j=2; j<6; j++) {
                board[j][i] = new Dull("NOCOLOR",j,i,' ');
                board[j][i].setMoveCounter(0);
            }
	}
        
	board[7][0] = new Tower("WHITE",7,0);
        board[7][0].setMoveCounter(0);
	board[7][1] = new Knight("WHITE",7,1);
	board[7][1].setMoveCounter(0);
        board[7][2] = new Bishop("WHITE",7,2);
	board[7][2].setMoveCounter(0);
        board[7][3] = new Queen("WHITE",7,3);
	board[7][3].setMoveCounter(0);
        board[7][4] = new King("WHITE",7,4);
	board[7][4].setMoveCounter(0);
        board[7][5] = new Bishop("WHITE",7,5);
	board[7][5].setMoveCounter(0);
        board[7][6] = new Knight("WHITE",7,6);
	board[7][6].setMoveCounter(0);
        board[7][7] = new Tower("WHITE",7,7);
        board[7][7].setMoveCounter(0);
        
        WKing = (King)board[7][4];
        BKing = (King)board[0][4];
    }

    //Update the board no matter what piece moves or eats another
    public synchronized boolean updateBoard() {
        
        int direction = 0;
        String data = "";
        
        //If we're castling
        if (board[rowone][colone].getRepresentation() == 'K' &&
                board[rowone][colone].canMove(rowtwo, coltwo) && 
                (((King)board[rowone][colone]).isCastling==true)){
            direction = board[rowone][colone].getDirection(rowtwo, coltwo);
            
            if (direction == 2){        //east castling
                if (board[rowone][7].getMoveCounter()==0 && AI.noOneOnTheWay(board[rowone][7], rowone, 5) &&
                        AI.isUnderAttack(board[rowone][colone], board[rowone][colone].getAttackingColor())<0 &&
                        AI.isUnderAttack(board[rowone][colone+1], board[rowone][colone].getAttackingColor())<0 &&
                        AI.isUnderAttack(board[rowone][colone+2], board[rowone][colone].getAttackingColor())<0){
                    ((King)board[rowone][colone]).setCastling(false);
                    King temp = (King) board[rowone][colone];
                    Tower tow = (Tower) board[rowone][7];
                    temp.Move(rowtwo, coltwo);
                    board[rowtwo][coltwo] = temp;
                    board[rowone][colone] = new Dull("NOCOLOR", rowone, colone, ' ');
                    board[rowtwo][coltwo].selected = false;
                    tow.Move(rowtwo, coltwo-1);
                    board[rowtwo][coltwo-1] = tow;
                    board[rowone][7] = new Dull("NOCOLOR", rowone, 7, ' ');
                    
                    //Update the global kings
                    if (board[rowtwo][coltwo].getColor() == "WHITE"){
                        WKing = (King)board[rowtwo][coltwo];
                    }
                    if (board[rowtwo][coltwo].getColor() == "BLACK"){
                        BKing = (King)board[rowtwo][coltwo];
                    }
                    this.repaint();  
                    return true;
                }
                else{
                    board[rowone][colone].selected = false;
                    ((King)board[rowone][colone]).isCastling = false;
                    this.repaint();
                    return false;
                }                    
            }
            else if (direction == 6) {  //west castling
                if (board[rowone][0].getMoveCounter()==0 && AI.noOneOnTheWay(board[rowone][0], rowone, 3) &&
                        AI.isUnderAttack(board[rowone][colone],board[rowone][colone].getAttackingColor())<0 &&
                        AI.isUnderAttack(board[rowone][colone-1], board[rowone][colone].getAttackingColor())<0 &&
                        AI.isUnderAttack(board[rowone][colone-2], board[rowone][colone].getAttackingColor())<0){
                    ((King)board[rowone][colone]).setCastling(false);
                    King temp = (King)board[rowone][colone];
                    Tower tow = (Tower)board[rowone][0];
                    temp.Move(rowtwo, coltwo);
                    board[rowtwo][coltwo] = temp;
                    board[rowone][colone] = new Dull("NOCOLOR", rowone, colone, ' ');
                    board[rowtwo][coltwo].selected = false;
                    tow.Move(rowtwo, coltwo+1);
                    board[rowtwo][coltwo+1] = tow;
                    board[rowone][0] = new Dull("NOCOLOR", rowone, 0, ' ');
                    
                    //Update the global kings
                    if (board[rowtwo][coltwo].getColor() == "WHITE"){
                        WKing = (King)board[rowtwo][coltwo];
                    }
                    if (board[rowtwo][coltwo].getColor() == "BLACK"){
                        BKing = (King)board[rowtwo][coltwo];
                    }
                    this.repaint();     
                    return true;
                } 
                else{
                    board[rowone][colone].selected = false;
                    ((King)board[rowone][colone]).isCastling = false;
                    this.repaint();
                    return false;
                }
            }
            else {
                System.out.println("KING SHOULD NEVER GET IN THIS STATE");
                System.out.println("Rowone, Colone Piece: " + board[rowone][colone].getRepresentation());
                System.out.println("King isCastling: " + ((King)board[rowone][colone]).isCastling);
                //shouldn't come here
                return false;
            }
        }
        else if (board[rowone][colone].canMove(rowtwo, coltwo)) {
            int validMove = AI.isMoveValid(board[rowone][colone],board[rowtwo][coltwo]);
            boolean goAllTheWay = AI.goAllTheWay(board[rowone][colone],rowtwo,coltwo);
            boolean isCreatingCheck = true;
            boolean isStoppingCheck = false;

            if (validMove > 0 && goAllTheWay == true) {
                //AI.getBoard(board);
                isCreatingCheck = AI.isCreatingCheck(board[rowone][colone],board[rowtwo][coltwo]);
                AI.getBoard(board);
                isStoppingCheck = AI.isStoppingCheck(board[rowone][colone],board[rowtwo][coltwo]);

                AI.getBoard(board);

                if (validMove > 0 && goAllTheWay && !isCreatingCheck && isStoppingCheck) {

                    ////////////////////////
                    //En passant capture
                    if (validMove == 3 && board[rowone][colone-1].getColor() == board[rowone][colone].getAttackingColor()) {
                        if (board[rowone][colone-1].getColor() == "WHITE") {
                            this.capturedWhitePieces.add(board[rowone][colone-1]);
                        }
                        if (board[rowone][colone-1].getColor() == "BLACK") {
                            this.capturedBlackPieces.add(board[rowone][colone-1]);
                        }
                        board[rowone][colone-1] = new Dull("NOCOLOR", rowone, colone-1, ' ');

                    }
                    else if (validMove == 4 && board[rowone][colone+1].getColor() == board[rowone][colone].getAttackingColor()) {
                        if (board[rowone][colone+1].getColor() == "WHITE") {
                            this.capturedWhitePieces.add(board[rowone][colone+1]);
                        }
                        if (board[rowone][colone+1].getColor() == "BLACK") {
                            this.capturedBlackPieces.add(board[rowone][colone+1]);
                        }
                        board[rowone][colone+1] = new Dull("NOCOLOR", rowone, colone+1, ' ');
                    }
                    ///////////////////////

                    if (board[rowtwo][coltwo].getRepresentation() != ' '){
                        this.pieceCounter--;
                        if (board[rowtwo][coltwo].getColor() == "WHITE") {
                            this.capturedWhitePieces.add(board[rowtwo][coltwo]);
                        }
                        if (board[rowtwo][coltwo].getColor() == "BLACK") {
                            this.capturedBlackPieces.add(board[rowtwo][coltwo]);
                        }
                    }
                    Piece temp = board[rowone][colone];
                    temp.Move(rowtwo, coltwo);
                    board[rowtwo][coltwo] = temp;
                    board[rowone][colone] = new Dull("NOCOLOR", rowone, colone, ' ');
                    board[rowtwo][coltwo].selected = false;

                    //Update the enpassantable
                    if (board[rowtwo][coltwo].getRepresentation() == 'P' && Math.abs((rowtwo-rowone))==2){
                       ((Pawn)board[rowtwo][coltwo]).enPassantAble = this.turnCounter;
                    }
                    //Update the global kings
                    if (board[rowtwo][coltwo].getRepresentation() == 'K' && board[rowtwo][coltwo].getColor() == "WHITE"){
                        WKing = (King)board[rowtwo][coltwo];
                    }
                    if (board[rowtwo][coltwo].getRepresentation() == 'K' && board[rowtwo][coltwo].getColor() == "BLACK"){
                        BKing = (King)board[rowtwo][coltwo];
                    }

                    //If we reached the other end of the board with a pawn, promote the pawn
                    if (board[rowtwo][coltwo].getRepresentation() == 'P' && (rowtwo == 7 || rowtwo == 0)){

                        prom = new Promotion();

                        char piece[];
                        piece = new char[5];
                        for (int i=0; i<5; i++)
                            piece[i] = ' ';
                        this.setEnabled(false);

                        //If we're the server and it's a white pawn,
                        //ask for user input
                        if (((options.gameType == 1 || options.gameType == 0) && board[rowtwo][coltwo].getColor() == "WHITE")
                           ||(options.gameType == 0 || options.gameType == 2) && board[rowtwo][coltwo].getColor() == "BLACK"){

                            new Thread() {
                                public void run() {
                                    prom.setIcons(board[rowtwo][coltwo].getColor());
                                    prom.setVisible(true);
                                }
                            }.start();
                            try {
                                synchronized (this) {
                                    while (prom.shouldWait){
                                        wait(100);
                                    }
                                    prom.shouldWait = true;
                                }

                                piece[1] = prom.getPiece();
                                piece[0] = board[rowtwo][coltwo].getColor().charAt(0);
                                piece[2] = (Integer.toString(rowtwo).charAt(0));
                                piece[3] = (Integer.toString(coltwo).charAt(0));
                                piece[4] = (Integer.toString((board[rowtwo][coltwo].getMoveCounter())).charAt(0));
                                this.createPiece(piece);
                                data = Character.toString(piece[0]) + Character.toString(piece[1])
                                        + Character.toString(piece[2]) + Character.toString(piece[3])
                                        + Character.toString(piece[4]);
                                if (options.gameType == 1)
                                    options.server.sendData(data);
                                else if (options.gameType == 2)
                                    options.client.sendData(data);
                                this.setEnabled(true);
                                this.setVisible(true);
                            }
                            catch (InterruptedException ex) { }
                        }
                         else {
                            //If we're the server but it wasn't a white pawn
                            if (options.gameType == 1 && board[rowtwo][coltwo].getColor() == "BLACK") {
                                try {
                                    data = options.server.receiveData();
                                } catch (IOException ex) {
                                    Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            //If we're the client but it wasn't a black pawn
                            if (options.gameType == 2 && board[rowtwo][coltwo].getColor() == "WHITE") {
                                try {
                                    data = options.client.receiveData();
                                } catch (IOException ex) {
                                    Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            piece = data.toCharArray();
                            this.createPiece(piece);
                            this.setEnabled(true);
                            this.setVisible(true);
                        }

                        prom = null;

                    }
                    board[rowone][colone].selected = false;
                    this.repaint();
                    return true;
                }
                board[rowone][colone].selected = false;
                this.repaint();
                return false;
            }
            else{
                board[rowone][colone].selected = false;
                System.out.println("Illegal move1!");
                this.repaint();
                return false;
            }
	}
	else{
            board[rowone][colone].selected = false;
            System.out.println("Illegal move2!");
            this.repaint();
            return false;
	}
    }

    //Gameplay
    private void play() {
        
        this.turnCounter = 1;
        this.repaint();
        String data = "";

        while (Winner == false & pieceCounter >= 3) {
            
            //White's turn
            if (this.turnCounter % 2 == 1) {
                this.AI.generateAllMoves("WHITE");
                this.setMessage("White's Move" + ", Number of Possible Moves: " + (AI.getNumberOfMoves()));
                
                AI.getBoard(board);
                AI.getKing(WKing);
                //We're the server and it's our turn, change the icon to negative
                if (options.gameType == 1 && turnCounter > 1 && !this.hasFocus() && !this.isActive()) {
                    this.setIconImage(negIcon);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.setIconImage(icon);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.setIconImage(negIcon);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.setIconImage(icon);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.setIconImage(negIcon);
                }
                
            }
            //Black's turn
            else {
                this.AI.generateAllMoves("BLACK");
                //new AePlayWave("yes.wav").start();
                this.setMessage("Black's Move" + ", Number of Possible Moves: " + (AI.getNumberOfMoves()));

                AI.getBoard(board);
                AI.getKing(BKing);
                //We're the client and it's our turn, change icon to negative
                if (options.gameType == 2 && !this.hasFocus() && !this.isActive()) {
                    this.setIconImage(negIcon);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.setIconImage(icon);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.setIconImage(negIcon);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.setIconImage(icon);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.setIconImage(negIcon);
                }
            }
            //First input
            while(mouseRow > 7 || mouseRow < 0 || mouseCol > 7 || mouseCol < 0){

                //If we're the server, just wait for manual input
                if (options.gameType == 1 && this.turnCounter%2 == 0) {
                    //Try receiving the clients first selection of a piece (1st input)
                    try {
                        data = options.server.receiveData();
                    } catch (IOException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (NullPointerException e) {
                        //Server couldn't receive data error...
                    }
                    if (data != "") {
                        mouseRow = Integer.parseInt(data.substring(0, 1));
                        mouseCol = Integer.parseInt(data.substring(1, 2));
                    }
                }
                //we're the client, try to receive data
                else if (options.gameType == 2 && this.turnCounter%2 == 1) {
                    try {
                        data = options.client.receiveData();
                    } catch (IOException ex) {
                        Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (NullPointerException e) {
                        System.out.println("Null pointer exception");
                    }
                    if (data != "") {
                        mouseRow = Integer.parseInt(data.substring(0, 1));
                        mouseCol = Integer.parseInt(data.substring(1, 2));
                    }
                }
                else if (options.gameType == 0){
                   System.out.print("");
                }
                //don't do anything, just wait for input
            }

            //Set the row and col values with the input that was just received
            rowone = mouseRow;
            colone = mouseCol;
            
            //if we're the server, send the data after getting manual input
            if (options.gameType == 1 && this.turnCounter%2 == 1) {
                data = String.valueOf(mouseRow) + String.valueOf(mouseCol);
                options.server.sendData(data);
            }
            //we're the client, send the data after getting manual input
            else if (options.gameType == 2 && this.turnCounter%2 == 0) {
                data = String.valueOf(mouseRow) + String.valueOf(mouseCol);
                options.client.sendData(data);
            }
            if (((board[rowone][colone].getColor() == "WHITE") & (turnCounter%2)==1) ||
                    ((board[rowone][colone].getColor() == "BLACK") & (turnCounter%2)==0)){
               
                board[rowone][colone].selected = true; 
                this.repaint();
                mouseRow=9; 
                mouseCol=9;

                //Second input
                while (mouseRow>7 || mouseRow<0 || mouseCol>7 || mouseCol<0){
                    //else if we're the server, just wait for manual input
                    if (options.gameType == 1 && this.turnCounter%2 == 0) {
                        try {
                            data = options.server.receiveData();
                        } catch (IOException ex) {
                            Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        catch (NullPointerException e) {
                            System.out.println("Null pointer exception");
                        }
                        if (data != "") {
                            mouseRow = Integer.parseInt(data.substring(0, 1));
                            mouseCol = Integer.parseInt(data.substring(1, 2));
                        }
                    }
                    //we're the client, try to receive data
                    else if (options.gameType == 2 && this.turnCounter%2 == 1) {
                        try {
                            data = options.client.receiveData();
                            System.out.println("Received data: " + data);
                        } catch (IOException ex) {
                            Logger.getLogger(ChessFrame2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        catch (NullPointerException e) {
                            System.out.println("Null pointer exception");
                        }
                        if (data != "") {
                            mouseRow = Integer.parseInt(data.substring(0, 1));
                            mouseCol = Integer.parseInt(data.substring(1, 2));
                        }
                    }
                     //If we're playing single player
                    else if (options.gameType == 0){
                        System.out.print("");
                    }
                    //don't do anything, just wait
                }
                rowtwo = mouseRow;
                coltwo = mouseCol;

                //if we're the server, send the data after getting manual input
                if (options.gameType == 1 && this.turnCounter%2 == 1) {
                    data = String.valueOf(mouseRow) + String.valueOf(mouseCol);
                    options.server.sendData(data);
                }
                 //we're the client, send the data after getting manual input
                else if (options.gameType == 2 && this.turnCounter%2 == 0) {
                    data = String.valueOf(mouseRow) + String.valueOf(mouseCol);
                    options.client.sendData(data);
                }
                if (updateBoard()){
                    this.turnCounter++;
                    AI.getBoard(this.board);
                    AI.getGameCounter(this.turnCounter);
                    
                    //One or both kings missing
                    if (!AI.getKing(WKing) || !AI.getKing(BKing)) {
                        System.out.println("Game Over");
                        break;
                    }
                }
                this.AI.printAIboard();

                //Winner = AI.checkWinner();
            }
            mouseRow = 9;
            mouseCol = 9;
	}
    }
    
    public static void main(String args[]) {new ChessFrame2();}

    @Override
    public void setIconImage(Image image) {
        super.setIconImage(image);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exitForm();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {       
    }

    public void mouseClicked(MouseEvent e) {
    }

    public synchronized void mousePressed(MouseEvent e) {
        mouseRow = (e.getY()-50)/64;
        mouseCol = (e.getX()-10)/64;
        e.consume();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        this.setIconImage(icon);
    }

    public void mouseExited(MouseEvent e) {}

    public void mouseWheelMoved(MouseWheelEvent e) {
    }

}
