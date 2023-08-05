import { Color, OutlineMode, OverlayImage, RectangleImage } from 'impworld';

export function outlineFadeToBlack(c: Color, size: number) {
  const c1 = c.times(0.8);
  const c2 = c.times(0.6);
  const c3 = c.times(0.4);
  const c4 = c.times(0.2);

  const sizenew = Math.round(size * 0.1);
  return new OverlayImage(
    new RectangleImage(size - sizenew * 4, size - sizenew * 4, OutlineMode.SOLID, c),
    new OverlayImage(
      new RectangleImage(size - sizenew * 3, size - sizenew * 3, OutlineMode.SOLID, c1),
      new OverlayImage(
        new RectangleImage(size - sizenew * 2, size - sizenew * 2, OutlineMode.SOLID, c2),
        new OverlayImage(
          new RectangleImage(size - sizenew, size - sizenew, OutlineMode.SOLID, c3),
          new RectangleImage(size, size, OutlineMode.SOLID, c4),
        ),
      ),
    ),
  );
}
