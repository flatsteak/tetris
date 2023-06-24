package tetris;

import javalib.worldimages.Posn;

public class SPiece extends APiece {

	static boolean[][] SPIECE_UP = new boolean[][] {
		{ false, true, true, false }, 
		{ true, true, false, false },   
		{ false, false, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] SPIECE_LEFT = new boolean[][] {
		{ true, false, false, false }, 
		{ true, true, false, false },   
		{ false, true, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] SPIECE_RIGHT = new boolean[][] {
		{ false, true, false, false }, 
		{ false, true, true, false },   
		{ false, false, true, false },
		{ false, false, false, false }
	};
	
	static boolean[][] SPIECE_DOWN  = new boolean[][] {
		{ false, false, false, false }, 
		{ false, true, true, false },   
		{ true, true, false, false },
		{ false, false, false, false }
	};
	
	SPiece(Posn posn) {
		super(posn, new CycleIndex(SPIECE_UP.clone(), SPIECE_LEFT.clone(), SPIECE_RIGHT.clone(), SPIECE_DOWN.clone()), Tetrimino.S);
	}

	@Override
	public boolean hasSpun(Board b) {
		return false;
	}
	
	public boolean[][] rotInitialState(String s) {
		switch (s) {
		case "up": return SPIECE_UP;
		case "left": return SPIECE_LEFT;
		case "right": return SPIECE_RIGHT;
		default: return SPIECE_DOWN;
		}
	}

}
