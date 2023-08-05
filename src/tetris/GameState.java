package tetris;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.*;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;



enum Residue {
	S, Z, I, O, T, L, J, CHEESE, EMPTY, FLOOR;

  public String toString() {
      return name() + ": This is " + name();
  }
}

class GameStats {
	int lines;
	int score;
	int atk;
	long starttime;
	long decostarttime;
	int pieces;

	GameStats(int lines, int score, long time, int atk, int pieces, long decostarttime) {
		this.lines = lines;
		this.score = score;
		this.starttime = time;
		this.atk = atk;
		this.pieces = pieces;
		this.decostarttime = decostarttime;
	}
	

	GameStats() {
		this(0, 0, System.currentTimeMillis() + 1, 0, 0, System.currentTimeMillis());
	}
}

class GameState extends World {
	Board board;
	Ruleset rules;
	GameStats stats;
	Optional<Long> keyheldtime;
	boolean sdfactive;
	SingerBot bot;
	int storedval;
	Optional<Player> player;
	Tetrimino last;
	
	DifficultySelectionScreen selscreen = DifficultySelectionScreen.DEFAULT_SELECTION;
	boolean selectionscreenup = true;
	
	boolean arractiveleft;
	boolean leftkeypressed;
	
	boolean arractiveright;
	boolean rightkeypressed;
	
	boolean spin;

	
	// settings
	static int DAS = 50; // in ms
	static int ARR = 0; // in ms
	static int SDF = 0; // in ms
	
	static int METER_SPACING = Board.CELL_SIZE * 2;
	static int FIRST_METER_SPACING = Board.CELL_SIZE * 4;
	
	static double GAME_SPEED = 0.001;
	static int INVERT_SPEED = (int) (1 / GAME_SPEED);
	
	// positions
	
	static int DECO_DURATION = 2000;
	
	static int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	static int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	static Posn SPECIALPOS = new Posn(SCREEN_WIDTH / 16, SCREEN_HEIGHT * 3 / 4);

	GameState() { 
		this.board = new Board();
		this.rules = new Ruleset(RuleType.VS, 100);
		this.stats = new GameStats();
		this.keyheldtime = Optional.empty();
		this.bot = DifficultyPool.CURTAIN_CALL_SS;
		this.storedval = 0;
		this.player = Optional.of(Player.DEFAULT_BUILD);
	}

	
	public WorldEnd worldEnds() {
		if (this.rules.gameOver(this) || this.board.gameovertrigger) {
			return new WorldEnd(true, this.rules.lastScene(this));
		} else {
			return new WorldEnd(false, this.makeScene());
		}
	}
	public WorldScene makeScene() {
		int width = Board.CELL_SIZE * this.board.width;
		int height = Board.CELL_SIZE * this.board.height;
		long systime = System.currentTimeMillis();
		int time = (int) (systime - this.stats.starttime);
		WorldScene s = new WorldScene(width, height);
		Posn singerpos = new Posn(GameState.SCREEN_WIDTH - (int) bot.getSinger().getWidth() / 2, GameState.SCREEN_HEIGHT - (int) bot.getSinger().getWidth() / 2);
		
		if (time == 0) {
			time += 1;
		}
		
		
		
		if (!selectionscreenup) {
		s.placeImageXY(board.bgimage, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
		
		WorldImage linemeter = new TextImage("LINES:  " + this.stats.lines, Color.WHITE);
		WorldImage atkmeter = new AtkMeter(this.stats.atk, time).getMeterVal();
		WorldImage timemeter = new TimeMeter(time).getMeterVal();
		WorldImage piecemeter = new PiecesMeter(this.stats.pieces, time).getMeterVal();
		WorldImage b2bmeter = (this.board.b2b > 1)? new TextImage("B2B X" + (this.board.b2b - 1), Color.WHITE) :  new TextImage("", Color.WHITE);
		WorldImage combometer = (this.board.currentcombo > 1)? new TextImage((this.board.currentcombo - 1) + " COMBO", Color.WHITE) :  new TextImage("", Color.WHITE);
		WorldImage songname = new TextImage(FilePaths.BGSONG.second, Color.WHITE);
		
		

		// Draw the existing residue
		for (int i = 0; i < board.height; i++) {
			s.placeImageXY(
				Arrays.stream(this.board.residue.get(i))
					.map(this.board::drawResidue)
					.reduce((prev, next) -> new BesideImage(prev, next))
					.get(),
					board.width / 2 * Board.CELL_SIZE,
					i * Board.CELL_SIZE + Board.CELL_SIZE / 2);
		}
		// special, ability slots, enchant
		if (player.isPresent()) {
			s.placeImageXY(player.get().special.draw(), SPECIALPOS.x, SPECIALPOS.y);
		}
		
		
		
		// garbagemeters
		WorldImage garbagemeter = this.board.garbage.draw(this.board.height);
		WorldImage chargemeter = this.board.chargemeter.draw(this.board.height);
		WorldImage meters = new BesideImage(garbagemeter, chargemeter).movePinhole(-Board.CELL_SIZE / 2, 0);
		WorldImage meterswithdisplay = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, meters, 0, board.garbage.displayLines().getHeight() * 3 / 2, new AboveImage(board.garbage.displayLines(), board.chargemeter.displayLines())).movePinholeTo(meters.pinhole);
		
		s.placeImageXY(meterswithdisplay, board.width * Board.CELL_SIZE + Board.CELL_SIZE / 2, this.board.height / 2 * Board.CELL_SIZE);
		
		
		APiece shadow = this.board.fallingpiece;
		Posn storedpos = new Posn(this.board.fallingpiece.position.x, this.board.fallingpiece.position.y);
		for (int i = shadow.position.y; i < this.board.height - 1; i++) {
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

		
		// hold piece drawing
		
		s.placeImageXY(this.board.t.holdbox, (int) (width + this.board.t.holdbox.getWidth() / 2) + Board.CELL_SIZE * 2,
				(int) this.board.t.holdbox.getHeight() / 2);
		if (this.board.hold.isPresent()) {
			APiece p = this.board.tetriminoToPiece(this.board.hold.get());
			p.position = new Posn((int) (width + this.board.t.holdbox.getWidth() / 2 - Board.CELL_SIZE / 8) / Board.CELL_SIZE,
					(int) (this.board.t.holdbox.getHeight() / 2) / Board.CELL_SIZE - 1);
			p.drawPiece(this.board.t, s, this.board.pieceToImage(p));
		}
		
		s.placeImageXY(new TextImage("LINES:  " + this.stats.lines, Color.WHITE).movePinhole((linemeter.getWidth() * -1) / 2, 0), width + Board.CELL_SIZE * 2, FIRST_METER_SPACING);
		s.placeImageXY(atkmeter.movePinhole((atkmeter.getWidth() * -1) / 2, 0), width + Board.CELL_SIZE * 2, (int) (METER_SPACING * 2.5));
		s.placeImageXY(timemeter.movePinhole((timemeter.getWidth() * -1) / 2, 0), width + Board.CELL_SIZE * 2, METER_SPACING * 3);
		s.placeImageXY(piecemeter.movePinhole((piecemeter.getWidth() * -1) / 2, 0), width + Board.CELL_SIZE * 2, (int) (METER_SPACING * 3.5));
		s.placeImageXY(b2bmeter.movePinhole((b2bmeter.getWidth() * -1) / 2, 0), width + Board.CELL_SIZE * 2, METER_SPACING * 4);
		s.placeImageXY(combometer.movePinhole((combometer.getWidth() * -1) / 2, 0), width +  Board.CELL_SIZE * 2, METER_SPACING * 5);
		
		for (int i = 0; i < 5; i++) {
			APiece queuepiece = this.board.tetriminoToPiece(this.board.queue.get(i));
			queuepiece.position = new Posn(width / Board.CELL_SIZE + 4, (i + 2) * 4);
			queuepiece.drawPiece(this.board.t, s, this.board.pieceToImage(queuepiece));
		}
		
		// ornament drawing
		for (int i = 0; i < this.board.ornaments.size(); i++) {
			Double<WorldImage, Posn> ornament = this.board.ornaments.get(i);
			s.placeImageXY(ornament.first, ornament.second.x, ornament.second.y);
		}
		
		s.placeImageXY(bot.getSinger(), singerpos.x, singerpos.y);
		
		// numbers drawing thing
		if (this.board.ornaments.size() > 0 && System.currentTimeMillis() - this.stats.decostarttime > GameState.DECO_DURATION) {
			this.board.ornaments.remove(0);
			this.storedval = 0;
			this.stats.decostarttime = System.currentTimeMillis();
		}
		
		s.placeImageXY(songname.movePinhole(0, - songname.getHeight() / 2), width / 2, height);
		
		// animation drawing
		for (int i = 0; i < this.board.anims.size(); i++) {
			Animation a = this.board.anims.get(i);
			s.placeImageXY(a.getAnim(), a.posn.x, a.posn.y);
		}
		
		} else {
			DifficultySelectionScreen defaultsel = DifficultySelectionScreen.DEFAULT_SELECTION;
			defaultsel.placeAllButtons(new Posn(100, 100));
			s.placeImageXY(defaultsel.drawAllSelections(), 100, 100);
		}
		return s;
	}

	public void onKeyEvent(String key) {
		
		switch (key) {
			case "left":
				this.board.fallingpiece.moveLeft(board);
				this.leftkeypressed = true;
				break;
			case "right":
				this.board.fallingpiece.moveRight(board);
				this.rightkeypressed = true;
				break;
			case "down":
				this.sdfactive = true;
				break;
			case " ":
				last = this.board.fallingpiece.identity;
				this.board.fallingpiece.hardDrop(board);
				this.stats.pieces += 1;
				new AudioPlayer().play(FilePaths.PLACEPIECE);
				int tosend = this.board.removeRows(this, last);
				this.stats.atk += tosend;
				this.bot.sendLines(tosend);
				spin = this.board.fallingpiece.hasSpun(this.board);
				break;
			case "c":
				this.board.hold();
				break;
			case "up":
				List<Posn> testsright = this.board.fallingpiece.getKickTests(Rotation.CLOCKWISE);
				boolean moved = false;
				for (int i = 0; i < testsright.size(); i++) {
					Posn p = testsright.get(i);
					if (!this.board.fallingpiece.checkOverlap(this.board, this.board.fallingpiece.piece.right, p) && !moved) {
						this.board.fallingpiece.position = new Posn(this.board.fallingpiece.position.x + p.x, this.board.fallingpiece.position.y + p.y);
						this.board.fallingpiece.rotate(Rotation.CLOCKWISE);
						spin = this.board.fallingpiece.hasSpun(this.board);
						this.board.spin = spin;
						moved = true;
					}
					
				}
				spin = this.board.fallingpiece.hasSpun(this.board);
				break;
			case "z":
				List<Posn> testsleft = this.board.fallingpiece.getKickTests(Rotation.COUNTERCLOCKWISE);
				boolean movedpiece = false;
				for (int i = 0; i < testsleft.size(); i++) {
					Posn p = testsleft.get(i);
					if (!this.board.fallingpiece.checkOverlap(this.board, this.board.fallingpiece.piece.left, p) && !movedpiece) {
						this.board.fallingpiece.position = new Posn(this.board.fallingpiece.position.x + p.x, this.board.fallingpiece.position.y + p.y);
						this.board.fallingpiece.rotate(Rotation.COUNTERCLOCKWISE);
						spin = this.board.fallingpiece.hasSpun(this.board);
						this.board.spin = spin;
						movedpiece = true;
					}
					
				}
				spin = this.board.fallingpiece.hasSpun(this.board);
				break;
			case "a":
				if (!this.board.fallingpiece.checkOverlap(board, this.board.fallingpiece.piece.flip, new Posn(0, 0))) {
					this.board.fallingpiece.rotate(Rotation.FLIP);
				}
				spin = this.board.fallingpiece.hasSpun(board);
				this.board.spin = spin;
				break;
			case "r":
				this.board = new Board();
				this.stats = new GameStats();
				break;
			case "f":
				if (this.player.isPresent() && last != null) {
					this.player.get().special.activate(this, last, this.board.fallingpiece.hasSpun(board));
					if (this.player.get().special.active) {
					Animation specialanim = new FadingCircleSizeAnimation(500, AnimateValue.OUTSIN, 20, Theme.PURPLE, new Posn(Board.CELL_SIZE * this.board.width / 2, Board.CELL_SIZE * this.board.height / 2), 200);
					specialanim.start();
					board.anims.add(specialanim);
					board.garbage.garbage += player.get().special.cost;
					}
				}

				
		}
		
		if (player.isPresent()) {
			for (AbilitySlot abilityslot : player.get().abilities) {
				if (key == abilityslot.key) {

					player.get().active.add(abilityslot.ability);
				}
			}
		}
		
	}
	public void onKeyReleased(String key) {
		switch (key) {
		case "down": this.sdfactive = false;
		case "left": this.arractiveleft = false; this.leftkeypressed = false; keyheldtime = Optional.empty();
		case "right": this.arractiveright = false; this.rightkeypressed = false; keyheldtime = Optional.empty();
		break;
		}
	}
	
	
	public void onTick() {
		int time = (int) (System.currentTimeMillis() - this.stats.starttime);
		
		bot.makeMove(this);
		
		for (int i = 0; i < this.board.anims.size(); i++) {
				Animation a = this.board.anims.get(i);
				if (a.isEnded()) {
					this.board.anims.remove(a);
				}
		}
		
		if ((rightkeypressed || leftkeypressed) && keyheldtime.isEmpty()) {
			keyheldtime = Optional.of(System.currentTimeMillis());
		}
		boolean dascheck = (keyheldtime.isPresent())? System.currentTimeMillis() - keyheldtime.get() >= GameState.DAS : false;
		
		if (sdfactive && GameState.SDF <= 0) {
			this.board.fallingpiece.softDropInf(board);
		}
		else if (sdfactive && time % GameState.SDF == 0) {
			this.board.fallingpiece.softDrop(board);
		}
		
		if (leftkeypressed && GameState.ARR > 0 && time % GameState.ARR == 0 && dascheck) {
			this.board.fallingpiece.moveLeft(board);
		} else if (GameState.ARR <= 0 && dascheck && leftkeypressed) {
			this.board.fallingpiece.moveLeftInf(board);
		}
		
		if (rightkeypressed && GameState.ARR > 0 && time % GameState.ARR == 0 && dascheck) {
			this.board.fallingpiece.moveRight(board);
		} else if (GameState.ARR <= 0 && dascheck && rightkeypressed) {
			this.board.fallingpiece.moveRightInf(board);
		}
		
	}
	
	public void onMouseReleased(Posn p) {
		if (selectionscreenup) {
			selscreen.findClickedButton(p, this);
		}
	}

}

class Board {
	int height;
	int width;
	// residue will never be longer than boardwidth and there must be an integer for
	// each row of the board
	List<Residue[]> residue;
	int currentcombo;
	int b2b;
	Theme t;
	List<Tetrimino> queue;
	APiece fallingpiece;
	Optional<Tetrimino> hold;
	boolean pieceplaced;
	
	GarbageMeter garbage;
	ChargeMeter chargemeter;
	
	List<Double<WorldImage, Posn>> ornaments;
	List<Animation> anims;
	WorldImage bgimage;
	
	boolean spin;
	boolean gameovertrigger;

	static int CELL_SIZE = 20;

	Board() {
		this.height = 20;
		this.width = 10;
		this.residue = new ArrayList<>();
		for (int i = 0; i < this.height; i++) {
			residue.add(this.newEmptyRow());
		}
		this.currentcombo = 0;
		this.b2b = 0;
		this.t = ThemePool.FADE_THEME;
		this.queue = Queue.sevenBag();
		queue.addAll(Queue.sevenBag());
		this.fallingpiece = this.pullFromBag(queue);
		this.hold = Optional.empty();
		this.pieceplaced = false;
		this.garbage = new GarbageMeter();
		this.chargemeter = new ChargeMeter();
		this.ornaments = new ArrayList<>();
		this.anims = new ArrayList<>();
		this.bgimage = FilePaths.BGSTARRY;
	}

	//
	// CHEESE STUFF
	//
	
	Residue[] newLineOfCheese(int cheesewell) {
		Residue[] toreturn = new Residue[width];
		for (int i = 0; i < width; i++) {
			if (i == cheesewell) {
				toreturn[i] = Residue.EMPTY;
			} else {
				toreturn[i] = Residue.CHEESE;
			}
		}
		return toreturn;
	}
	
	void addCheese(int lines) {
		int well = new Random().nextInt(width - 1);
		Residue[] cheeseline = this.newLineOfCheese(well);
		for (int i = 0; i < lines; i++) {
			this.residue.remove(0);
			this.residue.add(cheeseline);
		}
	}
	
	void recieveLines(int lines) {
		this.garbage.garbage += lines;
		new AudioPlayer().play(FilePaths.RECIEVESMALL);
	}

	void placePiece(APiece p) {
		for (int i = 0; i < 4; i++) {
			Residue[] currentRow = null;
			for (int j = 0; j < 4; j++) {
				if (p.piece.first[i][j]) {
					if (currentRow == null) {
						Residue[] existing = this.residue.get(p.position.y + i);
						currentRow = existing == null ? this.newEmptyRow() : existing.clone();
						this.residue.set(i + p.position.y, currentRow);
					}
					currentRow[p.position.x + j] = TetrisUtil.tetriminoToResidue(p.identity);
				}
			}
		}
		this.fallingpiece = this.pullFromBag(queue);
	}

	boolean overlap(Posn p) {
		if (p.x >= this.width || p.x < 0) {
			return true;
		}
		switch (this.getResidueAt(p)) {
			case S, Z, L, J, O, T, I, CHEESE, FLOOR:
				return true;
			default:
				return false;
		}
	}

	void hold() {
		boolean present = hold.isPresent();
		if (present) {
			APiece heldpiece = this.tetriminoToPiece(this.hold.get());
			this.hold = Optional.of(fallingpiece.identity);
			this.fallingpiece = heldpiece;
		} else {
			this.hold = Optional.of(fallingpiece.identity);
			this.fallingpiece = pullFromBag(queue);
		}

	}

	Residue[] newEmptyRow() {
		Residue[] toreturn = new Residue[width];
		for (int i = 0; i < width; i++) {
			toreturn[i] = Residue.EMPTY;
		}
		return toreturn;
	}

	int removeRows(GameState g, Tetrimino t) {
		System.out.println(this.currentcombo);
		List<Integer> toremove = new ArrayList<>();
		for (int i = 0; i < height; i++) {
			Residue[] row = this.residue.get(i);
			if (!TetrisUtil.contains(row, Residue.EMPTY)) {
				toremove.add(i);
			}
		}
		
		boolean addcombo = false;
		 if (toremove.size() <= 0) {
			 this.currentcombo = 0;
			this.garbage.recieve(this);
			return 0;
		} else {
			addcombo = true;
		}
		
		for (Integer r : toremove) {
			this.removeLine(r, g);
		}
		
		boolean addb2b = toremove.size() == 4 || spin;
		
		if (!addb2b) {
			b2b = 0;
		}
		
		int atk = AttackTable.applyCombo(this.currentcombo, this.b2b,
				new Double<Integer, Boolean>(toremove.size(), g.spin));
		if (this.residue.stream().allMatch(a -> TetrisUtil.containsAll(a, Residue.EMPTY))) {
			atk += 10;
		}
		
		if (addb2b) {
			this.b2b += 1;
		}
		
		if (g.player.isPresent() && g.player.get().active.size() > 0) {
			for (Ability ability : g.player.get().active) {
				atk = ability.effect(g, atk, t);
			}
			
		}
		
		Color textcolor = Color.BLACK;
		if (g.player.isPresent() && g.player.get().special.active) {
			textcolor = Theme.PURPLE;
			g.player.get().special.effect(atk, g, t, g.spin);
		}

		
		
		Posn placeat = new Posn(this.width / 2 * CELL_SIZE, toremove.get(0) * CELL_SIZE);
		int displayatk = atk;
		if (atk != 0) {
			if (System.currentTimeMillis() - g.stats.decostarttime < GameState.DECO_DURATION && ornaments.size() > 0) {
				displayatk += g.storedval;
			}
			g.storedval += atk;
		g.stats.decostarttime = System.currentTimeMillis();
		int txtsize = new Random().nextInt(Math.min(70, displayatk * 5), Math.min(80, displayatk * 5 + 10));
		int rot = new Random().nextInt(-70, 70);
		Double<WorldImage, Posn> toput = new Double<WorldImage, Posn>(new RotateImage(new OverlayImage(
				new TextImage("" + displayatk, txtsize, textcolor), 
				new TextImage("" + displayatk, txtsize + 5, Color.WHITE)), rot), placeat);
		if (this.ornaments.size() > 0) {
			ornaments.set(0, toput);
		} else {
			ornaments.add(toput);
		}
		}
		
		if (displayatk >= 16) {
			new AudioPlayer().play(FilePaths.AUDIO + "thundermini.wav");
			Animation flashlow = new Flashwave(placeat);
			flashlow.start(placeat);
			this.anims.add(flashlow);
		}
		else if (displayatk >= 10) {
			new AudioPlayer().play(FilePaths.AUDIO + "thundermini.wav");
			Animation flashlow = new Flashwave(placeat);
			flashlow.start(placeat);
			this.anims.add(flashlow);
		}
		
		if (addcombo) {
			this.currentcombo += 1;
		}
		
		
		if (g.bot instanceof BeatmapSingerBot) {
			if (g.player.isPresent()) {
				if (g.player.get().special.active) {
					this.chargemeter.power += atk;
				} else {
					this.garbage.garbage -= atk;
				}
			} else {
				this.garbage.garbage -= atk;
			}
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

	void removeLine(int y, GameState g) {
		residue.remove(y);
		residue.add(0, this.newEmptyRow());
		g.stats.lines += 1;
		
		
	}

	Residue getResidueAt(Posn p) {
		if (p.y >= this.height || p.x < 0) {
			return Residue.FLOOR;
		}
		if (p.y >= this.residue.size() || p.y < 0) {
			return Residue.EMPTY;
		}
		if (p.x >= this.width) {
			return Residue.EMPTY;
		}

		return this.residue.get(p.y)[p.x];
	}

	WorldImage drawResidue(Residue r) {
		if (r.equals(Residue.S)) {
			return this.t.s;
		}
		if (r.equals(Residue.Z)) {
			return this.t.z;
		}
		if (r.equals(Residue.O)) {
			return this.t.o;
		}
		if (r.equals(Residue.I)) {
			return this.t.i;
		}
		if (r.equals(Residue.J)) {
			return this.t.j;
		}
		if (r.equals(Residue.L)) {
			return this.t.l;
		}
		if (r.equals(Residue.T)) {
			return this.t.t;
		}
		if (r.equals(Residue.CHEESE)) {
			return this.t.cheese;
		}
		return this.t.empty;
	}

	WorldImage pieceToImage(APiece a) {
		if (a instanceof SPiece) {
			return this.t.s;
		}
		if (a instanceof ZPiece) {
			return this.t.z;
		}
		if (a instanceof OPiece) {
			return this.t.o;
		}
		if (a instanceof IPiece) {
			return this.t.i;
		}
		if (a instanceof JPiece) {
			return this.t.j;
		}
		if (a instanceof LPiece) {
			return this.t.l;
		}
		if (a instanceof TPiece) {
			return this.t.t;
		}
		return this.t.empty;
	}

	APiece pullFromBag(List<Tetrimino> q) {
		Tetrimino pull = q.get(0);
		this.queue.remove(0);
		this.advanceQueue();
		return this.tetriminoToPiece(pull);
	}

	APiece tetriminoToPiece(Tetrimino pull) {
		Posn spawn = new Posn(this.width / 2 - 2, 0);
		switch (pull) {
			case S:
				APiece spiece = new SPiece(spawn);
				gameovertrigger = spiece.checkOverlap(this, spiece.piece.first, new Posn(0,0));
				return new SPiece(spawn);
			case Z:
				APiece zpiece = new ZPiece(spawn);
				gameovertrigger = zpiece.checkOverlap(this, zpiece.piece.first, new Posn(0,0));
				return zpiece;
			case L:
				APiece lpiece = new LPiece(spawn);
				gameovertrigger = lpiece.checkOverlap(this, lpiece.piece.first, new Posn(0,0));
				return lpiece;
			case J:
				APiece jpiece = new JPiece(spawn);
				gameovertrigger = jpiece.checkOverlap(this, jpiece.piece.first, new Posn(0,0));
				return jpiece;
			case O:
				APiece opiece = new OPiece(spawn);
				gameovertrigger = opiece.checkOverlap(this, opiece.piece.first, new Posn(0,0));
				return opiece;
			case I:
				APiece ipiece = new IPiece(spawn);
				gameovertrigger = ipiece.checkOverlap(this, ipiece.piece.first, new Posn(0,0));
				return ipiece;
			default:
				APiece tpiece = new TPiece(spawn);
				gameovertrigger = tpiece.checkOverlap(this, tpiece.piece.first, new Posn(0,0));
				return tpiece;
		}

	}

	void advanceQueue() {
		if (this.queue.size() <= 7) {
			this.queue.addAll(Queue.sevenBag());
		}
	}
}

class Double<X, Y> {
	X first;
	Y second;

	Double(X f, Y s) {
		this.first = f;
		this.second = s;
	}
}
