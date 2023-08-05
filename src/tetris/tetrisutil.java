package tetris;

import java.awt.Color;

import javalib.worldimages.*;

interface IComparator<X> {
	boolean compare(X o1, X o2);
}

interface IFunc<X> {
	X apply(X arg);
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
	
	static double logBase(double base, double arg) {
		return Math.log(arg) / Math.log(base);
	}
	
	static WorldImage outlineCircleInterior(int amt, int size, Color c) {
		WorldImage toreturn = new CircleImage(size, OutlineMode.OUTLINE, c);
		for (int i = size - 1; i > size - amt; i--) {
			toreturn = new OverlayImage(toreturn, new CircleImage(i, OutlineMode.OUTLINE, c));
		}
		return toreturn;
	}
	
	static WorldImage outlineCircleInteriorFade(int amt, int size, float alpha) {
		WorldImage toreturn = new CircleImage(size, OutlineMode.OUTLINE, new Color(0f, 0f, 0f, alpha));
		int middle = size - amt / 2;
		for (int i = size - 1; i > size - amt; i--) {
			float grayamt = Math.abs(i - middle) / amt;
			toreturn = new OverlayImage(toreturn, new CircleImage(i, OutlineMode.OUTLINE, new Color(grayamt, grayamt, grayamt, Math.max(alpha - grayamt, 0f))));
		}
		return toreturn;
	}
	
	
	static WorldImage outlineTriangleInterior(int amt, int size, Color c) {
		WorldImage toreturn = new EquilateralTriangleImage(size, OutlineMode.OUTLINE, c);
		for (int i = size - 1; i > size - amt; i--) {
			toreturn = new OverlayImage(toreturn, new EquilateralTriangleImage(i, OutlineMode.OUTLINE, c));
		}
		return toreturn;
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
	
	static Color modifyAll(Color c, double i) {
		return new Color((int) (c.getRed() * i), (int) (c.getGreen() * i), (int) (c.getBlue() * i));
	}
	
	static WorldImage outlineFadeToBlack(Color c, int size) {
		Color c1 = modifyAll(c, 0.8);
		Color c2 = modifyAll(c, 0.6);
		Color c3 = modifyAll(c, 0.4);
		Color c4 = modifyAll(c, 0.2);
		int sizenew = (int) (size * 0.1);
		return new OverlayImage(
				new RectangleImage(size - sizenew * 4, size - sizenew * 4, OutlineMode.SOLID, c),
				new OverlayImage(
						new RectangleImage(size - sizenew * 3, size - sizenew * 3, OutlineMode.SOLID, c1),
						new OverlayImage(
								new RectangleImage(size - sizenew * 2, size - sizenew * 2, OutlineMode.SOLID, c2),
								new OverlayImage(
										new RectangleImage(size - sizenew, size - sizenew, OutlineMode.SOLID, c3),
										new RectangleImage(size, size, OutlineMode.SOLID, c4)))));
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
		return new TextImage("TIME:  " + place1 + ":" + place2 + "." + decimals, Color.WHITE);
	}
	
}

class AtkMeter extends Meter {
	
	AtkMeter(int atk, int t) {
		super(atk, (int) (atk * 60 * GameState.INVERT_SPEED / t), (atk * 6000 * GameState.INVERT_SPEED / t) % 100);
	}
	
	public WorldImage getMeterVal() {
		return new TextImage("ATK:  " + place1 + ", APM:  " + place2 + "." + decimals, Color.WHITE);
	}
}

class PiecesMeter extends Meter {

	PiecesMeter(int p, int t) {
		super(p, (p * GameState.INVERT_SPEED) / t, (p * 100 * GameState.INVERT_SPEED / t) % 100);
	}

	public WorldImage getMeterVal() {
		return new TextImage("PIECES:  " + place1 + ", PPS:  " + place2 + "." + decimals, Color.WHITE);
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
	
	void recieve(Board b) {
		int torecieve = Math.min(maxaccept, Math.max(0, garbage));
		b.addCheese(torecieve);
		this.garbage = garbage - torecieve;
	}
	
	WorldImage draw(int height) {
		WorldImage meter = new OverlayImage(
				new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE * height, OutlineMode.OUTLINE, Color.WHITE),
				new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE * height, OutlineMode.SOLID, Color.BLACK));
		WorldImage garbagedisplay = (this.garbage > 0) ? new RectangleImage(Board.CELL_SIZE, Math.min(Board.CELL_SIZE * 20, Board.CELL_SIZE * garbage), OutlineMode.SOLID, Color.RED) : 
			new RectangleImage(Board.CELL_SIZE, Math.min(Board.CELL_SIZE * 20, Board.CELL_SIZE * -garbage), OutlineMode.SOLID, Theme.SKYBLUE);
		
		WorldImage meterwithgarbage = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, garbagedisplay, 0, 0, meter);
		WorldImage maxbar = new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE * maxaccept, OutlineMode.OUTLINE, Color.WHITE);
		
		return new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, maxbar, 0, 0, meterwithgarbage);
	}
	
	WorldImage displayLines() {
		WorldImage extragarbage = new TextImage("" + garbage, 30, Color.RED);
		if (garbage < 0) {
			extragarbage = new TextImage("" + -garbage, 30, Theme.SKYBLUE);
		}
		
		return extragarbage;
	}
}

class ChargeMeter {
	int power; // in lines 
	// describes cheese in queue about to be sent.
	// ex: 2 lines of garbage in queue, meaning 2 will be accepted / cancelled at some point
	
	
	ChargeMeter(int g) {
		this.power = g;
	}
	
	ChargeMeter() {
		this(0);
	}
	
	void send(GameState g) {
		g.bot.sendLines(power);
		this.power = 0;
	}
	
	WorldImage draw(int height) {
		WorldImage meter = new OverlayImage(
				new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE * height, OutlineMode.OUTLINE, Color.WHITE),
				new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE * height, OutlineMode.SOLID, Color.BLACK));
		WorldImage garbagedisplay = new RectangleImage(Board.CELL_SIZE, Math.min(Board.CELL_SIZE * 20, Board.CELL_SIZE * power), OutlineMode.SOLID, Theme.PURPLE);
		
		WorldImage meterwithgarbage = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, garbagedisplay, 0, 0, meter);
		
		return meterwithgarbage;
	}
	
	WorldImage displayLines() {
		return new TextImage("" + power, 30, Theme.PURPLE);
	}
}
