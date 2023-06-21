package tetris;

import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;

import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

class CycleIndex {
	boolean[][] first;
	boolean[][] left;
	boolean[][] right;
	boolean[][] flip;
	
	CycleIndex(boolean[][] n, boolean[][] l, boolean[][] r, boolean[][] f) {
		this.first = n;
		this.left = l;
		this.right = r;
		this.flip = f;
	}
	
	void cycleRight() {
		boolean[][] rotfirst = this.first.clone();
		boolean[][] rotleft = this.left.clone();
		boolean[][] rotright = this.right.clone();
		boolean[][] rotflip = this.flip.clone();
		this.first = rotright;
		this.right = rotflip;
		this.flip = rotleft;
		this.left = rotfirst;
	}
	void cycleLeft() {
		boolean[][] rotfirst = this.first.clone();
		boolean[][] rotleft = this.left.clone();
		boolean[][] rotright = this.right.clone();
		boolean[][] rotflip = this.flip.clone();
		this.first = rotleft;
		this.left = rotflip;
		this.flip = rotright;
		this.right = rotfirst;
	}
	void cycleFlip() {
		this.cycleLeft();
		this.cycleLeft();
	}
}

enum Rotation {
	CLOCKWISE, COUNTERCLOCKWISE, FLIP;
}

abstract class APiece {
	Posn position;
	CycleIndex piece;
	Tetrimino identity;
	
	APiece(Posn posn, CycleIndex rotations, Tetrimino identity) {
		this.position = posn;
		this.piece = rotations;
		this.identity = identity;
	}
	
	void rotate(Rotation r) {
		if (r.equals(Rotation.CLOCKWISE)) {
			this.piece.cycleRight();
			return;
		}
		if (r.equals(Rotation.COUNTERCLOCKWISE)) {
			this.piece.cycleLeft();
			return;
		}
		if (r.equals(Rotation.FLIP)) {
			this.piece.cycleFlip();
		}
	}
	
	
	boolean checkOverlap(Board b, boolean[][] posns, Posn shift) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (posns[i][j] && b.overlap(new Posn(position.x + j + shift.x, position.y + i + shift.y))) {
					return true;
				}
			}
		}
		return false;
	}
	
	void moveLeft(Board b) {
		Posn shift = new Posn(-1, 0);
		if (!checkOverlap(b, this.piece.first, shift)) {
			this.position = new Posn(this.position.x - 1, this.position.y);
		}
	}
	
	void moveRight(Board b) {
		Posn shift = new Posn(1, 0);
		if (!checkOverlap(b, this.piece.first, shift)) {
			this.position = new Posn(this.position.x + 1, this.position.y);
		}
	}
	
	void softDrop(Board b) {
		Posn shift = new Posn(0, 1);
		if (!checkOverlap(b, this.piece.first, shift)) {
			System.out.println("foo " + this.position);
			this.position = new Posn(this.position.x, this.position.y + 1);
		}
	}
	
	void hardDrop(Board b) {
		for (int i = this.position.y; i < b.height; i++) {
			if (this.checkOverlap(b, this.piece.first, new Posn(0, 1))) {
				System.out.println("placed");
				this.position = new Posn(this.position.x, i);
				b.placePiece(this);
				return;
			}
		}
		this.position = new Posn(this.position.x, b.height - 1);
		b.placePiece(this);
	}
	
	
	public abstract boolean hasSpun(Board b);
	
	
	public void drawPiece(Theme t, WorldScene s, WorldImage cell) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.piece.first[i][j]) {
					s.placeImageXY(cell, (j + this.position.x) * Board.CELL_SIZE, (i + this.position.y) * Board.CELL_SIZE + Board.CELL_SIZE / 2);
				}
			}
		}
	}
}
