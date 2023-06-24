package tetris;

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
		case VS: return g.board.residue.size() > g.board.height + 2;
		case LINES: return g.stats.lines >= amount;
		}
		return false;
	}
	
	SingerBot getBot(int aps, int hp) {
		switch (type) {
		case VS: return new VSingerBot(aps, hp);
		case SURVIVAL: return new HostileSingerBot(aps);
		default: return new FriendlySingerBot();
		}
	}
}
