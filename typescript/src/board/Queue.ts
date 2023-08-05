import { Tetrimino } from '../pieces/Tetrimino';

export class Queue {
  static BAG: Tetrimino[] = [
    Tetrimino.S,
    Tetrimino.Z,
    Tetrimino.L,
    Tetrimino.J,
    Tetrimino.T,
    Tetrimino.I,
    Tetrimino.O,
  ];

  static sevenBag(): Tetrimino[] {
    const toreturn: Tetrimino[] = [];
    const bag: Tetrimino[] = [...Queue.BAG];
    for (let i = 7; i > 1; i--) {
      const rngidx = Math.floor(Math.random() * bag.length);
      toreturn.push(bag[rngidx]);
      bag.splice(rngidx, 1);
    }
    toreturn.push(...bag);
    return toreturn;
  }
}