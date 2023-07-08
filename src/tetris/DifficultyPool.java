package tetris;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javalib.worldimages.Posn;

public class DifficultyPool {
	static BeatmapSingerBot mtsingerbot = new BeatmapSingerBot(List.of(), "");
	static Posn singerpos = new Posn(GameState.SCREEN_WIDTH - (int) mtsingerbot.getSinger().getWidth() / 2, GameState.SCREEN_HEIGHT - (int) mtsingerbot.getSinger().getWidth() / 2);
	static BeatmapSingerBot CURTAIN_CALL_SS = new BeatmapSingerBot(new ArrayList<Interval>(Arrays.asList(
			new Interval(5500, 4, new InverseLinearFlash(Color.WHITE, 3400).withPosn(LinearFlash.FLASH_POSN)),
			new Interval(8900, 2, new LinearFlash(Color.WHITE, 100).withPosn(LinearFlash.FLASH_POSN), FilePaths.BGSTARRY),
			new Interval(9240, 1),
			new Interval(9480, 3, new CircleSizeAnimation(1000, (a) -> (float) Math.sin(a * Math.PI), 40, Color.WHITE, singerpos, 500)),
			new Interval(9720, 0, new FadingCircleSizeAnimation(500, AnimateValue.OUTSIN, 40, Color.WHITE, new Posn(singerpos.x - 100, singerpos.y - 100), 150)),
			new Interval(9840, 0, new FadingCircleSizeAnimation(500, AnimateValue.OUTSIN, 40, Color.WHITE, new Posn(singerpos.x + 100, singerpos.y + 100), 150)),
			new Interval(10060, 6, new FadingRotatingTriangleAnimation(500, AnimateValue.ARCTAN, 500, Color.WHITE, singerpos, 150, 100)),
			new Interval(10800, 2),
			new Interval(12000, 4))), FilePaths.AUDIO + "CurtainCall.wav");
	
	static VSingerBot VS_A = new VSingerBot(4, 10000, 40);
}



