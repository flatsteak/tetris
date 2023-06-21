package tetris;

import java.awt.Color;

import javalib.worldimages.Posn;

public class ZPiece extends APiece {
	
	static boolean[][] ZPIECE_UP = new boolean[][] {
		{ true, true, false, false }, 
		{ false, true, true, false },   
		{ false, false, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] ZPIECE_LEFT = new boolean[][] {
		{ false, true, false, false }, 
		{ true, true, false, false },   
		{ true, false, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] ZPIECE_RIGHT = new boolean[][] {
		{ false, false, true, false }, 
		{ false, true, true, false },   
		{ false, true, false, false },
		{ false, false, false, false }
	};
	
	static boolean[][] ZPIECE_DOWN = new boolean[][] {
		{ false, false, false, false }, 
		{ true, true, false, false },   
		{ false, true, true, false },
		{ false, false, false, false }
	};
	
	static CycleIndex ZPIECE_ROTATIONS = new CycleIndex(ZPIECE_UP, ZPIECE_LEFT, ZPIECE_RIGHT, ZPIECE_DOWN);

	ZPiece(Posn posn) {
		super(posn, ZPIECE_ROTATIONS, Tetrimino.Z);
	}

	public boolean hasSpun(Board b) {
		return false;
	}

}
