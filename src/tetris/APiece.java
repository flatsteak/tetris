package tetris;


import java.util.List;

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

	int getEmptyLineCountY() {
		for (int i = 3; i >= 0; i -= 1) {
			boolean foundFilled = false;
			for (int j = 0; j < 4; j += 1) {
				if (this.piece.first[i][j]) {
				foundFilled = true;
					break;
				}
			}
			if (foundFilled) {
				return 3 - i;
			}
		}
		throw new RuntimeException("Impossible piece");
	}
	 
	int getEmptyLineCountFromRight() {
		for (int i = 3; i >= 0; i -= 1) {
			boolean foundFilled = false;
			for (int j = 0; j < 4; j += 1) {
				if (this.piece.first[j][i]) {
					foundFilled = true;
					break;
				}
			}
			if (foundFilled) {
				return 3 - i;
			}
		}
		throw new RuntimeException("Impossible piece");
	}

	
	int getEmptyLineCountFromLeft() {
		for (int i = 0; i <= 3; i += 1) {
			boolean foundFilled = false;
			for (int j = 0; j < 4; j += 1) {
				if (this.piece.first[j][i]) {
					foundFilled = true;
					break;
				}
			}
			if (foundFilled) {
				return i;
			}
		} 
		throw new RuntimeException("Impossible piece");
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

	
	void softDropInf(Board b) {
		for (int i = this.position.y; i < b.height; i++) {
			if (this.checkOverlap(b, this.piece.first, new Posn(0, i - this.position.y + 1))) {
				this.position = new Posn(this.position.x, i);
				return;
			}
		}
		this.position = new Posn(this.position.x, b.height - this.getEmptyLineCountY());
	}
	
	void moveLeftInf(Board b) {
		for (int i = 1; i < this.position.x + this.getEmptyLineCountFromLeft(); i++) {
			if (this.checkOverlap(b, this.piece.first, new Posn(-i, 0))) {
				this.position = new Posn(this.position.x - i + 1, this.position.y);
				return;
			}
		}
		this.position = new Posn(-this.getEmptyLineCountFromLeft(), this.position.y);
	}
	
	void moveRightInf(Board b) {
		for (int i = this.position.x; i < b.width; i++) {
			if (this.checkOverlap(b, this.piece.first, new Posn(i - this.position.x + 1, 0))) {
				this.position = new Posn(i, this.position.y);
				return;
			}
		}
		this.position = new Posn(b.width - this.getEmptyLineCountFromRight(), this.position.y);
	}
	
	void hardDrop(Board b) {
		for (int i = this.position.y; i < b.height; i++) {
			if (this.checkOverlap(b, this.piece.first, new Posn(0, i - this.position.y + 1))) {
				this.position = new Posn(this.position.x, i);
				b.placePiece(this);
				return;
			}
		}
		// Place it at the bottom of the board, which is b.height - 1, but adjusted for the piece
		this.position = new Posn(this.position.x, b.height - this.getEmptyLineCountY());
		b.placePiece(this);
		b.pieceplaced = true;
		
	}


	public abstract boolean hasSpun(Board b);
	public abstract boolean[][] rotInitialState(String s);

	public List<Posn> getKickTests(Rotation r) {
		if (this.piece.first == this.rotInitialState("up")) {
			switch (r) {
			case CLOCKWISE:
				return List.of(new Posn(0, 0), new Posn(-1, 0), new Posn(-1, 1), new Posn(0, 2), new Posn(-1, 2));
			case COUNTERCLOCKWISE:
				return List.of(new Posn(0, 0), new Posn(1, 0), new Posn(1, 1), new Posn(0, 2), new Posn(1, 2));
			default:
				return List.of(new Posn(0, 0), new Posn(0, -1));
			}
		} else if (this.piece.first == this.rotInitialState("right")) {
			switch (r) {
			case CLOCKWISE:
				return List.of(new Posn(0, 0), new Posn(1, 0), new Posn(1, 1), new Posn(0, -2), new Posn(1, 2));
			case COUNTERCLOCKWISE:
				return List.of(new Posn(0, 0), new Posn(1, 0), new Posn(1, 1), new Posn(1, 0), new Posn(0, -2), new Posn(1, 2));
			default:
				return List.of(new Posn(0, 0), new Posn(0, -1));
			}
		} else if (this.piece.first == this.rotInitialState("left")) {
			switch (r) {
			case CLOCKWISE:
				return List.of(new Posn(0, 0), new Posn(-1, 0), new Posn(-1, 1), new Posn(0, 2), new Posn(-1, 2));
			case COUNTERCLOCKWISE:
				return List.of(new Posn(0, 0), new Posn(-1, 0), new Posn(-1, 1), new Posn(0, 2), new Posn(-1, 2));
			default:
				return List.of(new Posn(0, 0), new Posn(0, -1));
			}
		} else {
			switch (r) {
			case CLOCKWISE:
				return List.of(new Posn(0, 0), new Posn(-1, 0), new Posn(-1, 1), new Posn(0, 2), new Posn(-1, 2));
			case COUNTERCLOCKWISE:
				return List.of(new Posn(0, 0), new Posn(1, 0), new Posn(1, 1), new Posn(0, 2), new Posn(1, 2));
			default:
				return List.of(new Posn(0, 0), new Posn(0, -1));
			}
		}

	}

	public void drawPiece(Theme t, WorldScene s, WorldImage cell) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.piece.first[i][j]) {
					s.placeImageXY(
						cell,
						(j + this.position.x) * Board.CELL_SIZE + Board.CELL_SIZE / 2,
						(i + this.position.y) * Board.CELL_SIZE + Board.CELL_SIZE / 2);
				}
			}
		}
	}
}
