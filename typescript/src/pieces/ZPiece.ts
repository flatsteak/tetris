import { Posn } from 'impworld';
import { APiece } from './APiece';
import { CycleIndex } from './CycleIndex';
import { Tetrimino } from './Tetrimino';
import { Board } from '@/board';

export class ZPiece extends APiece {
  static ZPIECE_UP: boolean[][] = [
    [true, true, false, false],
    [false, true, true, false],
    [false, false, false, false],
    [false, false, false, false],
  ];
  static ZPIECE_LEFT: boolean[][] = [
    [false, true, false, false],
    [true, true, false, false],
    [true, false, false, false],
    [false, false, false, false],
  ];
  static ZPIECE_RIGHT: boolean[][] = [
    [false, false, true, false],
    [false, true, true, false],
    [false, true, false, false],
    [false, false, false, false],
  ];
  static ZPIECE_DOWN: boolean[][] = [
    [false, false, false, false],
    [true, true, false, false],
    [false, true, true, false],
    [false, false, false, false],
  ];

  constructor(posn: Posn) {
    super(
      posn,
      new CycleIndex(
        ZPiece.ZPIECE_UP.slice(),
        ZPiece.ZPIECE_LEFT.slice(),
        ZPiece.ZPIECE_RIGHT.slice(),
        ZPiece.ZPIECE_DOWN.slice(),
      ),
      Tetrimino.Z,
    );
  }

  public hasSpun(b: Board): boolean {
    return false;
  }

  public rotInitialState(s: string): boolean[][] {
    switch (s) {
      case 'up':
        return ZPiece.ZPIECE_UP;
      case 'left':
        return ZPiece.ZPIECE_LEFT;
      case 'right':
        return ZPiece.ZPIECE_RIGHT;
      default:
        return ZPiece.ZPIECE_DOWN;
    }
  }
}
