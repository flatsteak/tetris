package tetris;

import javalib.worldimages.Posn;

public class JPiece extends APiece {

	static boolean[][] JPIECE_UP = new boolean[][] {
		{ true, false, false, false }, 
		{ true, true, true, false },   
		{ false, false, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] JPIECE_LEFT = new boolean[][] {
		{ false, true, false, false }, 
		{ false, true, false, false },   
		{ true, true, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] JPIECE_RIGHT = new boolean[][] {
		{ false, true, true, false }, 
		{ false, true, false, false },   
		{ false, true, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] JPIECE_DOWN = new boolean[][] {
		{ false, false, false, false }, 
		{ true, true, true, false },   
		{ false, false, true, false },
		{ false, false, false, false }
	};
	
	
	JPiece(Posn posn) {
		super(posn, new CycleIndex(JPIECE_UP.clone(), JPIECE_LEFT.clone(), JPIECE_RIGHT.clone(), JPIECE_DOWN.clone()), Tetrimino.J);
	}

	public boolean hasSpun(Board b) {
		return false;
	}
	public boolean[][] rotInitialState(String s) {
		switch (s) {
		case "up": return JPIECE_UP;
		case "left": return JPIECE_LEFT;
		case "right": return JPIECE_RIGHT;
		default: return JPIECE_DOWN;
		}
	}

}
