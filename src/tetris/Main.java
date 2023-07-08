package tetris;

public class Main {

	public static void main(String[] args) {
		GameState n = new GameState();
		n.bigBang(GameState.SCREEN_WIDTH, GameState.SCREEN_HEIGHT, GameState.GAME_SPEED);
		String toplay = (n.bot instanceof BeatmapSingerBot) ? n.bot.getSong() : FilePaths.BGSONG.first;
		new AudioPlayer().playAccurateLoop(toplay, n);
	}
}
