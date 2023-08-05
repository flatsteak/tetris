import { GameState } from '@/GameState';
import { FromFileImage } from 'impworld';

FromFileImage.preload('static/bgdefault.jpeg').then(() => {
  try {
    const game = new GameState();
    game.bigBang(GameState.SCREEN_WIDTH, GameState.SCREEN_HEIGHT, GameState.GAME_SPEED);
  } catch (e) {
    console.error(e);
  }
});
