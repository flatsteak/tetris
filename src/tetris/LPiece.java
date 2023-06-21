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
	
	static CycleIndex LPIECE_ROTATIONS = new CycleIndex(LPIECE_UP, LPIECE_LEFT, LPIECE_RIGHT, LPIECE_DOWN);

	LPiece(Posn posn) {
		super(posn, LPIECE_ROTATIONS, Tetrimino.L);
	}
	
	public boolean hasSpun(Board b) {
		return false;
	}
}
