import { Queue } from '@/board';
import { APiece } from '@/pieces/APiece';
import { Residue } from '@/pieces/Residue';
import { Tetrimino } from '@/pieces/Tetrimino';
import { ThemePool } from '@/themes/ThemePool';
import { Posn, WorldImage } from 'impworld';

export class Board {
  height: number;
  width: number;
  residue: Residue[][];
  currentcombo: number;
  b2b: number;
  t: ThemePool;
  queue: Tetrimino[];
  fallingpiece: APiece;
  hold: Tetrimino | undefined;
  pieceplaced: boolean;
  garbage: GarbageMeter;
  ornaments: [WorldImage, Posn][];
  anims: Animation[];
  bgimage: WorldImage;
  spin: boolean;
  gameovertrigger: boolean;

  static CELL_SIZE = 20;

  constructor() {
    this.height = 20;
    this.width = 10;
    this.residue = [];
    for (let i = 0; i < this.height; i++) {
      this.residue.push(this.newEmptyRow());
    }
    this.currentcombo = 0;
    this.b2b = 0;
    this.t = ThemePool.FADE_THEME;
    this.queue = Queue.sevenBag();
    this.queue.push(...Queue.sevenBag());
    this.fallingpiece = this.pullFromBag(this.queue);
    this.hold = undefined;
    this.pieceplaced = false;
    this.garbage = new GarbageMeter();
    this.ornaments = [];
    this.anims = [];
    this.bgimage = FilePaths.BGSTARRY;
    this.spin = false;
    this.gameovertrigger = false;
  }

  private newEmptyRow(): Residue[] {
    const row: Residue[] = [];
    for (let i = 0; i < this.width; i++) {
      row.push(Residue.EMPTY);
    }
    return row;
  }

  private pullFromBag(queue: Tetrimino[]): APiece {
    const piece = queue.shift()!;
    return piece.newPiece();
  }
}
