import { GameState } from '@/GameState';
import { FriendlySingerBot } from '@/bot/FriendlySingerBot';
import { HostileSingerBot } from '@/bot/HostileSingerBot';
import { VSingerBot } from '@/bot/VSingerBot';
import { Residue } from '@/pieces/Residue';
import { TimeMeter } from '@/util/Meter';
import { Color, Posn, TextImage, WorldScene } from 'impworld';

export enum RuleType {
  LINES,
  TIME,
  SCORE,
  VS,
  SURVIVAL,
}

export class Ruleset {
  constructor(private type: RuleType, private amount: number) {}

  gameOver(g: GameState) {
    switch (this.type) {
      case RuleType.TIME:
        return Date.now() - g.stats.starttime >= this.amount;
      case RuleType.SCORE:
        return g.stats.score >= this.amount;
      case RuleType.SURVIVAL:
        return g.board.residue.length > g.board.height + 2;
      case RuleType.LINES:
        return g.stats.lines >= this.amount;
      case RuleType.VS:
        return (
          g.board.residue.filter((residue) => !residue.every((r) => r === Residue.EMPTY)).length >
          g.board.height + 2
        );
    }
    return false;
  }

  lastScene(g: GameState) {
    const s = new WorldScene(new Posn(500, 500));
    switch (this.type) {
      case RuleType.LINES:
        s.placeImageXY(
          new TextImage(
            this.amount +
              ' LINES : ' +
              new TimeMeter(Math.floor(Date.now() - g.stats.starttime)).getMeterVal(),
            Color.BLACK,
          ),
          250,
          250,
        );
        break;
      case RuleType.VS:
        s.placeImageXY(new TextImage('' + g.board.residue.length, Color.BLACK), 250, 250);
    }
    return s;
  }

  getBot(aps: number, hp: number) {
    switch (this.type) {
      case RuleType.VS:
        return new VSingerBot(aps, 1000, hp);
      case RuleType.SURVIVAL:
        return new HostileSingerBot(aps);
      default:
        return new FriendlySingerBot();
    }
  }
}
