package tetris;

import java.awt.Color;
import java.util.*;

import javalib.impworld.*;
import javalib.worldimages.*;

abstract class Animation {
	WorldImage img;
	int start; // ms
	int end; // ms
	int current; // ms
	Posn posn;
	
	Animation(WorldImage img, int start, int end, int cur, Posn p) {
		this.img = img;
		this.start = start;
		this.end = end; 
		this.current = cur;
		this.posn = p;
	}
	
	abstract WorldImage getAnim();
	
	void tick() {
		this.current = (int) System.currentTimeMillis();
	}
}

class Flashwave extends Animation {
	int radius;
	static int SIZE = 60;
	static int SPEED = 1; // in ticks (ms)
	static double RATE = 1.2; // px / tickspeed
	
	Flashwave(Posn p) {
		super(new CircleImage(SIZE, OutlineMode.OUTLINE, Color.WHITE), (int) System.currentTimeMillis(), (int) (System.currentTimeMillis() + 5000), (int) System.currentTimeMillis(), p);
	}

	WorldImage getAnim() {
		List<Integer> todraw = new ArrayList<>();
		for (int i = - SIZE; i < SIZE; i++) {
			todraw.add(i);
		}
		return todraw.stream()
				.map(j -> new CircleImage(Math.max(j + (int) ((current - start) * RATE / SPEED), 0), OutlineMode.OUTLINE, new Color(Math.max(0, 255 - Math.abs(j)), Math.max(0, 255 - Math.abs(j)), Math.max(0, 255 - Math.abs(j)), Math.max(0, 255 - Math.abs(j * 5)))))
				.reduce((WorldImage) new RectangleImage(0, 0, OutlineMode.OUTLINE, Color.BLACK), (curr, next) -> new OverlayImage(curr, next), (a, b) -> new OverlayImage(a, b));
	}
}