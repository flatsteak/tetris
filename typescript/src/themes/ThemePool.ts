import { GamePositions } from '@/GamePositions';
import { Theme } from '@/themes/Theme';
import { outlineFadeToBlack } from '@/util/outlineFadeToBlack';
import { Color, OutlineMode, OverlayImage, RectangleImage } from 'impworld';

export class ThemePool {
  static DEFAULT_THEME = (p: GamePositions) =>
    new Theme(
      new RectangleImage(
        p.cellSize,
        p.cellSize,
        OutlineMode.SOLID,
        Color.BLUE.brighter().brighter(),
      ), // i
      new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.GREEN), // s
      new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.RED), // z
      new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.BLUE), // j
      new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.ORANGE), // l
      new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.YELLOW), // o
      new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, new Color(138, 43, 226)), // t
      new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.LIGHT_GRAY), // cheese
      new OverlayImage( // empty
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.BLACK),
      ),
      new OverlayImage(
        new RectangleImage(p.cellSize * 4, p.cellSize * 3, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(p.cellSize * 5, p.cellSize * 3.5, OutlineMode.SOLID, Color.BLACK),
      ),
      new OverlayImage(
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.GRAY),
      ),
    );

  static OUTLINE_THEME = (p: GamePositions) =>
    new Theme(
      new OverlayImage( // i
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(
          p.cellSize,
          p.cellSize,
          OutlineMode.SOLID,
          Color.BLUE.brighter().brighter(),
        ),
      ),
      new OverlayImage( // s
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.GREEN),
      ),
      new OverlayImage( // z
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.RED),
      ),
      new OverlayImage( // j
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.BLUE),
      ),
      new OverlayImage( // l
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.ORANGE),
      ),
      new OverlayImage( // o
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.YELLOW),
      ),
      new OverlayImage( // t
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, new Color(138, 43, 226)),
      ),
      new OverlayImage( // cheese
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.BLACK),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.LIGHT_GRAY),
      ),
      new OverlayImage( // empty
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.BLACK),
      ),
      new OverlayImage(
        new RectangleImage(p.cellSize * 5, p.cellSize * 3.5, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(p.cellSize * 4, p.cellSize * 3, OutlineMode.SOLID, Color.BLACK),
      ),
      new OverlayImage(
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.GRAY),
      ),
    );

  static FADE_THEME = (p: GamePositions) =>
    new Theme(
      outlineFadeToBlack(Theme.SKYBLUE, p.cellSize),
      outlineFadeToBlack(Color.GREEN, p.cellSize),
      outlineFadeToBlack(Color.RED, p.cellSize),
      outlineFadeToBlack(Color.BLUE, p.cellSize),
      outlineFadeToBlack(Color.ORANGE, p.cellSize),
      outlineFadeToBlack(Color.YELLOW, p.cellSize),
      outlineFadeToBlack(Theme.PURPLE, p.cellSize),
      outlineFadeToBlack(Color.GRAY, p.cellSize),
      new OverlayImage( // empty
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, new Color(0, 0, 0, 200)),
      ),
      new OverlayImage(
        new RectangleImage(p.cellSize * 5, p.cellSize * 3.5, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(p.cellSize * 4, p.cellSize * 3, OutlineMode.SOLID, Color.BLACK),
      ),
      new OverlayImage(
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(p.cellSize, p.cellSize, OutlineMode.SOLID, Color.GRAY),
      ),
    );
}
