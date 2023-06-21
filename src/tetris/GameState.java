package tetris;

import java.util.*;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

enum Residue {
	S, Z, I, O, T, L, J, CHEESE, EMPTY, FLOOR
}

class GameStats {
	int lines;
	int score;
	double time;
	
	GameStats(int lines, int score, double time) {
		this.lines = lines;
		this.score = score;
		this.time = time;
	}
	
	GameStats() {
		this(0, 0, 0.0);
	}
}

class GameState extends World {
	Board board;
	Ruleset rules;
	GameStats stats;
	
	

	
	GameState() {
		this.board = new Board();
		this.rules = new Ruleset(RuleType.LINES, 40);
		this.stats = new GameStats();
	}
	
	
	public WorldScene makeScene() {
		int width = Board.CELL_SIZE * this.board.width;
		int height = Board.CELL_SIZE * this.board.height;
		WorldScene s = new WorldScene(width, height);
		for (int i = 0; i < board.height; i++) {
			s.placeImageXY(Arrays.stream(this.board.residue.get(i + 1))
				.reduce(this.board.drawResidue(Residue.EMPTY), (row, cell) -> new BesideImage(row, this.board.drawResidue(cell)), (a, b) -> a), board.width / 2 * Board.CELL_SIZE + Board.CELL_SIZE / 2, i * Board.CELL_SIZE + Board.CELL_SIZE / 2);
		}
		this.board.fallingpiece.drawPiece(this.board.t, s, this.board.pieceToImage(this.board.fallingpiece));
		s.placeImageXY(this.board.t.holdbox, (int) (width + this.board.t.holdbox.getWidth() / 2) + Board.CELL_SIZE / 2, (int) this.board.t.holdbox.getHeight() / 2);
		if (this.board.hold.isPresent()) {
			APiece p = this.board.tetriminoToPiece(this.board.hold.get());
			p.position = new Posn((int) (width + this.board.t.holdbox.getWidth() / 2 - Board.CELL_SIZE / 8) / Board.CELL_SIZE,  (int) (this.board.t.holdbox.getHeight() / 2) / Board.CELL_SIZE - 1);
			p.drawPiece(this.board.t, s, this.board.pieceToImage(p));
		}
		return s;
	}
	
	
	public void onKeyEvent(String key) {
		switch (key) {
		case "left": this.board.fallingpiece.moveLeft(board);
		break;
		case "right": this.board.fallingpiece.moveRight(board);
		break;
		case "down": this.board.fallingpiece.softDrop(board);
		break;
		case " ": this.board.fallingpiece.hardDrop(board);
		break;
		case "c": this.board.hold();
		break;
		case "up": if (!this.board.fallingpiece.checkOverlap(board, this.board.fallingpiece.piece.right, new Posn(0, 0))) {
			this.board.fallingpiece.rotate(Rotation.CLOCKWISE);
		}
		break;
		case "z":if (!this.board.fallingpiece.checkOverlap(board, this.board.fallingpiece.piece.left, new Posn(0, 0))) {
			this.board.fallingpiece.rotate(Rotation.COUNTERCLOCKWISE);
		}
		break;
		case "a": if (!this.board.fallingpiece.checkOverlap(board, this.board.fallingpiece.piece.flip, new Posn(0, 0))) {
			this.board.fallingpiece.rotate(Rotation.FLIP);
		}
		break;
		}
	}
	
	
	
}

class Board {
	int height;
	int width;
	// residue will never be longer than boardwidth and there must be an integer for each row of the board
	Map<Integer, Residue[]> residue;
	int currentcombo;
	int b2b;
	Theme t;
	List<Tetrimino> queue;
	APiece fallingpiece;
	Optional<Tetrimino> hold;
	
	
	static int CELL_SIZE = 20;
	
	Board() {
		this.height = 20;
		this.width = 10;
		this.residue = new HashMap<>();
		for (int i = 0; i < this.height; i++) {
			residue.put(i + 1, this.newEmptyRow());
		}
		this.currentcombo = 0;
		this.b2b = 0;
		this.t = ThemePool.OUTLINE_THEME;
		this.queue = Queue.sevenBag();
		queue.addAll(Queue.sevenBag());
		System.out.println(this.queue);
		this.fallingpiece = this.pullFromBag(queue);
		this.hold = Optional.empty();
	}
	
	void addCheese(int lines) {
		Map<Integer, Residue[]> toreturn = new HashMap<>();
		for (int i = 0; i < lines; i++) {
			toreturn.put(i + lines, this.residue.get(i + 1));
		}
		this.residue = toreturn;
	}
	
	void placePiece(APiece p) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (p.piece.first[i][j]) {
					Residue[] newrow = (this.residue.get(i + 1) == null) ? this.newEmptyRow() : this.residue.get(i + 1).clone() ;
					newrow[j + p.position.x] = TetrisUtil.tetriminoToResidue(p.identity);
					this.residue.put(i + p.position.y, newrow);
				}
			}
		}
		this.fallingpiece = this.pullFromBag(queue);
	}
	
	boolean overlap(Posn p) {
		if (p.x > this.width - 1 || p.x < 1) {
			return true;
		}
		switch (this.getResidueAt(p)) {
		case S, Z, L, J, O, T, I, CHEESE, FLOOR: return true;
		default: return false;
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
		Residue[] toreturn = new Residue[width - 1];
		for (int i = 0; i < width - 1; i++) {
			
			toreturn[i] = Residue.EMPTY;
		}
		return toreturn;
	}
	
	int removeRows() {
		List<Integer> toremove = new ArrayList<>();
		for (int i = 0; i < height; i++) {
			Residue[] row = this.residue.get(i);
			if (!(row == null)) {
				toremove.add(i);
			}
		}
		for (Integer r : toremove) {
			this.removeLine(r);
		}
		return AttackTable.applyCombo(this.currentcombo, this.b2b, new Double<Integer, Boolean>(toremove.size(), this.fallingpiece instanceof TPiece));
	}
	
	void removeLine(int y) {
		for (int i = 0; i < height - 1; i++) {
			Residue[] above = residue.get(i + 2);
			residue.remove(i + 1);
			residue.put(i + 1, above);
		}
		residue.put(height, new Residue[width]);
	}
	
	Residue getResidueAt(Posn p) {
 
		if (p.y + 1 > this.height) {
			return Residue.FLOOR;
		}
		if (this.residue.get(p.y) == null) {
			return Residue.EMPTY;
		}
		
		return this.residue.get(p.y + 1)[p.x];
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
		System.out.println("pulled");
		return this.tetriminoToPiece(pull);
	}
	
	APiece tetriminoToPiece(Tetrimino pull) {
		Posn spawn = new Posn(this.width / 2 - 1, 0);
		switch (pull) {
		case S: return new SPiece(spawn);
		case Z: return new ZPiece(spawn);
		case L: return new LPiece(spawn);
		case J: return new JPiece(spawn);
		case O: return new OPiece(spawn);
		case I: return new IPiece(spawn);
		default: return new TPiece(spawn);
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

class RunGame {
	void testChess(Tester t) {
		GameState n = new GameState();
		n.bigBang(Board.CELL_SIZE * n.board.width + 400, Board.CELL_SIZE * n.board.height + 400, 0.5);
	}
}
	

