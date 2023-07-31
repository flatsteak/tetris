import { Board } from '@/board';
import { APiece } from '@/pieces/APiece';
import { CycleIndex } from '@/pieces/CycleIndex';
import { Rotation } from '@/pieces/Rotation';
import { Tetrimino } from '@/pieces/Tetrimino';
import { Posn } from 'impworld';

export class IPiece extends APiece {
  static IPIECE_UP: boolean[][] = [
    [false, false, false, false],
    [true, true, true, true],
    [false, false, false, false],
    [false, false, false, false],
  ];

  static IPIECE_LEFT: boolean[][] = [
    [false, true, false, false],
    [false, true, false, false],
    [false, true, false, false],
    [false, true, false, false],
  ];

  static IPIECE_RIGHT: boolean[][] = [
    [false, false, true, false],
    [false, false, true, false],
    [false, false, true, false],
    [false, false, true, false],
  ];

  static IPIECE_DOWN: boolean[][] = [
    [false, false, false, false],
    [false, false, false, false],
    [true, true, true, true],
    [false, false, false, false],
  ];

  constructor(posn: Posn) {
    super(
      posn,
      new CycleIndex(IPiece.IPIECE_UP, IPiece.IPIECE_LEFT, IPiece.IPIECE_RIGHT, IPiece.IPIECE_DOWN),
      Tetrimino.I,
    );
  }

  hasSpun(b: Board): boolean {
    return false;
  }

  rotInitialState(s: string): boolean[][] {
    switch (s) {
      case 'up':
        return IPiece.IPIECE_UP;
      case 'left':
        return IPiece.IPIECE_LEFT;
      case 'right':
        return IPiece.IPIECE_RIGHT;
      default:
        return IPiece.IPIECE_DOWN;
    }
  }

  getKickTests(r: Rotation): Posn[] {
    if (this.piece.first === this.rotInitialState('up')) {
      switch (r) {
        case Rotation.CLOCKWISE:
          return [new Posn(0, 0), new Posn(-2, 0), new Posn(1, 0), new Posn(-2, 1), new Posn(1, 2)];
        case Rotation.COUNTERCLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(-1, 0),
            new Posn(2, 0),
            new Posn(-1, 2),
            new Posn(2, -1),
          ];
        default:
          return [new Posn(0, 0)];
      }
    } else if (this.piece.first === this.rotInitialState('right')) {
      switch (r) {
        case Rotation.CLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(-1, 0),
            new Posn(2, 0),
            new Posn(-1, -2),
            new Posn(2, -1),
          ];
        case Rotation.COUNTERCLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(2, 0),
            new Posn(-1, 0),
            new Posn(2, 1),
            new Posn(-1, -2),
          ];
        default:
          return [new Posn(0, 0)];
      }
    } else if (this.piece.first === this.rotInitialState('left')) {
      switch (r) {
        case Rotation.CLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(1, 0),
            new Posn(-2, 0),
            new Posn(1, -2),
            new Posn(-2, 1),
          ];
        case Rotation.COUNTERCLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(-2, 0),
            new Posn(1, 0),
            new Posn(-2, -1),
            new Posn(1, 2),
          ];
        default:
          return [new Posn(0, 0)];
      }
    } else {
      switch (r) {
        case Rotation.CLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(2, 0),
            new Posn(-1, 0),
            new Posn(2, 1),
            new Posn(-1, -2),
          ];
        case Rotation.COUNTERCLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(1, 0),
            new Posn(-2, 0),
            new Posn(1, -2),
            new Posn(-2, 1),
          ];
        default:
          return [new Posn(0, 0)];
      }
    }
  }
}
