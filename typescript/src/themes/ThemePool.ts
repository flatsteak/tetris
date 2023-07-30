import { Board } from '@/board';
import { Theme } from '@/themes/Theme';
import { outlineFadeToBlack } from '@/util/outlineFadeToBlack';
import { Color, OutlineMode, OverlayImage, RectangleImage } from 'impworld';

export class ThemePool {
  static DEFAULT_THEME = new Theme(
    new RectangleImage(
      Board.CELL_SIZE,
      Board.CELL_SIZE,
      OutlineMode.SOLID,
      Color.BLUE.brighter().brighter(),
    ), // i
    new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.GREEN), // s
    new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.RED), // z
    new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.BLUE), // j
    new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.ORANGE), // l
    new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.YELLOW), // o
    new RectangleImage(
      Board.CELL_SIZE,
      Board.CELL_SIZE,
      OutlineMode.SOLID,
      new Color(138, 43, 226),
    ), // t
    new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.LIGHT_GRAY), // cheese
    new OverlayImage( // empty
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(
        Board.CELL_SIZE * 4,
        Board.CELL_SIZE * 3,
        OutlineMode.OUTLINE,
        Color.WHITE,
      ),
      new RectangleImage(Board.CELL_SIZE * 4, Board.CELL_SIZE * 3, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.GRAY),
    ),
  );

  static OUTLINE_THEME: Theme = new Theme(
    new OverlayImage( // i
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(
        Board.CELL_SIZE,
        Board.CELL_SIZE,
        OutlineMode.SOLID,
        Color.BLUE.brighter().brighter(),
      ),
    ),
    new OverlayImage( // s
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.GREEN),
    ),
    new OverlayImage( // z
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.RED),
    ),
    new OverlayImage( // j
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.BLUE),
    ),
    new OverlayImage( // l
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.ORANGE),
    ),
    new OverlayImage( // o
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.YELLOW),
    ),
    new OverlayImage( // t
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(
        Board.CELL_SIZE,
        Board.CELL_SIZE,
        OutlineMode.SOLID,
        new Color(138, 43, 226),
      ),
    ),
    new OverlayImage( // cheese
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.LIGHT_GRAY),
    ),
    new OverlayImage( // empty
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(
        Board.CELL_SIZE * 4,
        Board.CELL_SIZE * 3,
        OutlineMode.OUTLINE,
        Color.WHITE,
      ),
      new RectangleImage(Board.CELL_SIZE * 4, Board.CELL_SIZE * 3, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.GRAY),
    ),
  );

  static FADE_THEME: Theme = new Theme(
    outlineFadeToBlack(Theme.SKYBLUE, Board.CELL_SIZE),
    outlineFadeToBlack(Color.GREEN, Board.CELL_SIZE),
    outlineFadeToBlack(Color.RED, Board.CELL_SIZE),
    outlineFadeToBlack(Color.BLUE, Board.CELL_SIZE),
    outlineFadeToBlack(Color.ORANGE, Board.CELL_SIZE),
    outlineFadeToBlack(Color.YELLOW, Board.CELL_SIZE),
    outlineFadeToBlack(Theme.PURPLE, Board.CELL_SIZE),
    outlineFadeToBlack(Color.GRAY, Board.CELL_SIZE),
    new OverlayImage( // empty
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(
        Board.CELL_SIZE,
        Board.CELL_SIZE,
        OutlineMode.SOLID,
        new Color(0, 0, 0, 200),
      ),
    ),
    new OverlayImage(
      new RectangleImage(
        Board.CELL_SIZE * 4,
        Board.CELL_SIZE * 3,
        OutlineMode.OUTLINE,
        Color.WHITE,
      ),
      new RectangleImage(Board.CELL_SIZE * 4, Board.CELL_SIZE * 3, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(Board.CELL_SIZE, Board.CELL_SIZE, OutlineMode.SOLID, Color.GRAY),
    ),
  );
}
