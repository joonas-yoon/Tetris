package kr.ac.jbnu.se.tetris;

import java.util.ArrayList;
import java.util.Random;

public class BlockFactory {
	private int goalSize = 5;
	private ArrayList<Shape> queue = new ArrayList<>();

	public BlockFactory(int size) {
		fill(size);
	}

	public Shape createBlock() {
		Shape shape = new Shape();
		shape.setRandomShape();
		int rotateCount = new Random().nextInt(4);
		while (rotateCount-- > 0) {
			shape = shape.rotateLeft();
		}
		return shape;
	}

	public Shape getBlock(int idx) {
		if (queue.isEmpty() || idx < 0 || idx >= queue.size()) {
			return null;
		}
		return queue.get(idx);
	}

	public Shape getNextBlock() {
		return getBlock(0);
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
		while (queue.size() < goalSize) {
			queue.add(createBlock());
		}
		while (queue.size() > goalSize) {
			queue.remove(0);
		}
	}
}
