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
	
	static CycleIndex TPIECE_ROTATIONS = new CycleIndex(TPIECE_UP, TPIECE_LEFT, TPIECE_RIGHT, TPIECE_DOWN);

	TPiece(Posn posn) {
		super(posn, TPIECE_ROTATIONS, Tetrimino.T);
	}

	
	public boolean hasSpun(Board b) {
		Residue[] bottom = b.residue.get(this.position.y);
		Residue[] top = b.residue.get(this.position.y + 2);
		return !(top[this.position.x].equals(Residue.EMPTY) && top[this.position.x + 2].equals(Residue.EMPTY)) &&
				!(bottom[this.position.x].equals(Residue.EMPTY) || bottom[this.position.x].equals(Residue.EMPTY));
	}

}
