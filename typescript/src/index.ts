import { GameState } from '@/GameState';
import { FromFileImage } from 'impworld';
import { AudioPlayer } from './ui/AudioPlayer';
import { FilePaths } from './constants';

AudioPlayer.preload(
  ...Object.values(FilePaths.audio.sfx),
  ...Object.values(FilePaths.audio.songs),
)

FromFileImage.preload(
  ...Object.values(FilePaths.image.bg),
  ...Object.values(FilePaths.image.enemies),
).then(() => {
  try {
    const game = new GameState();
    game.bigBang(GameState.SCREEN_WIDTH, GameState.SCREEN_HEIGHT, GameState.GAME_SPEED);
  } catch (e) {
    console.error(e);
  }
}).catch((error) => {
  console.error('Preload failed', error);
});
