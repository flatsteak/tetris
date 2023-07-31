import { RuleType, Ruleset } from '@/Ruleset';
import { Board } from '@/board';
import { SingerBot } from '@/bot/SingerBot';
import { CELL_SIZE, FilePaths } from '@/constants';
import { GameStats } from '@/util/GameStats';
import { AtkMeter, PiecesMeter, TimeMeter } from '@/util/Meter';
import { pickRandom } from '@/util/pickRandom';
import { BesideImage, Color, Posn, TextImage, World, WorldEnd, WorldImage, WorldScene } from 'impworld';

export class GameState extends World {
  board = new Board();
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

  static METER_SPACING = CELL_SIZE * 2;
  static FIRST_METER_SPACING = CELL_SIZE * 4;

  static GAME_SPEED = 0.001;
  static INVERT_SPEED = Math.floor(1 / GameState.GAME_SPEED);

  static DECO_DURATION = 2000;

  static SCREEN_WIDTH = document.getElementById('world')?.clientWidth ?? 500;
  static SCREEN_HEIGHT = document.getElementById('world')?.clientHeight ?? 500;

  constructor() {
    super();
  }

  worldEnds(): WorldEnd {
    if (this.rules.gameOver(this) || this.board.gameovertrigger) {
      return new WorldEnd(true, this.rules.lastScene(this));
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  makeScene() {
    let width: number = CELL_SIZE * this.board.width;
    let height: number = CELL_SIZE * this.board.height;
    let systime: number = Date.now();
    let time: number = systime - this.stats.starttime;
    let s: WorldScene = new WorldScene(new Posn(width, height));
    let singerpos: Posn = new Posn(GameState.SCREEN_WIDTH - Math.floor(this.bot.getSinger().getWidth() / 2), GameState.SCREEN_HEIGHT - Math.floor(this.bot.getSinger().getWidth() / 2));

		if (time == 0) {
			time += 1;
		}
		s.placeImageXY(this.board.bgimage, Math.round(GameState.SCREEN_WIDTH / 2), Math.round(GameState.SCREEN_HEIGHT / 2));

		const linemeter = new TextImage("LINES:  " + this.stats.lines, Color.WHITE);
		const atkmeter = new AtkMeter(this.stats.atk, time).getMeterVal();
		const timemeter = new TimeMeter(time).getMeterVal();
		const piecemeter = new PiecesMeter(this.stats.pieces, time).getMeterVal();
		const b2bmeter = (this.board.b2b > 1)? new TextImage("B2B X" + (this.board.b2b - 1), Color.WHITE) :  new TextImage("", Color.WHITE);
		const combometer = (this.board.currentcombo > 1)? new TextImage((this.board.currentcombo - 1) + " COMBO", Color.WHITE) :  new TextImage("", Color.WHITE);
		const songname = new TextImage(pickRandom(Object.keys(FilePaths.audio.songs)), Color.WHITE);

		// Draw the existing residue
		for (let i = 0; i < this.board.height; i++) {
      const mapped = this.board.residue[i].map((r) => this.board.drawResidue(r));
      const composedImage = new BesideImage(mapped[0], ...mapped.slice(1));
			s.placeImageXY(
        composedImage,
					this.board.width / 2 * CELL_SIZE,
					i * CELL_SIZE + CELL_SIZE / 2);
		}

		s.placeImageXY(this.board.garbage.draw(this.board.height), this.board.width * CELL_SIZE + CELL_SIZE / 2, this.board.height / 2 * CELL_SIZE);

    const shadow = this.board.fallingpiece;
		const storedpos = new Posn(this.board.fallingpiece.position.x, this.board.fallingpiece.position.y);
		for (let i = shadow.position.y; i < this.board.height - 1; i++) {
			if (this.board.fallingpiece.checkOverlap(this.board, this.board.fallingpiece.piece.first, new Posn(0, i - shadow.position.y + 1))) {
				shadow.position = new Posn(shadow.position.x, i);
				break;
			}
		}
		if (shadow.position == storedpos) {
			shadow.position = new Posn(shadow.position.x, this.board.height - shadow.getEmptyLineCountY());
		}

		shadow.drawPiece(this.board.t, s, this.board.t.shadow);
		shadow.position = storedpos;

		this.board.fallingpiece.drawPiece(this.board.t, s, this.board.pieceToImage(this.board.fallingpiece));

		s.placeImageXY(this.board.t.holdbox, Math.floor((width + this.board.t.holdbox.getWidth() / 2) + CELL_SIZE),
				Math.floor(this.board.t.holdbox.getHeight() / 2));
		if (this.board.hold) {
			const p = this.board.tetriminoToPiece(this.board.hold);
			p.position = new Posn(Math.floor((width + this.board.t.holdbox.getWidth() / 2 - CELL_SIZE / 8) / CELL_SIZE),
					Math.floor((this.board.t.holdbox.getHeight() / 2) / CELL_SIZE - 1));
			p.drawPiece(this.board.t, s, this.board.pieceToImage(p));
		}
		s.placeImageXY(new TextImage("LINES:  " + this.stats.lines, Color.WHITE).movePinhole((linemeter.getWidth() * -1) / 2, 0), width + CELL_SIZE, GameState.FIRST_METER_SPACING);
		s.placeImageXY(atkmeter.movePinhole((atkmeter.getWidth() * -1) / 2, 0), width + CELL_SIZE, Math.floor(GameState.METER_SPACING * 2.5));
		s.placeImageXY(timemeter.movePinhole((timemeter.getWidth() * -1) / 2, 0), width + CELL_SIZE, GameState.METER_SPACING * 3);
		s.placeImageXY(piecemeter.movePinhole((piecemeter.getWidth() * -1) / 2, 0), width + CELL_SIZE, Math.floor(GameState.METER_SPACING * 3.5));
		s.placeImageXY(b2bmeter.movePinhole((b2bmeter.getWidth() * -1) / 2, 0), width + CELL_SIZE, GameState.METER_SPACING * 4);
		s.placeImageXY(combometer.movePinhole((combometer.getWidth() * -1) / 2, 0), width + CELL_SIZE, GameState.METER_SPACING * 5);

		for (let i = 0; i < 5; i++) {
			const queuepiece = this.board.tetriminoToPiece(this.board.queue[i]);
			queuepiece.position = new Posn(width / CELL_SIZE + 4, (i + 2) * 4);
			queuepiece.drawPiece(this.board.t, s, this.board.pieceToImage(queuepiece));
		}

		// ornament drawing
		for (let i = 0; i < this.board.ornaments.length; i++) {
			const ornament = this.board.ornaments[i];
			s.placeImageXY(ornament[0], ornament[1].x, ornament[1].y);
		}

		// s.placeImageXY(bot.getSinger(), singerpos.x, singerpos.y);

		// numbers drawing thing
		if (this.board.ornaments.length > 0 && Date.now() - this.stats.decostarttime > GameState.DECO_DURATION) {
			this.board.ornaments.shift();
			this.storedval = 0;
			this.stats.decostarttime = Date.now();
		}

		s.placeImageXY(songname.movePinhole(0, - songname.getHeight() / 2), width / 2, height);

		// animation drawing
		// for (let i = 0; i < this.board.anims.length; i++) {
		// 	const a = this.board.anims[i];
		// 	s.placeImageXY(a.getAnim(), a.posn.x, a.posn.y);
		// }

		return s;
	}
}
