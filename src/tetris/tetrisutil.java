package tetris;

import java.awt.Color;

import javalib.worldimages.*;

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
	static boolean contains(Object[] arr, Object contain) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == contain) {
				return true;
			}
		}
		return false;
	}
	
	static boolean containsAll(Object[] arr, Object contain) {
		for (int i = 0; i < arr.length; i++) {
			if (!(arr[i] == contain)) {
				return false;
			}
		}
		return true;
	}
}

abstract class Meter {
	int place1;
	int place2;
	int decimals;
	
	Meter(int p1, int p2, int d) {
		this.place1 = p1;
		this.place2 = p2;
		this.decimals = d;
	}
	
	abstract public WorldImage getMeterVal();
}

class TimeMeter extends Meter {// the tickspeed must always be 0.01

	TimeMeter(int i) { // in MS
		super(i / (60 * GameState.INVERT_SPEED), (i / GameState.INVERT_SPEED) % 60, i % 1000);
	}

	public WorldImage getMeterVal() {
		return new TextImage("TIME:  " + place1 + ":" + place2 + "." + decimals, Color.BLACK);
	}
	
}

class AtkMeter extends Meter {
	
	AtkMeter(int atk, int t) {
		super(atk, (int) (atk * 60 * GameState.INVERT_SPEED / t), (atk * 6000 * GameState.INVERT_SPEED / t) % 100);
	}
	
	public WorldImage getMeterVal() {
		return new TextImage("ATK:  " + place1 + ", APM:  " + place2 + "." + decimals, Color.BLACK);
	}
}

class PiecesMeter extends Meter {

	PiecesMeter(int p, int t) {
		super(p, (p * GameState.INVERT_SPEED) / t, (p * 100 * GameState.INVERT_SPEED / t) % 100);
	}

	public WorldImage getMeterVal() {
		return new TextImage("PIECES:  " + place1 + ", PPS:  " + place2 + "." + decimals, Color.BLACK);
	}
	
}

class GarbageMeter {
	int garbage; // in lines 
	// describes cheese in queue about to be sent.
	// ex: 2 lines of garbage in queue, meaning 2 will be accepted / cancelled at some point
	
	int maxaccept; // max amt accepted garbage.
	// ex: if 20 lines are queued but maxaccept is 8, 8 lines of cheese will be added to the board per non line clear piece placed
	
	
	GarbageMeter(int g, int max) {
		this.garbage = g;
		this.maxaccept = max;
	}
	
	GarbageMeter() {
		this(0, 8);
	}
	
	void recieve(Board b, int cancel) {
		if (cancel > 0) {
			this.garbage = Math.max(0, garbage - cancel);
		} else {
			b.addCheese(Math.min(maxaccept, garbage));
			this.garbage = Math.max(0, garbage - maxaccept);
		}
	}
	
	WorldImage draw(int height) {
		WorldImage meter = new OverlayImage(
				new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE * height, OutlineMode.OUTLINE, Color.WHITE),
				new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE * height, OutlineMode.SOLID, Color.BLACK));
		WorldImage garbagedisplay = new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE * garbage, OutlineMode.SOLID, Color.RED);
		WorldImage meterwithgarbage = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, garbagedisplay, 0, 0, meter);
		WorldImage maxbar = new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE * maxaccept, OutlineMode.OUTLINE, Color.WHITE);
		return new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, maxbar, 0, 0, meterwithgarbage);
	}
}
