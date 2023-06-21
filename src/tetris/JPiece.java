package tetris;

import java.awt.Color;

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
	
	static CycleIndex JPIECE_ROTATIONS = new CycleIndex(JPIECE_UP, JPIECE_LEFT, JPIECE_RIGHT, JPIECE_DOWN);
	
	JPiece(Posn posn) {
		super(posn, JPIECE_ROTATIONS, Tetrimino.J);
	}

	public boolean hasSpun(Board b) {
		return false;
	}

}
