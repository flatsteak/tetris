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
	

	ZPiece(Posn posn) {
		super(posn, new CycleIndex(ZPIECE_UP.clone(), ZPIECE_LEFT.clone(), ZPIECE_RIGHT.clone(), ZPIECE_DOWN.clone()), Tetrimino.Z);
	}

	public boolean hasSpun(Board b) {
		return false;
	}
	
	public boolean[][] rotInitialState(String s) {
		switch (s) {
		case "up": return ZPIECE_UP;
		case "left": return ZPIECE_LEFT;
		case "right": return ZPIECE_RIGHT;
		default: return ZPIECE_DOWN;
		}
	}

}
