import Konva from 'konva';
import { RuleType, Ruleset } from '@/Ruleset';
import { Board } from '@/board';
import { SingerBot } from '@/bot/SingerBot';
import { FilePaths } from '@/constants';
import { GameStats } from '@/util/GameStats';
import { AtkMeter, PiecesMeter, TimeMeter } from '@/util/Meter';
import { pickRandom } from '@/util/pickRandom';
import {
  Color,
  Posn,
  TextImage,
  World,
  WorldEnd,
  WorldScene,
} from 'impworld';
import { AudioPlayer } from './ui/AudioPlayer';
import { Rotation } from './pieces/Rotation';
import { GamePositions } from './GamePositions';

export class GameState extends World {
  board: Board;
  positions: GamePositions;
  rules = new Ruleset(RuleType.VS, 100);
  stats = new GameStats();
  keyheldtime?: number;
  sdfactive: boolean = false;
  bot: SingerBot = new SingerBot();
  storedval: number = 0;

  arractiveleft: boolean = false;
  leftkeypressed: boolean = false;

  arractiveright: boolean = false;
  rightkeypressed: boolean = false;

  spin: boolean = false;

  // settings
  static DAS = 50; // in ms
  static ARR = 0; // in ms
  static SDF = 0; // in ms

  static GAME_SPEED = .1;
  static INVERT_SPEED = Math.floor(1 / GameState.GAME_SPEED);

  static DECO_DURATION = 2000;

  constructor(width: number, height: number) {
    super();
    this.positions = new GamePositions(width, height);
    this.board = new Board(this.positions);
  }

  worldEnds(): WorldEnd {
    if (this.rules.gameOver(this) || this.board.gameovertrigger) {
      return new WorldEnd(true, this.rules.lastScene(this));
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  makeScene() {
    let systime: number = Date.now();
    let time: number = systime - this.stats.starttime;

    let s: WorldScene = this.getEmptyScene();
    let singerpos: Posn = new Posn(
      this.positions.width - Math.floor(this.bot.getSinger().getWidth() / 2),
      this.positions.height - Math.floor(this.bot.getSinger().getWidth() / 2),
    );

    if (time == 0) {
      time += 1;
    }

    const linemeter = new TextImage('LINES:  ' + this.stats.lines, Color.WHITE);
    const atkmeter = new AtkMeter(this.stats.atk, time).getMeterVal();
    const timemeter = new TimeMeter(time).getMeterVal();
    const piecemeter = new PiecesMeter(this.stats.pieces, time).getMeterVal();
    const b2bmeter =
      this.board.b2b > 1
        ? new TextImage('B2B X' + (this.board.b2b - 1), Color.WHITE)
        : new TextImage('', Color.WHITE);
    const combometer =
      this.board.currentcombo > 1
        ? new TextImage(this.board.currentcombo - 1 + ' COMBO', Color.WHITE)
        : new TextImage('', Color.WHITE);
    const songname = new TextImage(pickRandom(Object.keys(FilePaths.audio.songs)), Color.WHITE);

    // Draw the existing residue
    this.board.residue.forEach((row, y) => {
      row.forEach((cell, x) => {
        s.placeImageXY(
          this.board.drawResidue(cell).movePinhole(-this.positions.cellSize / 2, -this.positions.cellSize / 2),
          this.positions.boardLeft + x * this.positions.cellSize,
          this.positions.boardTop + y * this.positions.cellSize,
        );
      });
    })

    s.placeImageXY(
      this.board.garbage.draw(this.positions.boardRows),
      this.positions.boardRight,
      this.positions.boardTop + (this.positions.boardRows / 2) * this.positions.cellSize,
    );

    const shadow = this.board.fallingpiece;
    const storedpos = new Posn(
      this.board.fallingpiece.position.x,
      this.board.fallingpiece.position.y,
    );
    for (let i = shadow.position.y; i < this.positions.boardRows - 1; i++) {
      if (
        this.board.fallingpiece.checkOverlap(
          this.board,
          this.board.fallingpiece.piece.first,
          new Posn(0, i - shadow.position.y + 1),
        )
      ) {
        shadow.position = new Posn(shadow.position.x, i);
        break;
      }
    }
    if (shadow.position == storedpos) {
      shadow.position = new Posn(
        shadow.position.x,
        this.positions.boardRows - shadow.getEmptyLineCountY(),
      );
    }

    shadow.drawPiece(this.board.theme, s, this.board.theme.shadow, this.positions);
    shadow.position = storedpos;

    this.board.fallingpiece.drawPiece(
      this.board.theme,
      s,
      this.board.pieceToImage(this.board.fallingpiece),
      this.positions,
    );

    s.placeImageXY(
      this.board.theme.holdbox,
      this.positions.boardRight + this.positions.cellSize + this.board.theme.holdbox.getWidth() / 2,
      this.positions.boardTop + Math.floor(this.board.theme.holdbox.getHeight() / 2),
    );
    if (this.board.hold) {
      const p = this.board.tetriminoToPiece(this.board.hold);
      p.position = new Posn(
        this.positions.boardColumns + 2.5,
        0.6,
      );
      p.drawPiece(this.board.theme, s, this.board.pieceToImage(p), this.positions);
    }

    s.placeImageXY(
      new TextImage('LINES:  ' + this.stats.lines, Color.WHITE).movePinhole(
        (linemeter.getWidth() * -1) / 2,
        0,
      ),
      this.positions.boardRight + this.positions.cellSize,
      this.positions.boardTop + this.positions.firstMeterSpacing,
    );
    s.placeImageXY(
      atkmeter.movePinhole((atkmeter.getWidth() * -1) / 2, 0),
      this.positions.boardRight + this.positions.cellSize,
      Math.floor(this.positions.meterSpacing * 2.5),
    );
    s.placeImageXY(
      timemeter.movePinhole((timemeter.getWidth() * -1) / 2, 0),
      this.positions.boardRight + this.positions.cellSize,
      this.positions.meterSpacing * 3,
    );
    s.placeImageXY(
      piecemeter.movePinhole((piecemeter.getWidth() * -1) / 2, 0),
      this.positions.boardRight + this.positions.cellSize,
      Math.floor(this.positions.meterSpacing * 3.5),
    );
    s.placeImageXY(
      b2bmeter.movePinhole((b2bmeter.getWidth() * -1) / 2, 0),
      this.positions.boardRight + this.positions.cellSize,
      this.positions.meterSpacing * 4,
    );
    s.placeImageXY(
      combometer.movePinhole((combometer.getWidth() * -1) / 2, 0),
      this.positions.boardRight + this.positions.cellSize,
      this.positions.meterSpacing * 5,
    );

    for (let i = 0; i < 5; i++) {
      const queuepiece = this.board.tetriminoToPiece(this.board.queue[i]);
      queuepiece.position = new Posn(this.positions.boardRight / this.positions.cellSize + 4, (i + 2) * 4);
      queuepiece.drawPiece(this.board.theme, s, this.board.pieceToImage(queuepiece), this.positions);
    }

    // ornament drawing
    for (let i = 0; i < this.board.ornaments.length; i++) {
      const ornament = this.board.ornaments[i];
      s.placeImageXY(ornament[0], ornament[1].x, ornament[1].y);
    }

    // s.placeImageXY(bot.getSinger(), singerpos.x, singerpos.y);

    // numbers drawing thing
    if (
      this.board.ornaments.length > 0 &&
      Date.now() - this.stats.decostarttime > GameState.DECO_DURATION
    ) {
      this.board.ornaments.shift();
      this.storedval = 0;
      this.stats.decostarttime = Date.now();
    }

    s.placeImageXY(songname.movePinhole(0, -songname.getHeight() / 2), this.positions.width / 2, this.positions.boardBottom);

    // animation drawing
    // for (let i = 0; i < this.board.anims.length; i++) {
    // 	const a = this.board.anims[i];
    // 	s.placeImageXY(a.getAnim(), a.posn.x, a.posn.y);
    // }

    return s;
  }

  protected onKeyEvent(key: string) {
    switch (key) {
      case 'left':
        this.board.fallingpiece.moveLeft(this.board);
        this.leftkeypressed = true;
        break;
      case 'right':
        this.board.fallingpiece.moveRight(this.board);
        this.rightkeypressed = true;
        break;
      case 'down':
        this.sdfactive = true;
        break;
      case ' ':
        this.board.fallingpiece.hardDrop(this.board);
        this.stats.pieces += 1;
        AudioPlayer.play(FilePaths.audio.sfx.PLACEPIECE);
        const tosend = this.board.removeRows(this);
        this.stats.atk += tosend;
        // this.bot.sendLines(tosend);
        this.spin = this.board.fallingpiece.hasSpun(this.board);
        break;
      case 'c':
        this.board.holdPiece();
        break;
      case 'up':
      case 'x':
        const testsright = this.board.fallingpiece.getKickTests(Rotation.CLOCKWISE);
        let moved = false;
        for (let i = 0; i < testsright.length; i++) {
          const p = testsright[i];
          if (
            !this.board.fallingpiece.checkOverlap(
              this.board,
              this.board.fallingpiece.piece.right,
              p,
            ) &&
            !moved
          ) {
            this.board.fallingpiece.position = new Posn(
              this.board.fallingpiece.position.x + p.x,
              this.board.fallingpiece.position.y + p.y,
            );
            this.board.fallingpiece.rotate(Rotation.CLOCKWISE);
            this.spin = this.board.fallingpiece.hasSpun(this.board);
            this.board.spin = this.spin;
            moved = true;
          }
        }
        this.spin = this.board.fallingpiece.hasSpun(this.board);
        break;
      case 'z':
        const testsleft = this.board.fallingpiece.getKickTests(Rotation.COUNTERCLOCKWISE);
        let movedpiece = false;
        for (let i = 0; i < testsleft.length; i++) {
          const p = testsleft[i];
          if (
            !this.board.fallingpiece.checkOverlap(
              this.board,
              this.board.fallingpiece.piece.left,
              p,
            ) &&
            !movedpiece
          ) {
            this.board.fallingpiece.position = new Posn(
              this.board.fallingpiece.position.x + p.x,
              this.board.fallingpiece.position.y + p.y,
            );
            this.board.fallingpiece.rotate(Rotation.COUNTERCLOCKWISE);
            this.spin = this.board.fallingpiece.hasSpun(this.board);
            this.board.spin = this.spin;
            movedpiece = true;
          }
        }
        this.spin = this.board.fallingpiece.hasSpun(this.board);
        break;
      case 'a':
        if (
          !this.board.fallingpiece.checkOverlap(
            this.board,
            this.board.fallingpiece.piece.flip,
            new Posn(0, 0),
          )
        ) {
          this.board.fallingpiece.rotate(Rotation.FLIP);
        }
        this.spin = this.board.fallingpiece.hasSpun(this.board);
        this.board.spin = this.spin;
        break;
      case 'r':
        this.board = new Board(this.positions);
        this.stats = new GameStats();
    }
  }

  protected onGesture(name: 'swipeleft' | 'swiperight' | 'swipeup' | 'swipedown') {
    const key = {
      swipeleft: 'left',
      swiperight: 'right',
      swipeup: 'up',
      swipedown: 'down',
    }[name];
    this.onKeyEvent(key);
    setTimeout(() => this.onKeyReleased(key), 100);
  }

  protected onKeyReleased(key: string) {
    switch (key) {
      case 'down':
        this.sdfactive = false;
        break;
      case 'left':
        this.arractiveleft = false;
        this.leftkeypressed = false;
        this.keyheldtime = undefined;
        break;
      case 'right':
        this.arractiveright = false;
        this.rightkeypressed = false;
        this.keyheldtime = undefined;
        break;
    }
  }

  protected onTick() {
    let time: number = Date.now() - this.stats.starttime;

    /*
    this.bot.makeMove(this);

    for (let i = 0; i < this.board.anims.length; i++) {
      let a: Animation = this.board.anims[i];
      if (a.isEnded()) {
        this.board.anims.splice(i, 1);
        i--; // Decrement index to account for the removed element
      }
    }
    */

    if ((this.rightkeypressed || this.leftkeypressed) && this.keyheldtime === undefined) {
      this.keyheldtime = Date.now();
    }

    let dascheck: boolean =
      this.keyheldtime !== undefined ? Date.now() - this.keyheldtime >= GameState.DAS : false;

    if (this.sdfactive && GameState.SDF <= 0) {
      this.board.fallingpiece.softDropInf(this.board);
    } else if (this.sdfactive && time % GameState.SDF === 0) {
      this.board.fallingpiece.softDrop(this.board);
    }

    if (this.leftkeypressed && GameState.ARR > 0 && time % GameState.ARR === 0 && dascheck) {
      this.board.fallingpiece.moveLeft(this.board);
    } else if (GameState.ARR <= 0 && dascheck && this.leftkeypressed) {
      this.board.fallingpiece.moveLeftInf(this.board);
    }

    if (this.rightkeypressed && GameState.ARR > 0 && time % GameState.ARR === 0 && dascheck) {
      this.board.fallingpiece.moveRight(this.board);
    } else if (GameState.ARR <= 0 && dascheck && this.rightkeypressed) {
      this.board.fallingpiece.moveRightInf(this.board);
    }
  }
}
