package tetris;

import java.awt.Color;

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
	
	static CycleIndex IPIECE_ROTATIONS = new CycleIndex(IPIECE_UP, IPIECE_LEFT, IPIECE_RIGHT, IPIECE_DOWN);
	IPiece(Posn posn) {
		super(posn, IPIECE_ROTATIONS, Tetrimino.I);
		// TODO Auto-generated constructor stub
	}

	public boolean hasSpun(Board b) {
		return false;
	}
}
