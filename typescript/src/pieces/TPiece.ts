import { Board } from '@/board';
import { APiece } from '@/pieces/APiece';
import { CycleIndex } from '@/pieces/CycleIndex';
import { Residue } from '@/pieces/Residue';
import { Tetrimino } from '@/pieces/Tetrimino';
import { Posn } from 'impworld';

export class TPiece extends APiece {
  static TPIECE_UP: boolean[][] = [
    [false, true, false, false], // *
    [true, true, true, false], //***
    [false, false, false, false],
    [false, false, false, false],
  ];
  static TPIECE_DOWN: boolean[][] = [
    [false, false, false, false], //
    [true, true, true, false], // ***
    [false, true, false, false], //  *
    [false, false, false, false],
  ];
  static TPIECE_RIGHT: boolean[][] = [
    [false, true, false, false], // *
    [false, true, true, false], // **
    [false, true, false, false], // *
    [false, false, false, false],
  ];
  static TPIECE_LEFT: boolean[][] = [
    [false, true, false, false], // *
    [true, true, false, false], //**
    [false, true, false, false], // *
    [false, false, false, false],
  ];

  constructor(posn: Posn) {
    super(
      posn,
      new CycleIndex(
        TPiece.TPIECE_UP.slice(),
        TPiece.TPIECE_LEFT.slice(),
        TPiece.TPIECE_RIGHT.slice(),
        TPiece.TPIECE_DOWN.slice(),
      ),
      Tetrimino.T,
    );
  }
  public hasSpun(b: Board): boolean {
    const top: Residue[] = b.residue[this.position.y];
    const bottom: Residue[] =
      this.position.y + 2 >= 20 ? b.newEmptyRow() : b.residue[this.position.y + 2];
    if (this.position.x + 2 > 9 || this.position.x < 0) {
      return false;
    }
    const topBlockPresent: boolean =
      top[this.position.x] !== Residue.EMPTY || top[this.position.x + 2] !== Residue.EMPTY;
    console.log(topBlockPresent);
    const bottomBlockPresent: boolean =
      bottom[this.position.x] !== Residue.EMPTY && bottom[this.position.x + 2] !== Residue.EMPTY;
    console.log(bottomBlockPresent);
    return topBlockPresent && bottomBlockPresent;
  }

  public rotInitialState(s: string): boolean[][] {
    switch (s) {
      case 'up':
        return TPiece.TPIECE_UP;
      case 'left':
        return TPiece.TPIECE_LEFT;
      case 'right':
        return TPiece.TPIECE_RIGHT;
      default:
        return TPiece.TPIECE_DOWN;
    }
  }
}
