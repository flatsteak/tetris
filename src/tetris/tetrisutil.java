package tetris;

interface IComparator<X> {
	boolean compare(X o1, X o2);
}

class TetrisUtil {
	static Residue tetriminoToResidue(Tetrimino t) {
		switch (t) {
		case S: return Residue.S;
		case Z: return Residue.Z;
		case J: return Residue.J;
		case L: return Residue.L;
		case O: return Residue.O;
		case T: return Residue.T;
		case I: return Residue.I;
		default: return Residue.EMPTY;
		
		}
	}
	static boolean containsNull(Object[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == null) {
				return true;
			}
		}
		return false;
	}
}
