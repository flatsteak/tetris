package tetris;

import java.awt.Color;

import javalib.worldimages.*;

public class Theme {
	WorldImage i;
	WorldImage s;
	WorldImage z;
	WorldImage j;
	WorldImage l;
	WorldImage o;
	WorldImage t;
	WorldImage cheese;
	WorldImage empty;
	WorldImage holdbox;
	
	Theme(WorldImage i, WorldImage s, WorldImage z, WorldImage j, WorldImage l, WorldImage o, WorldImage t, WorldImage cheese, WorldImage empty, WorldImage holdbox) {
		this.i = i;
		this.s = s;
		this.z = z;
		this.j = j;
		this.l = l;
		this.o = o;
		this.t = t;
		this.cheese = cheese;
		this.empty = empty;
		this.holdbox = holdbox;
	}
}

class ThemePool {
	static Theme DEFAULT_THEME = new Theme(
			new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, new Color(79, 46, 223)), // i
			new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.GREEN), // s
			new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.RED), // z
			new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.BLUE), // j
			new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.ORANGE), // l
			new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.YELLOW), // o
			new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, new Color(138, 43, 226)), // t
			new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.LIGHT_GRAY), // cheese
			new OverlayImage( // empty
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.BLACK)),
			new OverlayImage(
					new RectangleImage(Board.CELL_SIZE * 4, Board.CELL_SIZE * 3, OutlineMode.OUTLINE, Color.WHITE),
					new RectangleImage(Board.CELL_SIZE * 4, Board.CELL_SIZE * 3, OutlineMode.SOLID, Color.BLACK)));
	
	static Theme OUTLINE_THEME = new Theme(
			new OverlayImage( // i
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, new Color(79, 46, 223))),
			new OverlayImage( // s
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.GREEN)),
			new OverlayImage( // z
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.RED)),
			new OverlayImage( // j
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.BLUE)),
			new OverlayImage( // l
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.ORANGE)),
			new OverlayImage( // o
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.YELLOW)),
			new OverlayImage( // t
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, new Color(138, 43, 226))),
			new OverlayImage( // cheese
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.LIGHT_GRAY)),
			new OverlayImage( // empty
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
					new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.BLACK)),
			new OverlayImage(
					new RectangleImage(Board.CELL_SIZE * 4, Board.CELL_SIZE * 3, OutlineMode.OUTLINE, Color.WHITE),
					new RectangleImage(Board.CELL_SIZE * 4, Board.CELL_SIZE * 3, OutlineMode.SOLID, Color.BLACK)));
			
}
