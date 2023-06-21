package tetris;

import java.util.Map;

class AttackTable {
	static int B2B_MAX = 6;
	static int B2B_TOP = 24;
	static int B2B_MID = 13;
	static int B2B_LOW = 6;
	static Map<Double<Integer, Boolean>, Integer> table = Map.of(
			new Double<Integer, Boolean>(1, false), 0,
			new Double<Integer, Boolean>(2, false), 1,
			new Double<Integer, Boolean>(3, false), 2,
			new Double<Integer, Boolean>(4, false), 4,
			
			new Double<Integer, Boolean>(1, true), 2,
			new Double<Integer, Boolean>(2, true), 4,
			new Double<Integer, Boolean>(3, true), 6);

	
	static int applyCombo(int combo, int b2b, Double<Integer, Boolean> clear) {
		return (int) (table.get(clear) * Math.max(Math.floor(combo / 5), 10)) + AttackTable.b2bFactor(b2b);
	}
	
	static int b2bFactor(int b2b) {
		if (b2b > B2B_TOP) {
			return B2B_MAX;
		} else if (b2b > B2B_MID) {
			return 5;
		} else if (b2b > B2B_LOW) {
			return 3;
		} else if (b2b > 1) {
			return 2;
		}
		return 0;
	}
}