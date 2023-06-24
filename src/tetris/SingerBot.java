package tetris;

import java.util.*;

interface SingerBot {
	void makeMove(double time, Board b);
}

class FriendlySingerBot implements SingerBot {
	String name;
	List<String> speechpool;
	
	
	
	public void makeMove(double time, Board b) {
		speechpool.get(new Random().nextInt(speechpool.size() - 1));
	}
}

class HostileSingerBot implements SingerBot {
	int aps; // attack per second
	
	public void makeMove(double time, Board b) {
		if (time == 0.0) {
			b.addCheese(aps);
		}
	}
	
	HostileSingerBot(int aps) {
		this.aps = aps;
	}
}

class VSingerBot implements SingerBot {
	int attack; // in lines
	int rate; // in ms
	int atkleft; // lines left to send this cycle
	
	int garbagequeue; // lines it is going to recieve after a move
	int health; // lines sent before victory
	boolean cancel;
	
	public void makeMove(double time, Board b) {
		if (garbagequeue > 0) {
			if (cancel) {
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
			this.atkleft = attack;
		}
	}
	
	VSingerBot(int aps, int hp) {
		this.attack = aps;
		this.atkleft = aps;
		this.health = hp;
		this.cancel = true;
	}
}