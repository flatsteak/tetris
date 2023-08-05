package tetris;

import java.awt.Color;
import java.util.*;
import java.util.stream.IntStream;

import javalib.impworld.*;
import javalib.worldimages.*;

class AnimateValue {
	static IFunc<Float> OUTSIN = (a) -> (float) Math.sin(a * (Math.PI / 2));
	static IFunc<Float> INOUTSIN = (a) -> (float) -(Math.cos(a * Math.PI) - 1) / 2;
	static IFunc<Float> ROOT = (a) -> (float) Math.sqrt(a);
	static IFunc<Float> ASYM = (a) -> (float) 2 * a / (a + 1);
	static IFunc<Float> LOGBASE100 = (a) -> (float) ((TetrisUtil.logBase(100, a + 1.01) + 1) / (TetrisUtil.logBase(100, 1.01) + 1));
	static IFunc<Float> ARCTAN = (a) -> (float) (Math.atan(2 * a) / Math.atan(2));
}

abstract class Animation {
	long start; // ms
	long end; // ms
	int duration; // ms
	Posn posn;
	
	Animation(int duration) {
		this.duration = duration;
	}
	
	Animation withPosn(Posn p) {
		this.posn = p;
		return this;
	}
	
	void start(Posn at) {
		this.start();
		this.posn = at;		
	}
	
	void start() {
		this.start = System.currentTimeMillis();
		this.end = this.start + duration;
	}
	
	float getCurrent() {
		return Math.min((System.currentTimeMillis() - this.start) / (float) this.duration, 1.0f);
	}
	
	boolean isEnded() {
		return System.currentTimeMillis() >= this.end;
	}
	
	abstract WorldImage getAnim();
}

class Flashwave extends Animation {
	int radius;
	static int SIZE = 20; // # thickness outline
	static int SPEED = 1; // in ticks (ms)
	static double RATE = 5; // px / tickspeed
	static int SIZE_LIMIT = SIZE * 50;
	
	Flashwave(Posn p) {
		super(500);
		this.posn = p;
	}

	WorldImage getAnim() {
		return TetrisUtil.outlineCircleInteriorFade(SIZE, (int) this.getCurrent() * SIZE_LIMIT, 1f - this.getCurrent());
	}
}


class LinearSizeTextAnimation extends Animation {
	
	String s;
	Color c;
	LinearSizeTextAnimation(String s, int end, Color c) {
		super(end);
		this.s = s;
		this.c = c;
	}
	int speed;
	int rate;
	
	WorldImage getAnim() {
		return new TextImage(s, this.getCurrent(), c);
	}
	
}

class CircleSizeAnimation extends Animation {
	IFunc<Float> sizefunc;
	int size;
	Color c;
	int maxpx;
	CircleSizeAnimation(int duration, IFunc<Float> sizefunc, int size, Color c, Posn p, int maxpx) {
		super(duration);
		this.sizefunc = sizefunc;
		this.size = size;
		this.c = c;
		this.posn = p;
		this.maxpx = maxpx;
	}

	WorldImage getAnim() {
		return TetrisUtil.outlineCircleInterior(size, (int) (maxpx * sizefunc.apply(getCurrent())), c);
	}
}

class FadingCircleSizeAnimation extends CircleSizeAnimation {

	FadingCircleSizeAnimation(int duration, IFunc<Float> sizefunc, int size, Color c, Posn p, int maxpx) {
		super(duration, sizefunc, size, c, p, maxpx);
	}
	
	WorldImage getAnim() {
		
		int pos = (int) (maxpx * sizefunc.apply(getCurrent()));
		int outlines = (maxpx - size > pos)? size : size - (pos - (maxpx - size));
		
		return TetrisUtil.outlineCircleInterior(outlines, pos, c);
	}
	
}



class FadingRotatingTriangleAnimation extends Animation {
	IFunc<Float> sizefunc;
	Color c;
	int size;
	int thickness;
	int maxrot;
	
	

	FadingRotatingTriangleAnimation(int duration, IFunc<Float> sizefunc, int size, Color c, Posn p, int maxrot, int thickness) {
		super(duration);
		this.sizefunc = sizefunc;
		this.size = size;
		this.c = c;
		this.posn = p;
		this.maxrot = maxrot;
		this.thickness = thickness;
		
	}


	WorldImage getAnim() {
		int pos = (int) (maxrot * sizefunc.apply(getCurrent()));
		int outlines = thickness;
		if (maxrot - pos < thickness / 10) {
			outlines = 10 * (maxrot - pos);
		} else if (pos < thickness / 10) {
			outlines = 10 * pos;
		}
		return new RotateImage(TetrisUtil.outlineTriangleInterior(outlines, size, c), pos);
	}
	
}


class LinearFlash extends Animation {
	
	Color c;
	
	static Posn FLASH_POSN = new Posn(0, 0);

	LinearFlash(Color c, int end) {
		super(end);
		this.c = c;
	}

	WorldImage getAnim() {
		float pos = c.getAlpha() - c.getAlpha() * getCurrent();
		return new RectangleImage(GameState.SCREEN_WIDTH, GameState.SCREEN_HEIGHT, OutlineMode.SOLID, new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) pos)).movePinhole(-GameState.SCREEN_WIDTH / 2, -GameState.SCREEN_HEIGHT / 2);
	}
	
}

class InverseLinearFlash extends Animation {
	Color c;

	InverseLinearFlash(Color c, int end) {
		super(end);
		this.c = c;
	}

	WorldImage getAnim() {
		float pos = c.getAlpha() * getCurrent();
		return new RectangleImage(GameState.SCREEN_WIDTH, GameState.SCREEN_HEIGHT, OutlineMode.SOLID, new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) pos)).movePinhole(-GameState.SCREEN_WIDTH / 2, -GameState.SCREEN_HEIGHT / 2);
	}
}


class FadingTextAnimation extends Animation {
	IFunc<Float> step;
	String text;
	Color c;
	int size;
	
	FadingTextAnimation(int duration, int size, Color c, String text, IFunc<Float> step) {
		super(duration);
		this.step = step;
		this.c = c;
		this.text = text;
		this.size = size;
	}


	WorldImage getAnim() {
		return new TextImage(text, size, new Color((float) c.getRed() / 255, (float) c.getGreen() / 255, (float) c.getBlue() / 255, ((float) c.getAlpha() / 255) * (1f - step.apply(this.getCurrent()))));
	}
}
