package kr.ac.jbnu.se.tetris;

import java.util.ArrayList;

public class BlockFactory {
	
	private int goalSize = 5;
	private ArrayList<Shape> queue = new ArrayList<Shape>();
	
	public BlockFactory(int size) {
		fill(size);
	}
	
	public Shape createBlock() {
		Shape shape = new Shape();
		shape.setRandomShape();
		return shape;
	}
	
	public Shape getNextBlock() {
		if(queue.isEmpty()) return null;
		return queue.get(0);
	}
	
	public void pop() {
		queue.remove(0);
		fill(goalSize);
	}

	public void setSize(int size) {
		fill(size);
	}
	
	public int getSize() {
		return queue.size();
	}
	
	private void fill(int size) {
		goalSize = size;
		while(queue.size() < goalSize) {
			queue.add(createBlock());
		}
		while(queue.size() > goalSize) {
			queue.remove(0);
		}
	}
}
