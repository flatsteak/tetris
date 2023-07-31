import { FilePaths } from '@/constants';
import { AudioPlayer } from '@/ui/AudioPlayer';

type TableKey = [number, boolean];
type TableEntry = [TableKey, number];

function buildMap(...entries: TableEntry[]) {
  const map: Record<string, number> = {};
  entries.forEach(([[n, b], value]) => {
    map[`${n}${b}`] = value;
  });
  return map;
}

const B2B_MAX = 6;
const B2B_TOP = 24;
const B2B_MID = 13;
const B2B_LOW = 6;

export class AttackTable {
  static table = buildMap(
    [[1, false], 0],
    [[2, false], 1],
    [[3, false], 2],
    [[4, false], 4],
    [[1, true], 2],
    [[2, true], 4],
    [[3, true], 6],
  );

  static getLinesSent(clear: TableKey): number {
    if (clear[0] == 4) {
      return 4;
    }
    if (clear[1]) {
      if (clear[0] == 1) {
        new AudioPlayer().play(FilePaths.audio.sfx.TSS);
      } else if (clear[0] == 2) {
        new AudioPlayer().play(FilePaths.audio.sfx.TSD);
      } else if (clear[0] == 3) {
        new AudioPlayer().play(FilePaths.audio.sfx.TST);
      }
      return clear[0] * 2;
    }
    return clear[0] - 1;
  }

  static applyCombo(combo: number, b2b: number, clear: TableKey) {
    const baseatk = AttackTable.getLinesSent(clear);
    const combocap = clear[0] == 1 && !clear[1] ? 3 : clear[0] * 2;
    const comboval = Math.floor(
      Math.min(Math.max(Math.ceil((combo * baseatk) / 4.0), 0.25 * combo), combocap),
    );

    if (clear[0] == 4 && b2b > 0) {
      new AudioPlayer().play(FilePaths.audio.sfx.CLEARQUAD);
    }

    return baseatk + comboval + Math.max(0, AttackTable.b2bFactor(b2b - 1));
  }

  static b2bFactor(b2b: number): number {
    if (b2b > B2B_TOP) {
      return B2B_MAX;
    } else if (b2b > B2B_MID) {
      return 5;
    } else if (b2b > B2B_LOW) {
      return 3;
    } else if (b2b > 1) {
      return 2;
    }
    return 0;
  }
}
