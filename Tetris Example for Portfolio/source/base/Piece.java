package base;

import java.awt.*;

public class Piece {
	private final Object DEFAULT_PIECE_VALUE = Color.RED;
	private final Point[] DEFAULT_PIECE_POINTS = {
			new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0)};
	private Object value;
	private Cell cells[];

	public Piece() {
		value = DEFAULT_PIECE_VALUE;
		cells = new Cell[DEFAULT_PIECE_POINTS.length];

		for (int i = 0; i < cells.length; i++) {
			cells[i] = new Cell(value, DEFAULT_PIECE_POINTS[i]);
		}
	}
	
	public Piece(Object value, Point[] points) {
		this.value = value;
		cells = new Cell[points.length];
		
		for (int i = 0; i < cells.length; i++) {
			cells[i] = new Cell(value, points[i]);
		}
	}
	
	public Cell[] getCells() {
		return cells;
	}
	
	public Object getValue() {
		return value;
	}

	public void moveUp() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].setRow(cells[i].getRow() - 1);
		}
	}

	public void moveDown() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].setRow(cells[i].getRow() + 1);
		}
	}

	public void moveLeft() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].setColumn(cells[i].getColumn() - 1);
		}
	}
	
	public void moveRight() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].setColumn(cells[i].getColumn() + 1);
		}
	}

	public void rotateRight() {
		Point centerCellPoint = cells[0].getPoint();
		Cell tempCells[] = new Cell[cells.length];
		
		for (int i = 0; i < cells.length; i++) {
			int tempCol;
			int tempRow;
			
			// Copy cells points to the tempCells
			tempCells[i] = new Cell(value, cells[i].getPoint());

			tempCol = tempCells[i].getColumn() - centerCellPoint.x;
			tempRow = tempCells[i].getRow() - centerCellPoint.y;
			tempCells[i].setPoint(new Point(tempCol, tempRow));

			// matrix calculation (90 degree to the right)
			// | cos -sin 0 | |x|   | 0 -1 0 | |x|   |-y|
			// | sin  cos 0 | |y| = | 1  0 0 | |y| = | x|
			// |  0   0   1 | |1|   | 0  0 1 | |1|   | 1|
			tempCol = tempCells[i].getColumn();
			tempRow = tempCells[i].getRow();
			
			tempCells[i].setColumn(-tempRow);
			tempCells[i].setRow(tempCol);
		
			// relocate the griginal center point
			cells[i].setColumn(tempCells[i].getColumn() + centerCellPoint.x);
			cells[i].setRow(tempCells[i].getRow() + centerCellPoint.y);
		}
	}
	
	public void rotateLeft() {
		Point centerCellPoint = cells[0].getPoint();
		Cell tempCells[] = new Cell[cells.length];
		
		for (int i = 0; i < cells.length; i++) {
			int tempCol;
			int tempRow;
			
			// Copy cells points to the tempCells
			tempCells[i] = new Cell(value, cells[i].getPoint());

			tempCol = tempCells[i].getColumn() - centerCellPoint.x;
			tempRow = tempCells[i].getRow() - centerCellPoint.y;
			//System.out.println(i  + "tempCol = " + tempCol + "\ttempRow = " + tempRow);
			tempCells[i].setPoint(new Point(tempCol, tempRow));

			// matrix calculation (-90 degree to the right)
			// | cos -sin 0 | |x|   | 0  1 0 | |x|   | y|
			// | sin  cos 0 | |y| = | -1 0 0 | |y| = |-x|
			// |  0   0   1 | |1|   | 0  0 1 | |1|   | 1|
			tempCol = tempCells[i].getColumn();
			tempRow = tempCells[i].getRow();
			
			tempCells[i].setColumn(tempRow);
			tempCells[i].setRow(-tempCol);
		
			// relocate the griginal center point
			cells[i].setColumn(tempCells[i].getColumn() + centerCellPoint.x);
			cells[i].setRow(tempCells[i].getRow() + centerCellPoint.y);
		}
	}
}
