package tetris;

import java.awt.Color;
import java.util.*;

import javalib.worldimages.*;

class Player {
	Enchant enchant;
	List<AbilitySlot> abilities;
	List<Ability> active;
	Special special;
	
	Player(Enchant enchant, List<AbilitySlot> abilities, Special special) {
		this.enchant = enchant;
		this.abilities = abilities;
		if (abilities.size() > 3) {
			throw new IllegalArgumentException("More than 3 abilities not allowed on a single build");
			
		}
		for (int i = 0; i > abilities.size(); i++) {
			List<String> tocheck = new ArrayList<>();
			if (tocheck.contains(abilities.get(i).key)) {
				throw new IllegalArgumentException("More than 1 ability assigned to the same keybind");
			}
		}
		this.special = special;
		this.active = new ArrayList<>();
	}
	
	static Player DEFAULT_BUILD = new Player(new Enchant(), new ArrayList<>(), new TSpinBonus());
}

class Enchant {
	List<Tetrimino> pieces;
	IFunc<Integer> effect;
	
	Enchant(List<Tetrimino> pieces, IFunc<Integer> effect) {
		this.pieces = pieces;
		this.effect = effect;
	}
	
	Enchant() {
		this.pieces = new ArrayList<>();
		this.effect = a -> a;
	}
	
	int getAttack(int attack, APiece p) {
		return (pieces.contains(p.identity))? effect.apply(attack) : attack;
	}
}

class AbilitySlot {
	String key; // x, c, v
	Ability ability;
	
	AbilitySlot(String key, Ability a) {
		this.key = key;
		this.ability = a;
	}
	

}
abstract class Ability { // Abilities last one piece
	int cost;
	
	Ability() {
		this.cost = 0;
	}

	abstract int effect(GameState g, int attack, Tetrimino lastplaced);

	abstract WorldImage draw();
	
}

class ShieldI extends Ability {

	int effect(GameState g, int attack, Tetrimino lastplaced) {
		if (lastplaced.equals(Tetrimino.I) && g.board.garbage.garbage > 0) {
			return attack * 2;
		}
		return attack;
	}

	WorldImage draw() {
		return null;
	}
	
}

abstract class Special { // Specials last until you have sent more lines than what you had as shield, specials cannot be activated unless you have shield
	
	int cost;
	int drain;
	boolean active;
	
	Special() {
		this.cost = 5;
		this.drain = 1;
	}
	
	void activate(GameState g, Tetrimino lastplaced, boolean spin) {
		if (g.board.garbage.garbage <= -cost) {
			this.active = true;
			
		}
	}
	abstract int effect(int attack, GameState g, Tetrimino lastplaced, boolean spin);
	
	WorldImage draw() {
		return FilePaths.SPECIAL_PLACEHOLDER;
	}
}


class TSpinBonus extends Special {
	
	TSpinBonus() {
		this.cost = 4;
		this.drain = 2;
	}

	int effect(int attack, GameState g, Tetrimino lastplaced, boolean spin) {
		if (lastplaced.equals(Tetrimino.T) && spin && active) {
			g.board.garbage.garbage += this.cost;
			if (g.board.garbage.garbage >= 0) {
				this.active = false;
				if (g.board.chargemeter.power > 0) {
					System.out.println("sent & anim");
					Animation a = new FadingCircleSizeAnimation(500, AnimateValue.OUTSIN, 40, Color.RED, DifficultyPool.singerpos, 450);
					a.start();
					g.board.anims.add(a);
					Animation sent = new FadingTextAnimation(500, 13, Theme.PURPLE, g.board.chargemeter.power + "", i -> i);
					sent.start(DifficultyPool.singerpos);
					g.board.anims.add(sent);
					
				}
				g.board.chargemeter.send(g);
			}
			return (int) (attack * 1.5);
		}
		this.active = false;
		if (g.board.chargemeter.power > 0) {
			System.out.println("sent & anim: effect not run");
			System.out.println("sent & anim");
			Animation a = new FadingCircleSizeAnimation(500, AnimateValue.OUTSIN, 40, Color.RED, DifficultyPool.singerpos, 450);
			a.start();
			g.board.anims.add(a);
			Animation sent = new FadingTextAnimation(500, 13, Theme.PURPLE, g.board.chargemeter.power + "", i -> i);
			sent.start(DifficultyPool.singerpos);
			g.board.anims.add(sent);
		}
		g.board.chargemeter.send(g);
		return attack;
	}
	
	WorldImage draw() {
		if (active) {
			return FilePaths.A_S_CYCLONE;
		}
		return FilePaths.S_CYCLONE;
	}
	
}


class NormalPieceBonus extends Special {
	
	NormalPieceBonus() {
		this.cost = 7;
		this.drain = 2;
	}
	
	int effect(int attack, GameState g, Tetrimino lastplaced, boolean spin) {
		if (lastplaced.equals(Tetrimino.T) || lastplaced.equals(Tetrimino.I)) {
			return attack;
		}
		return attack + g.board.currentcombo;
	}
	
}

enum SendingAttackType {
	SENDDEFENSE, SENDATTACK
}

enum RecievingAttackType {
	RECIEVEVS, RECIEVESURVIVAL
}

enum CostType {
	SHIELD
}