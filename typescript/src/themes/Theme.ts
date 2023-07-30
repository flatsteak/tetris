import { Color, WorldImage } from 'impworld';

export class Theme {
  static SKYBLUE: Color = new Color(135, 206, 235);
  static PURPLE: Color = new Color(148, 0, 211);
  i: WorldImage;
  s: WorldImage;
  z: WorldImage;
  j: WorldImage;
  l: WorldImage;
  o: WorldImage;
  t: WorldImage;
  cheese: WorldImage;
  empty: WorldImage;
  holdbox: WorldImage;
  shadow: WorldImage;

  constructor(
    i: WorldImage,
    s: WorldImage,
    z: WorldImage,
    j: WorldImage,
    l: WorldImage,
    o: WorldImage,
    t: WorldImage,
    cheese: WorldImage,
    empty: WorldImage,
    holdbox: WorldImage,
    shadow: WorldImage,
  ) {
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
    this.shadow = shadow;
  }
}
