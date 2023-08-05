import { Color, WorldImage } from 'impworld';

export class Theme {
  static SKYBLUE: Color = new Color(135, 206, 235);
  static PURPLE: Color = new Color(148, 0, 211);

  constructor(
    public readonly i: WorldImage,
    public readonly s: WorldImage,
    public readonly z: WorldImage,
    public readonly j: WorldImage,
    public readonly l: WorldImage,
    public readonly o: WorldImage,
    public readonly t: WorldImage,
    public readonly cheese: WorldImage,
    public readonly empty: WorldImage,
    public readonly holdbox: WorldImage,
    public readonly shadow: WorldImage,
  ) {}
}
