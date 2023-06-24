package tetris;

import java.awt.Color;
import java.util.Arrays;

import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;

public class TPiece extends APiece {

	static boolean[][] TPIECE_UP = new boolean[][] {
		{ false, true, false, false }, // *  
		{ true, true, true, false },   //***
		{ false, false, false, false },
		{ false, false, false, false }
	};
	static boolean[][] TPIECE_DOWN = new boolean[][] {
		{ false, false, false, false }, // 
		{ true, true, true, false },    // ***
		{ false, true, false, false },  //  *
		{ false, false, false, false }
	};
	static boolean[][] TPIECE_RIGHT = new boolean[][] {
		{ false, true, false, false }, // * 
		{ false, true, true, false },  // **
		{ false, true, false, false }, // *
		{ false, false, false, false }
	};
	static boolean[][] TPIECE_LEFT = new boolean[][] {
		{ false, true, false, false }, // *
		{ true, true, false, false },  //** 
		{ false, true, false, false }, // *
		{ false, false, false, false }
	};
	

	TPiece(Posn posn) {
		super(posn, new CycleIndex(TPIECE_UP.clone(), TPIECE_LEFT.clone(), TPIECE_RIGHT.clone(), TPIECE_DOWN.clone()), Tetrimino.T);
	}

	
	public boolean hasSpun(Board b) {
		Residue[] top = b.residue.get(this.position.y);
		Residue[] bottom = b.residue.get(this.position.y + 2);
		
		if (this.position.x + 2 > 9 || this.position.x < 0) {
			return false;
		}
		boolean topBlockPresent = !top[this.position.x].equals(Residue.EMPTY) || !top[this.position.x + 2].equals(Residue.EMPTY);
		System.out.println(topBlockPresent);
		boolean bottomBlockPresent = !bottom[this.position.x].equals(Residue.EMPTY) && !bottom[this.position.x + 2].equals(Residue.EMPTY);
		System.out.println(bottomBlockPresent);
		return topBlockPresent && bottomBlockPresent;
	}
	
	public boolean[][] rotInitialState(String s) {
		switch (s) {
		case "up": return TPIECE_UP;
		case "left": return TPIECE_LEFT;
		case "right": return TPIECE_RIGHT;
		default: return TPIECE_DOWN;
		}
	}

}
