import { Posn } from 'impworld';
import { APiece } from './APiece';
import { CycleIndex } from './CycleIndex';
import { Tetrimino } from './Tetrimino';
import { Board } from '@/board';

export class JPiece extends APiece {
  static JPIECE_UP: boolean[][] = [
    [true, false, false, false],
    [true, true, true, false],
    [false, false, false, false],
    [false, false, false, false],
  ];
  static JPIECE_LEFT: boolean[][] = [
    [false, true, false, false],
    [false, true, false, false],
    [true, true, false, false],
    [false, false, false, false],
  ];
  static JPIECE_RIGHT: boolean[][] = [
    [false, true, true, false],
    [false, true, false, false],
    [false, true, false, false],
    [false, false, false, false],
  ];
  static JPIECE_DOWN: boolean[][] = [
    [false, false, false, false],
    [true, true, true, false],
    [false, false, true, false],
    [false, false, false, false],
  ];

  constructor(posn: Posn) {
    super(
      posn,
      new CycleIndex(
        JPiece.JPIECE_UP.slice(),
        JPiece.JPIECE_LEFT.slice(),
        JPiece.JPIECE_RIGHT.slice(),
        JPiece.JPIECE_DOWN.slice(),
      ),
      Tetrimino.J,
    );
  }

  public hasSpun(b: Board): boolean {
    return false;
  }

  public rotInitialState(s: string): boolean[][] {
    switch (s) {
      case 'up':
        return JPiece.JPIECE_UP;
      case 'left':
        return JPiece.JPIECE_LEFT;
      case 'right':
        return JPiece.JPIECE_RIGHT;
      default:
        return JPiece.JPIECE_DOWN;
    }
  }
}
