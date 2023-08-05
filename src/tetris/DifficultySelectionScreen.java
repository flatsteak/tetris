package tetris;

import java.awt.Color;
import java.util.*;

import javalib.worldimages.*;



class BotButton {
	SingerBot bot;
	boolean active;
	
	Posn bound1;
	Posn bound2;
	
	BotButton(SingerBot bot) {
		this.bot = bot;
		this.active = false;
	}
	
	void click(GameState g) {
		if (active) {
			g.selectionscreenup = false;
			g.bot = this.bot;
		} else {
			active = true;
		}
	}
	
	WorldImage draw() {
		
		WorldImage bottext = new TextImage(bot.getBotName(), Color.BLACK);
		WorldImage button = new OverlayImage(new RectangleImage((int) (bottext.getWidth() * 1.1), (int) (Board.CELL_SIZE), OutlineMode.OUTLINE, Color.BLACK), bottext);
		if (active) {
			button = new OverlayImage(button, new RectangleImage((int) (bottext.getWidth() * 1.1), (int) (Board.CELL_SIZE), OutlineMode.SOLID, Color.GRAY));
		}
		return button;
	}
	
	void off() {
		this.active = false;
	}
	
}

class DifficultySelectionScreen {
	List<BotButton> selections;
	boolean active;
	
	
	static DifficultySelectionScreen DEFAULT_SELECTION = new DifficultySelectionScreen(DifficultyPool.DEFAULT_BOTS);
	
	DifficultySelectionScreen(List<BotButton> selections, boolean active) {
		this.selections = selections;
		this.active = active;
	}
	
	DifficultySelectionScreen(List<SingerBot> bots) {
		this.selections = bots.stream()
				.map(bot -> new BotButton(bot))
				.toList();
		this.active = false;
	}
	
	void placeAllButtons(Posn p) {
		for (int i = 0; i < selections.size(); i++) {
			BotButton b = this.selections.get(i);
			double width = b.draw().getWidth();
			b.bound1 = new Posn(p.x, p.y + Board.CELL_SIZE * i);
			b.bound2 = new Posn(p.x + (int) width, p.y + Board.CELL_SIZE * (i + 1)); // cell size is draw height in draw()
		}
	}
	
	WorldImage drawAllSelections() {
		return selections.stream()
				.map(sel -> sel.draw())
				.reduce((a, b) -> new AboveImage(a, b)).get();
	}
	
	void findClickedButton(Posn p, GameState g) {
		for (int i = 0; i < selections.size(); i++) {
			BotButton current = this.selections.get(i);
			if (p.x > current.bound1.x && p.x < current.bound2.x && p.y > current.bound1.y && p.y < current.bound2.y) {
				for (int j = 0; j < selections.size(); j++) {
					selections.get(j).off();
				}
				current.active = true;
				current.click(g);
				return;
			}
			
		}
		for (int j = 0; j < selections.size(); j++) {
			selections.get(j).off();
		}
	}
	
}
