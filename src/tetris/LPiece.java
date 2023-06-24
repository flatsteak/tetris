package tetris;

import javalib.worldimages.Posn;

public class LPiece extends APiece {

	static boolean[][] LPIECE_UP = new boolean[][] {
		{ false, false, true, false }, 
		{ true, true, true, false },   
		{ false, false, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] LPIECE_LEFT = new boolean[][] {
		{ true, true, false, false }, 
		{ false, true, false, false },   
		{ false, true, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] LPIECE_RIGHT = new boolean[][] {
		{ false, true, false, false }, 
		{ false, true, false, false },   
		{ false, true, true, false },
		{ false, false, false, false }
	};
	
	static boolean[][] LPIECE_DOWN = new boolean[][] {
		{ false, false, false, false }, 
		{ true, true, true, false },   
		{ true, false, false, false },
		{ false, false, false, false }
	};
	

	LPiece(Posn posn) {
		super(posn, new CycleIndex(LPIECE_UP.clone(), LPIECE_LEFT.clone(), LPIECE_RIGHT.clone(), LPIECE_DOWN.clone()), Tetrimino.L);
	}
	
	public boolean hasSpun(Board b) {
		return false;
	}
	
	public boolean[][] rotInitialState(String s) {
		switch (s) {
		case "up": return LPIECE_UP;
		case "left": return LPIECE_LEFT;
		case "right": return LPIECE_RIGHT;
		default: return LPIECE_DOWN;
		}
	}
}
