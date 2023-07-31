import { Board } from '@/board';
import { APiece } from '@/pieces/APiece';
import { CycleIndex } from '@/pieces/CycleIndex';
import { Tetrimino } from '@/pieces/Tetrimino';
import { Posn } from 'impworld';

export class LPiece extends APiece {
  static LPIECE_UP: boolean[][] = [
    [false, false, true, false],
    [true, true, true, false],
    [false, false, false, false],
    [false, false, false, false],
  ];

  static LPIECE_LEFT: boolean[][] = [
    [true, true, false, false],
    [false, true, false, false],
    [false, true, false, false],
    [false, false, false, false],
  ];

  static LPIECE_RIGHT: boolean[][] = [
    [false, true, false, false],
    [false, true, false, false],
    [false, true, true, false],
    [false, false, false, false],
  ];

  static LPIECE_DOWN: boolean[][] = [
    [false, false, false, false],
    [true, true, true, false],
    [true, false, false, false],
    [false, false, false, false],
  ];

  constructor(posn: Posn) {
    super(
      posn,
      new CycleIndex(
        LPiece.LPIECE_UP.slice(),
        LPiece.LPIECE_LEFT.slice(),
        LPiece.LPIECE_RIGHT.slice(),
        LPiece.LPIECE_DOWN.slice(),
      ),
      Tetrimino.L,
    );
  }

  public hasSpun(b: Board): boolean {
    return false;
  }

  public rotInitialState(s: string): boolean[][] {
    switch (s) {
      case 'up':
        return LPiece.LPIECE_UP;
      case 'left':
        return LPiece.LPIECE_LEFT;
      case 'right':
        return LPiece.LPIECE_RIGHT;
      default:
        return LPiece.LPIECE_DOWN;
    }
  }
}
