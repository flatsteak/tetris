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
	
	static CycleIndex SPIECE_ROTATIONS = new CycleIndex(SPIECE_UP, SPIECE_LEFT, SPIECE_RIGHT, SPIECE_DOWN);
	
	SPiece(Posn posn) {
		super(posn, SPIECE_ROTATIONS, Tetrimino.S);
	}

	@Override
	public boolean hasSpun(Board b) {
		return false;
	}

}
