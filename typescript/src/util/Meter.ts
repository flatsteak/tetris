import { GameState } from '@/GameState';
import { Color, TextImage, WorldImage } from 'impworld';

abstract class Meter {
  place1: number;
  place2: number;
  decimals: number;

  constructor(p1: number, p2: number, d: number) {
    this.place1 = Math.floor(p1);
    this.place2 = Math.floor(p2);
    this.decimals = Math.floor(d);
  }

  abstract getMeterVal(): WorldImage;
}

export class TimeMeter extends Meter {
  // the tickspeed must always be 0.01

  constructor(i: number) {
    // in MS
    super(i / (60 * GameState.INVERT_SPEED), (i / GameState.INVERT_SPEED) % 60, i % 1000);
  }

  getMeterVal() {
    return new TextImage(
      'TIME:  ' + this.place1 + ':' + this.place2 + '.' + this.decimals,
      Color.WHITE,
    );
  }
}

export class AtkMeter extends Meter {
  constructor(atk: number, t: number) {
    super(
      atk,
      Math.floor((atk * 60 * GameState.INVERT_SPEED) / t),
      ((atk * 6000 * GameState.INVERT_SPEED) / t) % 100,
    );
  }

  getMeterVal() {
    return new TextImage(
      'ATK:  ' + this.place1 + ', APM:  ' + this.place2 + '.' + this.decimals,
      Color.WHITE,
    );
  }
}

export class PiecesMeter extends Meter {
  constructor(p: number, t: number) {
    super(p, (p * GameState.INVERT_SPEED) / t, ((p * 100 * GameState.INVERT_SPEED) / t) % 100);
  }

  getMeterVal() {
    return new TextImage(
      'PIECES:  ' + this.place1 + ', PPS:  ' + this.place2 + '.' + this.decimals,
      Color.WHITE,
    );
  }
}
