import { Posn } from 'impworld';
import { CycleIndex } from './CycleIndex';
import { Tetrimino } from './Tetrimino';
import { Board } from '@/board';
import { Rotation } from '@/pieces/Rotation';

export abstract class APiece {
  position: Posn;
  piece: CycleIndex;
  identity: Tetrimino;

  constructor(posn: Posn, rotations: CycleIndex, identity: Tetrimino) {
    this.position = posn;
    this.piece = rotations;
    this.identity = identity;
  }

  getEmptyLineCountY(): number {
    for (let i = 3; i >= 0; i -= 1) {
      let foundFilled = false;
      for (let j = 0; j < 4; j += 1) {
        if (this.piece.first[i][j]) {
          foundFilled = true;
          break;
        }
      }
      if (foundFilled) {
        return 3 - i;
      }
    }
    throw new Error('Impossible piece');
  }

  getEmptyLineCountFromRight(): number {
    for (let i = 3; i >= 0; i -= 1) {
      let foundFilled = false;
      for (let j = 0; j < 4; j += 1) {
        if (this.piece.first[j][i]) {
          foundFilled = true;
          break;
        }
      }
      if (foundFilled) {
        return 3 - i;
      }
    }
    throw new Error('Impossible piece');
  }

  getEmptyLineCountFromLeft(): number {
    for (let i = 0; i <= 3; i += 1) {
      let foundFilled = false;
      for (let j = 0; j < 4; j += 1) {
        if (this.piece.first[j][i]) {
          foundFilled = true;
          break;
        }
      }
      if (foundFilled) {
        return i;
      }
    }
    throw new Error('Impossible piece');
  }

  rotate(r: Rotation): void {
    if (r === Rotation.CLOCKWISE) {
      this.piece.cycleRight();
      return;
    }
    if (r === Rotation.COUNTERCLOCKWISE) {
      this.piece.cycleLeft();
      return;
    }
    if (r === Rotation.FLIP) {
      this.piece.cycleFlip();
    }
  }

  checkOverlap(b: Board, posns: boolean[][], shift: Posn): boolean {
    for (let i = 0; i < 4; i++) {
      for (let j = 0; j < 4; j++) {
        if (
          posns[i][j] &&
          b.overlap(new Posn(this.position.x + j + shift.x, this.position.y + i + shift.y))
        ) {
          return true;
        }
      }
    }
    return false;
  }

  moveLeft(b: Board): void {
    let shift = new Posn(-1, 0);
    if (!this.checkOverlap(b, this.piece.first, shift)) {
      this.position = new Posn(this.position.x - 1, this.position.y);
    }
  }

  moveRight(b: Board): void {
    let shift = new Posn(1, 0);
    if (!this.checkOverlap(b, this.piece.first, shift)) {
      this.position = new Posn(this.position.x + 1, this.position.y);
    }
  }

  softDrop(b: Board): void {
    let shift = new Posn(0, 1);
    if (!this.checkOverlap(b, this.piece.first, shift)) {
      console.log('foo ' + this.position);
      this.position = new Posn(this.position.x, this.position.y + 1);
    }
  }

  softDropInf(b: Board): void {
    for (let i = this.position.y; i < b.height; i++) {
      if (this.checkOverlap(b, this.piece.first, new Posn(0, i - this.position.y + 1))) {
        this.position = new Posn(this.position.x, i);
        return;
      }
    }
    this.position = new Posn(this.position.x, b.height - this.getEmptyLineCountY());
  }

  moveRightInf(b: Board): void {
    for (let i = this.position.x; i < b.width; i++) {
      if (this.checkOverlap(b, this.piece.first, new Posn(i - this.position.x + 1, 0))) {
        this.position = new Posn(i, this.position.y);
        return;
      }
    }
    this.position = new Posn(b.width - this.getEmptyLineCountFromRight(), this.position.y);
  }

  hardDrop(b: Board): void {
    for (let i = this.position.y; i < b.height; i++) {
      if (this.checkOverlap(b, this.piece.first, new Posn(0, i - this.position.y + 1))) {
        this.position = new Posn(this.position.x, i);
        b.placePiece(this);
        return;
      }
    }

    this.position = new Posn(this.position.x, b.height - this.getEmptyLineCountY());
    b.placePiece(this);
    b.pieceplaced = true;
  }

  getKickTests(r: Rotation): Posn[] {
    if (this.piece.first === this.rotInitialState('up')) {
      switch (r) {
        case Rotation.CLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(-1, 0),
            new Posn(-1, 1),
            new Posn(0, 2),
            new Posn(-1, 2),
          ];
        case Rotation.COUNTERCLOCKWISE:
          return [new Posn(0, 0), new Posn(1, 0), new Posn(1, 1), new Posn(0, 2), new Posn(1, 2)];
        default:
          return [new Posn(0, 0), new Posn(0, -1)];
      }
    } else if (this.piece.first === this.rotInitialState('right')) {
      switch (r) {
        case Rotation.CLOCKWISE:
          return [new Posn(0, 0), new Posn(1, 0), new Posn(1, 1), new Posn(0, -2), new Posn(1, 2)];
        case Rotation.COUNTERCLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(1, 0),
            new Posn(1, 1),
            new Posn(1, 0),
            new Posn(0, -2),
            new Posn(1, 2),
          ];
        default:
          return [new Posn(0, 0), new Posn(0, -1)];
      }
    } else if (this.piece.first === this.rotInitialState('left')) {
      switch (r) {
        case Rotation.CLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(-1, 0),
            new Posn(-1, 1),
            new Posn(0, 2),
            new Posn(-1, 2),
          ];
        case Rotation.COUNTERCLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(-1, 0),
            new Posn(-1, 1),
            new Posn(0, 2),
            new Posn(-1, 2),
          ];
        default:
          return [new Posn(0, 0), new Posn(0, -1)];
      }
    } else {
      switch (r) {
        case Rotation.CLOCKWISE:
          return [
            new Posn(0, 0),
            new Posn(-1, 0),
            new Posn(-1, 1),
            new Posn(0, 2),
            new Posn(-1, 2),
          ];
        case Rotation.COUNTERCLOCKWISE:
          return [new Posn(0, 0), new Posn(1, 0), new Posn(1, 1), new Posn(0, 2), new Posn(1, 2)];
        default:
          return [new Posn(0, 0), new Posn(0, -1)];
      }
    }
  }

  abstract hasSpun(b: Board): boolean;
  abstract rotInitialState(s: string): boolean[][];
}
