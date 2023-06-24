package tetris;

import java.util.List;

import javalib.worldimages.Posn;

public class IPiece extends APiece {
	
	static boolean[][] IPIECE_UP = new boolean[][] {
		{ false, false, false, false }, 
		{ true, true, true, true },   
		{ false, false, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] IPIECE_LEFT = new boolean[][] {
		{ false, true, false, false }, 
		{ false, true, false, false },   
		{ false, true, false, false },
		{ false, true, false, false }
	};
	
	static boolean[][] IPIECE_RIGHT = new boolean[][] {
		{ false, false, true, false }, 
		{ false, false, true, false },   
		{ false, false, true, false },
		{ false, false, true, false }
	};
	
	static boolean[][] IPIECE_DOWN = new boolean[][] {
		{ false, false, false, false }, 
		{ false, false, false, false },   
		{ true, true, true, true },
		{ false, false, false, false }
	};
	
	IPiece(Posn posn) {
		super(posn, new CycleIndex(IPIECE_UP.clone(), IPIECE_LEFT.clone(), IPIECE_RIGHT.clone(), IPIECE_DOWN.clone()), Tetrimino.I);
	}

	public boolean hasSpun(Board b) {
		return false;
	}

	public boolean[][] rotInitialState(String s) {
		switch (s) {
		case "up": return IPIECE_UP;
		case "left": return IPIECE_LEFT;
		case "right": return IPIECE_RIGHT;
		default: return IPIECE_DOWN;
		}
	}
	
	public List<Posn> getKickTests(Rotation r) {
		if (this.piece.first == this.rotInitialState("up")) {
			switch (r) {
			case CLOCKWISE: return List.of(new Posn(0, 0), new Posn(-2, 0), new Posn(1, 0), new Posn(-2, 1), new Posn(1, 2));
			case COUNTERCLOCKWISE: return List.of(new Posn(0, 0), new Posn(-1, 0), new Posn(2, 0), new Posn(-1, 2), new Posn(2, -1));
			default: return List.of(new Posn(0, 0));
			}
		} else if (this.piece.first == this.rotInitialState("right")) {
			switch (r) {
			case CLOCKWISE: return List.of(new Posn(0, 0), new Posn(-1, 0), new Posn(2, 0), new Posn(-1, -2), new Posn(2, -1));
			case COUNTERCLOCKWISE: return List.of(new Posn(0, 0), new Posn(2, 0), new Posn(-1, 0), new Posn(2, 1), new Posn(-1, -2));
			default: return List.of(new Posn(0, 0));
			}
		} else if (this.piece.first == this.rotInitialState("left")) {
			switch (r) {
			case CLOCKWISE: return List.of(new Posn(0, 0), new Posn(1, 0), new Posn(-2, 0), new Posn(1, -2), new Posn(-2, 1));
			case COUNTERCLOCKWISE: return List.of(new Posn(0, 0), new Posn(-2, 0), new Posn(1, 0), new Posn(-2, -1), new Posn(1, 2));
			default: return List.of(new Posn(0, 0));
			}
		} else {
			switch (r) {
			case CLOCKWISE: return List.of(new Posn(0, 0), new Posn(2, 0), new Posn(-1, 0), new Posn(2, 1), new Posn(-1, -2));
			case COUNTERCLOCKWISE: return List.of(new Posn(0, 0), new Posn(1, 0), new Posn(-2, 0), new Posn(1, -2), new Posn(-2, 1));
			default: return List.of(new Posn(0, 0));
			}
		}
		
	}
	
	
}
