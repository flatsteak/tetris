package tetris;

public class Main {
	public static void main(String[] args) {
		GameState n = new GameState();
		n.bigBang(Board.CELL_SIZE * n.board.width + 400, Board.CELL_SIZE * n.board.height + 400, 0.5);
	}
}
