package tetris;

import java.awt.Color;

import javalib.funworld.WorldScene;
import javalib.worldimages.*;

enum RuleType {
	LINES, TIME, SCORE, VS, SURVIVAL
}

public class Ruleset {
	RuleType type;
	int amount;

	Ruleset(RuleType t, int a) {
		this.type = t;
		this.amount = a;
	}

	public boolean gameOver(GameState g) {
		switch(this.type) {
		case TIME: return System.currentTimeMillis() - g.stats.starttime >= amount;
		case SCORE: return g.stats.score >= amount;
		case SURVIVAL: return g.board.residue.size() > g.board.height + 2;
		case LINES: return g.stats.lines >= amount;
		case VS: return g.board.residue.stream().filter(residue -> !TetrisUtil.containsAll(residue, Residue.EMPTY)).toList().size() > g.board.height + 2;
		}
		return false;
	}
	
	public WorldScene lastScene(GameState g) {
		WorldScene s = new WorldScene(500, 500);
		switch (type) {
		case LINES: s.placeImageXY(new TextImage(amount + " LINES : " + new TimeMeter((int) (System.currentTimeMillis() - g.stats.starttime)).getMeterVal(), Color.BLACK), 250, 250); break;
		case VS: s.placeImageXY(new TextImage("" + g.board.residue.size(), Color.BLACK), 250, 250);
		}
		return s;
	}
	
	SingerBot getBot(int aps, int hp) {
		switch (type) {
		case VS: return new VSingerBot(aps, 1000, hp);
		case SURVIVAL: return new HostileSingerBot(aps);
		default: return new FriendlySingerBot();
		}
	}
}
