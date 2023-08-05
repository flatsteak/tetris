import { Color, OutlineMode, RectangleImage } from 'impworld';

export class SingerBot {
  getSinger() {
    return new RectangleImage(10, 10, OutlineMode.SOLID, Color.RED);
  }
}
