import { Board } from '@/board';
import { APiece } from '@/pieces/APiece';
import { CycleIndex } from '@/pieces/CycleIndex';
import { Tetrimino } from '@/pieces/Tetrimino';
import { Posn } from 'impworld';

export class SPiece extends APiece {
  static SPIECE_UP: boolean[][] = [
    [false, true, true, false],
    [true, true, false, false],
    [false, false, false, false],
    [false, false, false, false],
  ];
  static SPIECE_LEFT: boolean[][] = [
    [true, false, false, false],
    [true, true, false, false],
    [false, true, false, false],
    [false, false, false, false],
  ];
  static SPIECE_RIGHT: boolean[][] = [
    [false, true, false, false],
    [false, true, true, false],
    [false, false, true, false],
    [false, false, false, false],
  ];
  static SPIECE_DOWN: boolean[][] = [
    [false, false, false, false],
    [false, true, true, false],
    [true, true, false, false],
    [false, false, false, false],
  ];

  constructor(posn: Posn) {
    super(
      posn,
      new CycleIndex(
        SPiece.SPIECE_UP.slice(),
        SPiece.SPIECE_LEFT.slice(),
        SPiece.SPIECE_RIGHT.slice(),
        SPiece.SPIECE_DOWN.slice(),
      ),
      Tetrimino.S,
    );
  }

  hasSpun(b: Board): boolean {
    return false;
  }

  rotInitialState(s: string): boolean[][] {
    switch (s) {
      case 'up':
        return SPiece.SPIECE_UP;
      case 'left':
        return SPiece.SPIECE_LEFT;
      case 'right':
        return SPiece.SPIECE_RIGHT;
      default:
        return SPiece.SPIECE_DOWN;
    }
  }
}
