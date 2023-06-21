package tetris;

import java.util.*;

class Queue {
	
	static List<Tetrimino> BAG = new ArrayList<Tetrimino>(Arrays.asList(
			Tetrimino.S,
			Tetrimino.Z,
			Tetrimino.L,
			Tetrimino.J,
			Tetrimino.T,
			Tetrimino.I,
			Tetrimino.O));
	
	static List<Tetrimino> sevenBag() {
		List<Tetrimino> toreturn = new ArrayList<>();
		List<Tetrimino> bag = new ArrayList<>();
		bag.addAll(Queue.BAG);
		for (int i = 7; i > 1; i--) {			
			int rngidx = new Random().nextInt(bag.size());
			toreturn.add(bag.get(rngidx));
			bag.remove(rngidx);
		}
		return toreturn;
	}
}

enum Tetrimino {
	S, Z, I, J, L, O, T;
}

