const audioCache = new Map<string, HTMLAudioElement>();

export class AudioPlayer {
  static preload(...urls: string[]) {
    urls.forEach((url) => {
      if (!audioCache.has(url)) {
        const audio = new Audio();
        audio.src = url;
        audioCache.set(url, audio);
      }
    });
  }

  static play(url: string) {
    if (!audioCache.has(url)) {
      this.preload(url);
    }
    const audio = audioCache.get(url);
    audio?.play().catch(function (error) {
      console.error('Error playing audio:', error);
    });
  }
}
