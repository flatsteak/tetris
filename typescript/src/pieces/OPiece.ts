import { Posn } from 'impworld';
import { APiece } from './APiece';
import { CycleIndex } from './CycleIndex';
import { Tetrimino } from './Tetrimino';
import { Board } from '@/board';

const OPIECE_UP: boolean[][] = [
  [true, true, false, false],
  [true, true, false, false],
  [false, false, false, false],
  [false, false, false, false],
];

const OPIECE_COPY: boolean[][] = [...OPIECE_UP];

const OPIECE_ROTATIONS: CycleIndex = new CycleIndex(
  OPIECE_COPY,
  OPIECE_COPY,
  OPIECE_COPY,
  OPIECE_COPY,
);

export class OPiece extends APiece {
  constructor(posn: Posn) {
    super(posn, OPIECE_ROTATIONS, Tetrimino.O);
  }

  public hasSpun(b: Board): boolean {
    return false;
  }

  public rotInitialState(s: string): boolean[][] {
    return OPIECE_UP;
  }
}
