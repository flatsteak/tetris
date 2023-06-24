package tetris;

import javalib.worldimages.Posn;

class OPiece extends APiece {
	// root posn top left
	static boolean[][] OPIECE_UP = new boolean[][] {
			{ true, true, false, false },
			{ true, true, false, false },
			{ false, false, false, false },
			{ false, false, false, false }
	};
	
	static boolean[][] OPIECE_COPY = OPIECE_UP.clone();
	static CycleIndex OPIECE_ROTATIONS = new CycleIndex(OPIECE_COPY, OPIECE_COPY, OPIECE_COPY, OPIECE_COPY);
	
	
	OPiece(Posn posn) {
		super(posn, OPIECE_ROTATIONS, Tetrimino.O);
	}

	public boolean hasSpun(Board b) {
		return false;
	}
	
	public boolean[][] rotInitialState(String s) {
		return OPIECE_UP;
	}


	
}
