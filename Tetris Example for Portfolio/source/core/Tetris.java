/**
 * Polytechnic West Project example
 */
 
package core;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import base.Piece;
import java.awt.BorderLayout;

public class Tetris extends JFrame implements ActionListener, KeyListener, Runnable {
	
	private final int MOVE_UP_CURRENT_PIECE = 0;
	private final int MOVE_DOWN_CURRENT_PIECE = 1;
	private final int MOVE_LEFT_CURRENT_PIECE = 2;
	private final int MOVE_RIGHT_CURRENT_PIECE = 4;
	private final int ROTATE_RIGHT_CURRENT_PIECE = 8;
	private final int ROTATE_LEFT_CURRENT_PIECE = 16;

	private final int MAX_GAME_LEVEL = 5;
	private final long GAME_LEVEL[] = {500L, 400L, 300L, 200L, 100L, 100L};
	private final int GAME_SCORE[] = {10, 20, 30, 40, 50, 60};
	private final int NEXT_LEVEL = 4;
	
	private final Color pieceColorList[] =
		{Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, 
			Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW, Color.WHITE};
	
	private final int MAX_FULL_ROWS = 4;

	private int currentLevel = 0;
	private int deletedLines = 0;
	private int totalScore = 0;

	protected Object playField[][];
	protected Piece currentPiece;
	protected Point piecePoints[];
	protected Thread daemon;
	protected boolean isNewPieceRequired;

	//Forms
	private JPanel pnlNorth;
	private JPanel pnlCentre;
	private JPanel pnlEast;
	private JPanel pnlWest;
	private JPanel pnlSouth;
	
	//Objects
	private StatusBar statusBar;
	private ScorePanel scorePanel;
	private GameBoard gameBoard;
	
	//Menu
	private JMenuBar menubar ;
  	private JMenu menFile, menGame, menHelp;
  	private JMenuItem itmExit, itmStart, itmAbout;
	
	//About
	AboutBox about;
	
	/**
	 * Constructor
	 *
	 */
	public Tetris()	{
		
		setSize(400, 530);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		setTitle("Java Tetris");
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		setMenu();

		setNorthPanel();
		setCentrePanel();
		setEastPanel();
		setWestPanel();
		setSouthPanel();
		
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCentre, BorderLayout.CENTER);
		add(pnlEast, BorderLayout.EAST);
		add(pnlWest, BorderLayout.WEST);
		add(pnlSouth, BorderLayout.SOUTH);
	}

	/**
	 * Create application menu
	 *
	 */
	private void setMenu() {
		
		//About
		about = new AboutBox();

		//Menu
		menubar = new JMenuBar();
		menFile = new JMenu("File");
		menGame = new JMenu("Game");
		menHelp = new JMenu("Help");
		itmExit = new JMenuItem("Exit");
		itmStart = new JMenuItem("Start");
		itmAbout = new JMenuItem("About");
		
		//Shortcuts
		menFile.setMnemonic('F');
		menGame.setMnemonic('G');
		menHelp.setMnemonic('H');
		itmExit.setMnemonic('x');
		itmStart.setMnemonic('S');
		itmAbout.setMnemonic('A');
		
		//Key Accelerator
		itmExit.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
		itmStart.setAccelerator(KeyStroke.getKeyStroke("alt F2"));
		itmExit.addActionListener(this);
		itmStart.addActionListener(this);
		itmAbout.addActionListener(this);
		
		//Adding Items to Menu
		menFile.add(itmExit);
		menGame.add(itmStart);
		menHelp.add(itmAbout);
		
		menubar.add(menFile);
		menubar.add(menGame);
		menubar.add(menHelp);
		
		setJMenuBar(menubar);
	}

	/**
	 * Set BorderLayout
	 *
	 */
	private void setNorthPanel() {
		pnlNorth = new JPanel(new FlowLayout());
		scorePanel = new ScorePanel();
		pnlNorth.add(scorePanel);
	}
	
	/**
	 * Set BorderLayout
	 *
	 */
	private void setCentrePanel() {
		pnlCentre = new JPanel();

		gameBoard = new GameBoard();
		this.playField = gameBoard.playField;

		pnlCentre.add(gameBoard);
		pnlCentre.setBackground(Color.BLACK);
	}
	
	/**
	 * Set BorderLayout
	 *
	 */
	private void setEastPanel() {
		pnlEast = new JPanel();
	}
	
	/**
	 * Set BorderLayout
	 *
	 */
	private void setWestPanel() {
		pnlWest = new JPanel();
	}
	
	/**
	 * Set BorderLayout
	 *
	 */
	private void setSouthPanel() {
		pnlSouth = new JPanel(new FlowLayout());
		statusBar = new StatusBar();
		statusBar.setMessage("Press Alt+F2 to start a new game.");
		pnlSouth.add(statusBar);
	}
	
	/**
	 * 
	 * Start a new game
	 */
	public void startGame() {
		this.setIgnoreRepaint(true);

		isNewPieceRequired = true;
		this.createNewPiece();
		addKeyListener(this);
		daemon = new Thread(this);
		daemon.start();
		requestFocus();
	}
	
	/**
	 * Run Thread
	 */
	public void run() {

		while(true) {
			try {
				Thread.sleep(GAME_LEVEL[currentLevel]);
			} catch (InterruptedException e) {
		
			}

			if(isNewPieceRequired) {
				this.clearRows(); 
				this.createNewPiece();
				isNewPieceRequired = false;
				this.drawCurrentPiece();
			} else {
				this.moveCurrentPiece(MOVE_DOWN_CURRENT_PIECE);
			}
			
			//Creating buffered image
			gameBoard.paintBuffer(); 
			repaint();     
		}
	}
	
	/**
	 * Create a new piece
	 *
	 */
	public void createNewPiece() {
		
		int centerOfColumn = playField.length / 2;

		int pieceColor = (int) Math.floor(Math.random() * 7);
		switch (pieceColor) {
		case 0:
			//[][][][] - Red
			piecePoints = new Point[4];
			piecePoints[0] = new Point(centerOfColumn - 0, 0);
			piecePoints[1] = new Point(centerOfColumn - 1, 0);
			piecePoints[2] = new Point(centerOfColumn - 2, 0);
			piecePoints[3] = new Point(centerOfColumn + 1, 0);
			currentPiece = new Piece(pieceColorList[pieceColor], piecePoints);
			break;

		case 1:
			//[][][] - Blue
			//  []
			piecePoints = new Point[4];
			piecePoints[0] = new Point(centerOfColumn - 0, 0);
			piecePoints[1] = new Point(centerOfColumn - 1, 0);
			piecePoints[2] = new Point(centerOfColumn + 1, 0);
			piecePoints[3] = new Point(centerOfColumn - 0, 1);
			currentPiece = new Piece(pieceColorList[pieceColor], piecePoints);
			break;

		case 2:
			// [][] - Cyan
			// [][]
			piecePoints = new Point[4];
			piecePoints[0] = new Point(centerOfColumn - 0, 0);
			piecePoints[1] = new Point(centerOfColumn - 1, 0);
			piecePoints[2] = new Point(centerOfColumn - 0, 1);
			piecePoints[3] = new Point(centerOfColumn - 1, 1);
			currentPiece = new Piece(pieceColorList[pieceColor], piecePoints);
			break;
		
		case 3:
			//  [][]   - Green
			//[][]
			piecePoints = new Point[4];
			piecePoints[0] = new Point(centerOfColumn - 0, 0);
			piecePoints[1] = new Point(centerOfColumn + 1, 0);
			piecePoints[2] = new Point(centerOfColumn - 0, 1);
			piecePoints[3] = new Point(centerOfColumn - 1, 1);
			currentPiece = new Piece(pieceColorList[pieceColor], piecePoints);
			break;

		case 4:
			//[][]   -Magenta
			//  [][]
			piecePoints = new Point[4];
			piecePoints[0] = new Point(centerOfColumn - 0, 0);
			piecePoints[1] = new Point(centerOfColumn - 1, 0);
			piecePoints[2] = new Point(centerOfColumn + 0, 1);
			piecePoints[3] = new Point(centerOfColumn + 1, 1);
			currentPiece = new Piece(pieceColorList[pieceColor], piecePoints);
			break;
			
		case 5:
			//[][][] - Orange
			//    []
			piecePoints = new Point[4];
			piecePoints[0] = new Point(centerOfColumn - 0, 0);
			piecePoints[1] = new Point(centerOfColumn - 1, 0);
			piecePoints[2] = new Point(centerOfColumn - 0, 1);
			piecePoints[3] = new Point(centerOfColumn - 2, 0);
			currentPiece = new Piece(pieceColorList[pieceColor], piecePoints);
			break;
			
		case 6:
			//[][][] - Pink
			//[]
			piecePoints = new Point[4];
			piecePoints[0] = new Point(centerOfColumn - 0, 0);
			piecePoints[1] = new Point(centerOfColumn - 1, 0);
			piecePoints[2] = new Point(centerOfColumn + 1, 0);
			piecePoints[3] = new Point(centerOfColumn - 1, 1);
			currentPiece = new Piece(pieceColorList[pieceColor], piecePoints);
			break;
		}
	
		//It is not possible, game over! lol
		if(isCurrentMovePossible() != true)
		{
			JOptionPane.showMessageDialog(null, "Game over");
			System.exit(0);	
		}
	}

	/**
	 * Clear current piece
	 *
	 */
	public void clearCurrentPiece() {
		for (int i = 0; i < currentPiece.getCells().length; i++) {
			playField[currentPiece.getCells()[i].getColumn()][currentPiece.getCells()[i].getRow()] = gameBoard.DEFAULT_PLAY_FIELD_VALUE;
		}
	}
	
	/**
	 * Draw current piece
	 *
	 */
	public void drawCurrentPiece() {
		for(int i = 0; i < currentPiece.getCells().length; i++) {
			playField[currentPiece.getCells()[i].getColumn()][currentPiece.getCells()[i].getRow()] = currentPiece.getValue();
		}
	}

	/**
	 * Verify possible piece moviment. 
	 * @return True if the moviment is possible or False it is not
	 */
	public boolean isCurrentMovePossible() {
		for (int i = 0; i < currentPiece.getCells().length; i++) {
			int x, y;
			
			x = currentPiece.getCells()[i].getColumn();
			y = currentPiece.getCells()[i].getRow();

			if ((x < 0) || (x >= playField.length)
					|| (y < 0) || y >= playField[0].length
					|| (playField[currentPiece.getCells()[i].getColumn()][currentPiece.getCells()[i].getRow()] != gameBoard.DEFAULT_PLAY_FIELD_VALUE)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Execute piece moviment 
	 * This moviment can be DOWN or ROTATION
	 * @param movePieceOption UP, DOWN, LEFT, RIGHT
	 */
	public void moveCurrentPiece(int movePieceOption) {
		
		//While moving piece stop creating next one
		if (isNewPieceRequired)
			return;
		
		switch (movePieceOption) {
		case MOVE_UP_CURRENT_PIECE:
			this.clearCurrentPiece();
			currentPiece.moveUp();
			
			if(!this.isCurrentMovePossible()) {
				currentPiece.moveDown();
			}
			this.drawCurrentPiece();
			break;

		case MOVE_DOWN_CURRENT_PIECE:
			this.clearCurrentPiece();
			currentPiece.moveDown();
			
			if(!this.isCurrentMovePossible()) {
				currentPiece.moveUp();
				isNewPieceRequired = true; // new piece required here, but piece isnt created
													// therefore user can still move the previous piece
			}
			this.drawCurrentPiece();
			break;

		case MOVE_LEFT_CURRENT_PIECE:
			this.clearCurrentPiece();
			currentPiece.moveLeft();
			
			if(!this.isCurrentMovePossible()) {
				currentPiece.moveRight();
			}
			this.drawCurrentPiece();
			break;

		case MOVE_RIGHT_CURRENT_PIECE:
			this.clearCurrentPiece();
			currentPiece.moveRight();
			
			if(!this.isCurrentMovePossible()) {
				currentPiece.moveLeft();
			}
			this.drawCurrentPiece();
			break;

		case ROTATE_RIGHT_CURRENT_PIECE:
			this.clearCurrentPiece();
			currentPiece.rotateRight();
			
			if(!this.isCurrentMovePossible()) {
				currentPiece.rotateLeft();
			}
			this.drawCurrentPiece();
			break;

		case ROTATE_LEFT_CURRENT_PIECE:
			this.clearCurrentPiece();
			currentPiece.rotateLeft();
			
			if(!this.isCurrentMovePossible()) {
				currentPiece.rotateRight();
			}
			this.drawCurrentPiece();
			break;
		}
		
		//refreshing screen
		gameBoard.paintBuffer(); 
		gameBoard.repaint();
	}

	/**
	 * Looks for full lines and clear them moving up pieces to down position
	 *
	 */
	public void clearRows()	{
		
		int count = 0;
		
		//Look for 4 possibles full rows
		for (int r = 0; r < MAX_FULL_ROWS; r++) {
			for (int i = (gameBoard.DEFAULT_PLAY_FIELD_HEIGHT - 1); i > 0; i--) {
				boolean isFullRow = true;
				
				for (int j = 0; j < gameBoard.DEFAULT_PLAY_FIELD_WIDTH; j++) {
					if(playField[j][i] == gameBoard.DEFAULT_PLAY_FIELD_VALUE) {
						isFullRow = false;
						break;
					}
				}
	
				//Row is full, clear it
				if (isFullRow) {
					count++;

					if (count > MAX_FULL_ROWS)
						count = 0;

					for (int k = (i - 1); k > 1; k--) {	
						for (int l = 0; l < gameBoard.DEFAULT_PLAY_FIELD_WIDTH; l++) {
							playField[l][k + 1] = playField[l][k]; 
					   }
					}
				}		
			}
		}
		
		//Compute Score
		deletedLines += count;
		totalScore  += count * GAME_SCORE[currentLevel];
		scorePanel.setScore(totalScore);
		
		if (deletedLines >= NEXT_LEVEL) {
			currentLevel++;
			
			if (currentLevel > MAX_GAME_LEVEL)
				currentLevel = 0;
			
			scorePanel.setLevel(currentLevel + 1);
			deletedLines = 0;
		}

	}

	/**
	 * Stop thread
	 *
	 */
	public void stop() {
		daemon = null;
	}

	/**
	 * Check with key has been pressed
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.moveCurrentPiece(MOVE_DOWN_CURRENT_PIECE);
			this.drawCurrentPiece();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.moveCurrentPiece(MOVE_LEFT_CURRENT_PIECE);
			this.drawCurrentPiece();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.moveCurrentPiece(MOVE_RIGHT_CURRENT_PIECE);
			this.drawCurrentPiece();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			this.moveCurrentPiece(ROTATE_RIGHT_CURRENT_PIECE);
//			this.moveCurrentPiece(ROTATE_LEFT_CURRENT_PIECE);
			this.drawCurrentPiece();
		} else {
			// No need for this condition...
		}
	}

	/**
	 * Check with key has been released
	 * It is not being used
	 */
	public void keyReleased(KeyEvent e) {
	
	}
	
	/**
	 * Check with key has been typed
	 * It is being used.
	 */
	public void keyTyped(KeyEvent e) {
	
	}
	
	/**
	 * This event is used to interact with menu options
	 */
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		//Menu Exit
		if (obj == itmExit)
			System.exit(0);
		
		//Menu Game
		if (obj == itmStart)
			if (daemon == null)
				startGame();

		//About Dialog
		if (obj == itmAbout)
			about.setVisible(true);
	}

	/**
	 * Application point start
	 * @param args array of strings
	 */
	public static void main (String[] args) {
		Tetris game = new Tetris();
		game.setVisible(true);
	}
}
