package tetris;

enum RuleType {
	LINES, TIME, SCORE, VS
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
		case TIME: return g.stats.time >= amount;
		case SCORE: return g.stats.score >= amount;
		case VS: return g.board.residue.size() > g.board.height + 2;
		case LINES: return g.stats.lines >= amount;
		}
		return false;
	}
}
