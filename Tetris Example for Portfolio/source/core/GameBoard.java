/**
 * Polytechnic West Project example
 */
 
package core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GameBoard extends Canvas {
	protected final int DEFAULT_PLAY_FIELD_WIDTH = 10;
	protected final int DEFAULT_PLAY_FIELD_HEIGHT = 20;
	protected final Dimension DEFAULT_CELL_SIZE = new Dimension(20, 20);
	protected final Object DEFAULT_PLAY_FIELD_VALUE = Color.DARK_GRAY;
	protected BufferedImage bufferedImgage;
	protected Graphics2D graphics2D;
	protected Object playField[][];

	/**
	 * Constructor
	 *
	 */
	public GameBoard() {
		 super();
	     super.setPreferredSize(new Dimension(200, 400));
	        
		playField = new Object[DEFAULT_PLAY_FIELD_WIDTH][DEFAULT_PLAY_FIELD_HEIGHT];
		
		for (int i = 0; i < DEFAULT_PLAY_FIELD_WIDTH; i++) {
			for (int j = 0; j < DEFAULT_PLAY_FIELD_HEIGHT; j++) {
				playField[i][j] = DEFAULT_PLAY_FIELD_VALUE;
			}
		}
	}
	
	/**
	 * Paint
	 * @see java.awt.Graphics
	 */
	public void paint(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
				
		g2.drawImage(bufferedImgage, 0, 0, null);
		g2.dispose();
	}
	
	/**
	 * The Buffered Image describes an Image with an accessible buffer of image data
	 * @see java.awt.image.BufferedImage
	 * @see java.awt.Graphics2D
	 */
	public void paintBuffer() {
		
		if( bufferedImgage == null ) {
			
			bufferedImgage =((BufferedImage) createImage(getWidth(), getHeight()));
			
			if( bufferedImgage != null ) {
				graphics2D = bufferedImgage.createGraphics();
			}
		}
		
		for (int i = 0; i < DEFAULT_PLAY_FIELD_WIDTH; i++) 
		{
			for (int j = 0; j < DEFAULT_PLAY_FIELD_HEIGHT; j++) 
			{
				graphics2D.setPaint((Color) playField[i][j]);
				graphics2D.fill3DRect(DEFAULT_CELL_SIZE.width * i, DEFAULT_CELL_SIZE.height * j,
									  DEFAULT_CELL_SIZE.width, DEFAULT_CELL_SIZE.height, true);
			}
		}
	}		

}
