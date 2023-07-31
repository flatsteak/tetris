import { Residue } from '@/pieces/Residue';
import { Tetrimino } from '@/pieces/Tetrimino';

export function tetriminoToResidue(t: Tetrimino) {
  switch (t) {
    case Tetrimino.S:
      return Residue.S;
    case Tetrimino.Z:
      return Residue.Z;
    case Tetrimino.J:
      return Residue.J;
    case Tetrimino.L:
      return Residue.L;
    case Tetrimino.O:
      return Residue.O;
    case Tetrimino.T:
      return Residue.T;
    case Tetrimino.I:
      return Residue.I;
    default:
      return Residue.EMPTY;
  }
}
