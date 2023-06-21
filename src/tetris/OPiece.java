package tetris;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

class OPiece extends APiece {
	// root posn top left
	static boolean[][] OPIECE_UP = new boolean[][] {
			{ true, true, false, false },
			{ true, true, false, false },
			{ false, false, false, false },
			{ false, false, false, false }
	};
	static CycleIndex OPIECE_ROTATIONS = new CycleIndex(OPIECE_UP, OPIECE_UP, OPIECE_UP, OPIECE_UP);
	
	OPiece(Posn posn) {
		super(posn, OPIECE_ROTATIONS, Tetrimino.O);
	}

	public boolean hasSpun(Board b) {
		return false;
	}


	
}
