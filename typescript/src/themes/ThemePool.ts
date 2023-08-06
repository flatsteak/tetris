import { CELL_SIZE } from '@/constants';
import { Theme } from '@/themes/Theme';
import { outlineFadeToBlack } from '@/util/outlineFadeToBlack';
import { Color, OutlineMode, OverlayImage, RectangleImage } from 'impworld';

export class ThemePool {
  static DEFAULT_THEME = new Theme(
    new RectangleImage(
      CELL_SIZE,
      CELL_SIZE,
      OutlineMode.SOLID,
      Color.BLUE.brighter().brighter(),
    ), // i
    new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GREEN), // s
    new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.RED), // z
    new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.BLUE), // j
    new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.ORANGE), // l
    new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.YELLOW), // o
    new RectangleImage(
      CELL_SIZE,
      CELL_SIZE,
      OutlineMode.SOLID,
      new Color(138, 43, 226),
    ), // t
    new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.LIGHT_GRAY), // cheese
    new OverlayImage( // empty
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(
        CELL_SIZE * 4,
        CELL_SIZE * 3,
        OutlineMode.OUTLINE,
        Color.WHITE,
      ),
      new RectangleImage(CELL_SIZE * 5, CELL_SIZE * 3.5, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY),
    ),
  );

  static OUTLINE_THEME: Theme = new Theme(
    new OverlayImage( // i
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(
        CELL_SIZE,
        CELL_SIZE,
        OutlineMode.SOLID,
        Color.BLUE.brighter().brighter(),
      ),
    ),
    new OverlayImage( // s
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GREEN),
    ),
    new OverlayImage( // z
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.RED),
    ),
    new OverlayImage( // j
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.BLUE),
    ),
    new OverlayImage( // l
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.ORANGE),
    ),
    new OverlayImage( // o
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.YELLOW),
    ),
    new OverlayImage( // t
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(
        CELL_SIZE,
        CELL_SIZE,
        OutlineMode.SOLID,
        new Color(138, 43, 226),
      ),
    ),
    new OverlayImage( // cheese
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.LIGHT_GRAY),
    ),
    new OverlayImage( // empty
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(
        CELL_SIZE * 5,
        CELL_SIZE * 3.5,
        OutlineMode.OUTLINE,
        Color.WHITE,
      ),
      new RectangleImage(CELL_SIZE * 4, CELL_SIZE * 3, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY),
    ),
  );

  static FADE_THEME: Theme = new Theme(
    outlineFadeToBlack(Theme.SKYBLUE, CELL_SIZE),
    outlineFadeToBlack(Color.GREEN, CELL_SIZE),
    outlineFadeToBlack(Color.RED, CELL_SIZE),
    outlineFadeToBlack(Color.BLUE, CELL_SIZE),
    outlineFadeToBlack(Color.ORANGE, CELL_SIZE),
    outlineFadeToBlack(Color.YELLOW, CELL_SIZE),
    outlineFadeToBlack(Theme.PURPLE, CELL_SIZE),
    outlineFadeToBlack(Color.GRAY, CELL_SIZE),
    new OverlayImage( // empty
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(
        CELL_SIZE,
        CELL_SIZE,
        OutlineMode.SOLID,
        new Color(0, 0, 0, 200),
      ),
    ),
    new OverlayImage(
      new RectangleImage(
        CELL_SIZE * 5,
        CELL_SIZE * 3.5,
        OutlineMode.OUTLINE,
        Color.WHITE,
      ),
      new RectangleImage(CELL_SIZE * 4, CELL_SIZE * 3, OutlineMode.SOLID, Color.BLACK),
    ),
    new OverlayImage(
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY),
    ),
  );
}
