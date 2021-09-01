package base;

import java.awt.*;

public class Cell {
	private final Point DEFAULT_CELL_POINT = new Point(0, 0);
	private final Object DEFAULT_CELL_VALUE = Color.RED;

	private Object value;
	private Point point;

	public Cell() {
		value = DEFAULT_CELL_VALUE;
		point = DEFAULT_CELL_POINT;
	}
	
	public Cell(Object value, Point point) {
		this.value = value;
		this.point = point;
	}
	
	public Cell(Object value, int column, int row) {
		this.value = value;
		point.x = column;
		point.y = row;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}

	public void setColumn(int column) {
		point.x = column;
	}
	
	public void setRow(int row) {
		point.y = row;
	}
	
	public Object getValue() {
		return value;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public int getColumn() {
		return point.x;
	}
	
	public int getRow() {
		return point.y;
	}
}
