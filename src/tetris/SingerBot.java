package tetris;

import java.util.*;

class SingerBot {
	String name;
	List<String> speechpool;
	
}

class HostileSingerBot {
	int aps; // attack per second
	
	void makeMove(double time, Board b) {
		if (time == 0.0) {
			b.addCheese(aps);
		}
	}
	
	HostileSingerBot(int aps) {
		this.aps = aps;
	}
}

class VSingerBot {
	int aps; // attack per second
	int atkleft; // lines left to send
	
	int garbagequeue; // lines it is going to recieve after a move
	int health; // lines sent before victory
	double cancel; // chance to cancel attacks
	
	void makeMove(double time, Board b) {
		if (garbagequeue > 0) {
			if (new Random().nextDouble(1.0) > 1 - cancel) {
				if (atkleft > garbagequeue) {
					this.atkleft -= garbagequeue;
					garbagequeue = 0;
				} else {
					this.garbagequeue -= atkleft;
					this.atkleft = 0;
				}
			}
		} else if (time == 0.0) {
			b.addCheese(atkleft); 
			this.atkleft = aps;
		}
	}
	
	VSingerBot(int aps, int hp) {
		this.aps = aps;
		this.health = hp;
	}
}