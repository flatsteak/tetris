export class GameStats {
  constructor(
    public lines: number = 0,
    public score: number = 0,
    public atk: number = 0,
    public starttime: number = Date.now() + 1,
    public decostarttime: number = Date.now(),
    public pieces: number = 0,
  ) {}
}
