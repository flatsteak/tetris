import { GamePositions } from '@/GamePositions';
import { GameState } from '@/GameState';
import { Queue } from '@/board';
import { BeatmapSingerBot } from '@/bot/BeatmapSingerBot';
import { VSingerBot } from '@/bot/VSingerBot';
import { FilePaths } from '@/constants';
import { APiece } from '@/pieces/APiece';
import { AttackTable } from '@/pieces/AttackTable';
import { IPiece } from '@/pieces/IPiece';
import { JPiece } from '@/pieces/JPiece';
import { LPiece } from '@/pieces/LPiece';
import { OPiece } from '@/pieces/OPiece';
import { Residue } from '@/pieces/Residue';
import { SPiece } from '@/pieces/SPiece';
import { TPiece } from '@/pieces/TPiece';
import { Tetrimino } from '@/pieces/Tetrimino';
import { ZPiece } from '@/pieces/ZPiece';
import { Theme } from '@/themes/Theme';
import { ThemePool } from '@/themes/ThemePool';
import { AudioPlayer } from '@/ui/AudioPlayer';
import { GarbageMeter } from '@/ui/GarbageMeter';
import { tetriminoToResidue } from '@/util/tetriminoToResidue';
import { Color, FromFileImage, OverlayImage, Posn, RotateImage, TextImage, WorldImage } from 'impworld';

export class Board {
  residue: Residue[][];
  currentcombo: number;
  b2b: number;
  theme: Theme;
  queue: Tetrimino[];
  fallingpiece: APiece;
  hold: Tetrimino | undefined;
  pieceplaced: boolean;
  garbage: GarbageMeter;
  ornaments: [WorldImage, Posn][];
  anims: Animation[];
  bgimage: WorldImage;
  spin: boolean;
  gameovertrigger: boolean;

  constructor(public readonly positions: GamePositions) {
    this.residue = [];
    for (let i = 0; i < this.positions.boardRows; i++) {
      this.residue.push(this.newEmptyRow());
    }
    this.currentcombo = 0;
    this.b2b = 0;
    this.theme = ThemePool.FADE_THEME(positions);
    this.queue = Queue.sevenBag();
    this.queue.push(...Queue.sevenBag());
    this.fallingpiece = this.pullFromBag(this.queue);
    this.hold = undefined;
    this.pieceplaced = false;
    this.garbage = new GarbageMeter(positions);
    this.ornaments = [];
    this.anims = [];
    this.bgimage = new FromFileImage(FilePaths.image.bg.BGSTARRY);
    this.spin = false;
    this.gameovertrigger = false;
  }

  //
  // CHEESE STUFF
  //

  newLineOfCheese(cheesewell: number): Residue[] {
    const width: number = 10; // Assuming width is a constant value
    const toreturn: Residue[] = new Array<Residue>(width);
    for (let i = 0; i < width; i++) {
      if (i === cheesewell) {
        toreturn[i] = Residue.EMPTY;
      } else {
        toreturn[i] = Residue.CHEESE;
      }
    }
    return toreturn;
  }

  addCheese(lines: number): void {
    const well = Math.floor(Math.random() * this.positions.boardColumns);
    const cheeseline = this.newLineOfCheese(well);
    for (let i = 0; i < lines; i++) {
      this.residue.shift();
      this.residue.push(cheeseline);
    }
  }

  recieveLines(lines: number) {
    if (this.garbage) {
      this.garbage.garbage += lines;
    }
    AudioPlayer.play(FilePaths.audio.sfx.RECIEVESMALL);
  }

  placePiece(p: APiece): void {
    for (let i = 0; i < 4; i++) {
      let currentRow: Residue[] | null = null;
      for (let j = 0; j < 4; j++) {
        if (p.piece.first[i][j]) {
          if (currentRow === null) {
            const existing: Residue[] | undefined = this.residue[p.position.y + i];
            currentRow = existing === undefined ? this.newEmptyRow() : existing.slice();
            this.residue[i + p.position.y] = currentRow;
          }
          currentRow[p.position.x + j] = tetriminoToResidue(p.identity);
        }
      }
    }
    this.fallingpiece = this.pullFromBag(this.queue);
  }

  overlap(p: Posn): boolean {
    if (p.x >= this.positions.boardColumns || p.x < 0) {
      return true;
    }
    switch (this.getResidueAt(p)) {
      case Residue.S:
      case Residue.Z:
      case Residue.L:
      case Residue.J:
      case Residue.O:
      case Residue.T:
      case Residue.I:
      case Residue.CHEESE:
      case Residue.FLOOR:
        return true;
      default:
        return false;
    }
  }

  holdPiece(): void {
    if (this.hold) {
      const heldpiece: APiece = this.tetriminoToPiece(this.hold);
      this.hold = this.fallingpiece.identity;
      this.fallingpiece = heldpiece;
    } else {
      this.hold = this.fallingpiece.identity;
      this.fallingpiece = this.pullFromBag(this.queue);
    }
  }

  newEmptyRow(): Residue[] {
    return new Array<Residue>(this.positions.boardColumns).fill(Residue.EMPTY);
  }

  removeRows(g: GameState): number {
    console.log(this.currentcombo);
    let toremove: number[] = [];
    for (let i = 0; i < this.positions.boardRows; i++) {
      let row: Residue[] = this.residue[i];
      if (!row.includes(Residue.EMPTY)) {
        toremove.push(i);
      }
    }

    let addcombo = false;
    if (toremove.length <= 0) {
      this.currentcombo = 0;
      this.garbage.receive(this);
      return 0;
    } else {
      addcombo = true;
    }

    for (let r of toremove) {
      this.removeLine(r, g);
    }

    const addb2b = toremove.length == 4 || this.spin;

    if (!addb2b) {
      this.b2b = 0;
    }

    let atk = AttackTable.applyCombo(this.currentcombo, this.b2b, [toremove.length, g.spin]);
    if (this.residue.every((a) => a.every((c) => c === Residue.EMPTY))) {
      atk += 10;
    }

    if (addb2b) {
      this.b2b += 1;
    }

    let placeat = new Posn((this.positions.boardColumns / 2) * this.positions.cellSize, toremove[0] * this.positions.cellSize);
    let displayatk = atk;
    if (atk !== 0) {
      if (Date.now() - g.stats.decostarttime < GameState.DECO_DURATION && this.ornaments.length > 0) {
        displayatk += g.storedval;
      }
      g.storedval += atk;
      g.stats.decostarttime = Date.now();
      const txtsize = Math.floor(
        Math.random() * Math.min(70, displayatk * 5, Math.min(80, displayatk * 5 + 10)),
      );
      const rot = Math.floor(Math.random() * 140) - 70;
      const toput: [WorldImage, Posn] = [
        new RotateImage(
          new OverlayImage(
            new TextImage(String(displayatk), txtsize, Color.BLACK),
            new TextImage(String(displayatk), txtsize + 5, Color.WHITE),
          ),
          rot,
        ),
        placeat,
      ];
      if (this.ornaments.length > 0) {
        this.ornaments[0] = toput;
      } else {
        this.ornaments.push(toput);
      }
    }

    if (displayatk >= 16) {
      AudioPlayer.play(FilePaths.audio.sfx.THUNDERMINI);
      // Animation flashlow = new Flashwave(placeat);
      // flashlow.start(placeat);
      // this.anims.add(flashlow);
    } else if (displayatk >= 10) {
      AudioPlayer.play(FilePaths.audio.sfx.THUNDERMINI);
      // Animation flashlow = new Flashwave(placeat);
      // flashlow.start(placeat);
      // this.anims.add(flashlow);
    }

    if (addcombo) {
      this.currentcombo += 1;
    }

    if (g.bot instanceof BeatmapSingerBot) {
      this.garbage.garbage -= atk;
    } else if (g.bot instanceof VSingerBot) {
      if (this.garbage.garbage > 0 && atk >= this.garbage.garbage) {
        this.garbage.garbage = 0;
        atk -= this.garbage.garbage;
      } else if (this.garbage.garbage > 0) {
        this.garbage.garbage -= atk;
        atk = 0;
      }
    }

    return atk;
  }

  removeLine(y: number, g: GameState): void {
    this.residue.splice(y, 1);
    this.residue.splice(0, 0, this.newEmptyRow());
    g.stats.lines += 1;
  }

  getResidueAt(p: Posn): Residue {
    if (p.y >= this.positions.boardRows || p.x < 0) {
      return Residue.FLOOR;
    }
    if (p.y >= this.residue.length || p.y < 0) {
      return Residue.EMPTY;
    }
    if (p.x >= this.positions.boardColumns) {
      return Residue.EMPTY;
    }

    return this.residue[p.y][p.x];
  }

  drawResidue(r: Residue): WorldImage {
    switch (r) {
      case Residue.S:
        return this.theme.s;
      case Residue.Z:
        return this.theme.z;
      case Residue.O:
        return this.theme.o;
      case Residue.I:
        return this.theme.i;
      case Residue.J:
        return this.theme.j;
      case Residue.L:
        return this.theme.l;
      case Residue.T:
        return this.theme.t;
      case Residue.CHEESE:
        return this.theme.cheese;
      case Residue.EMPTY:
      case Residue.FLOOR:
        return this.theme.empty;
      default:
        throw new Error(`Unknown residue ${r}`);
    }
  }

  pieceToImage(a: APiece): WorldImage {
    if (a instanceof SPiece) {
      return this.theme.s;
    }
    if (a instanceof ZPiece) {
      return this.theme.z;
    }
    if (a instanceof OPiece) {
      return this.theme.o;
    }
    if (a instanceof IPiece) {
      return this.theme.i;
    }
    if (a instanceof JPiece) {
      return this.theme.j;
    }
    if (a instanceof LPiece) {
      return this.theme.l;
    }
    if (a instanceof TPiece) {
      return this.theme.t;
    }
    return this.theme.empty;
  }

  private pullFromBag(queue: Tetrimino[]): APiece {
    const piece = queue.shift()!;
    this.advanceQueue();
    return this.tetriminoToPiece(piece);
  }

  tetriminoToPiece(pull: Tetrimino) {
    const spawn = new Posn(this.positions.boardColumns / 2 - 2, 0);
    switch (pull) {
      case Tetrimino.S:
        const spiece = new SPiece(spawn);
        this.gameovertrigger = spiece.checkOverlap(this, spiece.piece.first, new Posn(0, 0));
        return new SPiece(spawn);
      case Tetrimino.Z:
        const zpiece = new ZPiece(spawn);
        this.gameovertrigger = zpiece.checkOverlap(this, zpiece.piece.first, new Posn(0, 0));
        return zpiece;
      case Tetrimino.L:
        const lpiece = new LPiece(spawn);
        this.gameovertrigger = lpiece.checkOverlap(this, lpiece.piece.first, new Posn(0, 0));
        return lpiece;
      case Tetrimino.J:
        const jpiece = new JPiece(spawn);
        this.gameovertrigger = jpiece.checkOverlap(this, jpiece.piece.first, new Posn(0, 0));
        return jpiece;
      case Tetrimino.O:
        const opiece = new OPiece(spawn);
        this.gameovertrigger = opiece.checkOverlap(this, opiece.piece.first, new Posn(0, 0));
        return opiece;
      case Tetrimino.I:
        const ipiece = new IPiece(spawn);
        this.gameovertrigger = ipiece.checkOverlap(this, ipiece.piece.first, new Posn(0, 0));
        return ipiece;
      default:
        const tpiece = new TPiece(spawn);
        this.gameovertrigger = tpiece.checkOverlap(this, tpiece.piece.first, new Posn(0, 0));
        return tpiece;
    }
  }

  advanceQueue() {
    if (this.queue.length <= 7) {
      this.queue.push(...Queue.sevenBag());
    }
  }
}
