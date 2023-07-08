package tetris;

import java.awt.Color;
import java.util.*;

import javalib.worldimages.*;

interface SingerBot {
	WorldImage getSinger();
	void makeMove(GameState g);
	boolean isDead();
	String getSong();
	WorldImage sendLines(int lines);
	Posn getScreenPosn();
}

class FriendlySingerBot implements SingerBot {
	long time;
	String name;
	List<String> speechpool;
	
	static List<String> NAMEPOOL = List.of("Mr. Singer", " c Mr. Dancer", "Sing Sing", "Sing Dance");
	static List<String> SPEECHPOOL = new ArrayList<String>(Arrays.asList("Keep going!",
			"Good job!",
			"why u bad :sob:"));
	static int SPEECH_INTERVAL = 1000;
	
	FriendlySingerBot() {
		this.name = NAMEPOOL.get(new Random().nextInt(NAMEPOOL.size() - 1));
		this.speechpool = new ArrayList<String>();
		speechpool.addAll(SPEECHPOOL);
		this.time = System.currentTimeMillis();
	}
	
	public void makeMove(GameState g) {
		
	}



	public WorldImage getSinger() {
		return new FromFileImage(FilePaths.IMGS + "singerhappy.png");
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSong() {
		// TODO Auto-generated method stub
		return null;
	}

	public WorldImage sendLines(int lines ) {
		return null;
	}

	@Override
	public Posn getScreenPosn() {
		return new Posn(GameState.SCREEN_WIDTH - (int) (this.getSinger().getWidth()) / 2, GameState.SCREEN_HEIGHT / 2);
	}
}

class HostileSingerBot implements SingerBot {
	long time;
	
	int aps; // attack per second
	
	public void makeMove(GameState g) {
		if (time == 0.0) {
			//b.addCheese(aps);
		}
	}
	
	HostileSingerBot(int aps) {
		this.aps = aps;
		this.time = System.currentTimeMillis();
	}

	@Override
	public WorldImage getSinger() {
		return null;
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSong() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorldImage sendLines(int lines) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Posn getScreenPosn() {
		return new Posn(GameState.SCREEN_WIDTH - (int) (this.getSinger().getWidth()) / 2, GameState.SCREEN_HEIGHT / 2);
	}
}

class VSingerBot implements SingerBot {
	long starttime;
	
	int maxhealth;
	int attack; // in lines per attack
	int rate; // in ms
	int health; // lines sent before victory
	
	static int WARNING_TIME = 100;
	public void makeMove(GameState g) {
		if (System.currentTimeMillis() - starttime >= rate) {
			g.board.recieveLines(attack);
			starttime = System.currentTimeMillis();
			
		} else if (System.currentTimeMillis() - starttime >= rate - WARNING_TIME) {
			Animation toadd = new FadingCircleSizeAnimation(500, AnimateValue.OUTSIN, 40, Color.WHITE, new Posn(DifficultyPool.singerpos.x, DifficultyPool.singerpos.y), 150);
			toadd.start();
			g.board.anims.add(toadd);
		}
	}
	
	VSingerBot(int atk, int rate, int hp) {
		this.attack = atk;
		this.health = hp;
		this.maxhealth = hp;
		this.rate = rate;
		this.starttime = System.currentTimeMillis();
	}

	public WorldImage getSinger() {
		float healthamt = (this.health + 0.0f) / (this.maxhealth + 0.0f);
		WorldImage singer = new FromFileImage(FilePaths.IMGS + "singerhappy.png");
		WorldImage healthbar = new RectangleImage((int) (singer.getWidth() * healthamt), Board.CELL_SIZE, OutlineMode.SOLID, Color.RED);
		return new AboveAlignImage(AlignModeX.LEFT, singer, healthbar);
	}

	public boolean isDead() {
		return health <= 0;
	}

	@Override
	public String getSong() {
		// TODO Auto-generated method stub
		return null;
	}

	public WorldImage sendLines(int lines) {
		health -= lines;
		return new RotateImage(new EquilateralTriangleImage(this.getSinger().getWidth(), OutlineMode.OUTLINE, Color.RED), 20);
	}
	
	public Posn getScreenPosn() {
		return new Posn(GameState.SCREEN_WIDTH - (int) (this.getSinger().getWidth()) / 2, GameState.SCREEN_HEIGHT / 2);
	}
}

class Interval {
	int interval;
	int lines;
	boolean called;
	Optional<Animation> anim;
	Optional<WorldImage> bgchange;
	
	Interval(int time, int lines, Animation a, WorldImage img) {
		this.interval = time;
		this.lines = lines;
		this.called = false;
		this.anim = Optional.of(a);
		this.bgchange = Optional.of(img);
	}
	
	Interval(int time, int lines, Animation a) {
		this.interval = time;
		this.lines = lines;
		this.called = false;
		this.anim = Optional.of(a);
		this.bgchange = Optional.empty();
	}
	
	Interval(int time, int lines, WorldImage img) {
		this.interval = time;
		this.lines = lines;
		this.called = false;
		this.anim = Optional.empty();
		this.bgchange = Optional.of(img);
	}
	
	Interval(int time, int lines) {
		this.interval = time;
		this.lines = lines;
		this.called = false;
		this.anim = Optional.empty();
	}
	
	
	
}

class BeatmapSingerBot implements SingerBot {
	List<Interval> lines;
	boolean end;
	String song;
	
	BeatmapSingerBot(List<Interval> lines, String song) {
		this.lines = lines;
		this.end = false;
		this.song = song;
	}
	public WorldImage getSinger() {
		return FilePaths.SINGERHAPPY;
	}

	public void makeMove(GameState g) {
		for (Interval i : lines) {
			if (!i.called && System.currentTimeMillis() - g.stats.starttime >= i.interval) {
				g.board.recieveLines(i.lines);
				if (i.anim.isPresent()) {
					Animation a = i.anim.get(); 
					a.start();
					g.board.anims.add(a);
				}
				i.called = true;
				return;
			}
		}
	}
	
	public void makeMove(GameState g, int start) {
		for (Interval i : lines) {
			if (!i.called && System.currentTimeMillis() - start >= i.interval) {
				g.board.recieveLines(i.lines);
				if (i.anim.isPresent()) {
					Animation a = i.anim.get(); 
					a.start();
					g.board.anims.add(a);
				}
				if (i.bgchange.isPresent()) {
					g.board.bgimage = i.bgchange.get();
				}
				i.called = true;
				return;
			}
		}
	}

	public boolean isDead() {
		boolean toreturn = this.end;
		this.end = lines.stream().allMatch(interval -> interval.called);
		return toreturn;
	}
	
	public String getSong() {
		return song;
	}


	public WorldImage sendLines(int lines) {
		return new RectangleImage(0, 0, OutlineMode.SOLID, Color.BLACK);
	}
	
	public Posn getScreenPosn() {
		return new Posn(GameState.SCREEN_WIDTH - (int) (this.getSinger().getWidth()) / 2, GameState.SCREEN_HEIGHT / 2);
	}
	
}

