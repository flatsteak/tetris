import { Board } from '@/board';
import { CELL_SIZE } from '@/constants';
import { Theme } from '@/themes/Theme';
import {
  AlignModeX,
  AlignModeY,
  BesideAlignImage,
  Color,
  OutlineMode,
  OverlayImage,
  OverlayOffsetAlign,
  RectangleImage,
  TextImage,
  WorldImage,
} from 'impworld';

export class GarbageMeter {
  garbage: number; // in lines
  // describes cheese in queue about to be sent.
  // ex: 2 lines of garbage in queue, meaning 2 will be accepted / cancelled at some point

  maxaccept: number; // max amt accepted garbage.
  // ex: if 20 lines are queued but maxaccept is 8, 8 lines of cheese will be added to the board per non line clear piece placed

  constructor(g: number = 0, max: number = 8) {
    this.garbage = g;
    this.maxaccept = max;
  }

  receive(b: Board): void {
    const toReceive = Math.min(this.maxaccept, Math.max(0, this.garbage));
    b.addCheese(toReceive);
    this.garbage = this.garbage - toReceive;
  }

  draw(height: number): WorldImage {
    const meter = new OverlayImage(
      new RectangleImage(CELL_SIZE, CELL_SIZE * height, OutlineMode.OUTLINE, Color.WHITE),
      new RectangleImage(CELL_SIZE, CELL_SIZE * height, OutlineMode.SOLID, Color.BLACK),
    );
    const garbagedisplay =
      this.garbage > 0
        ? new RectangleImage(
            CELL_SIZE,
            Math.min(CELL_SIZE * 20, CELL_SIZE * this.garbage),
            OutlineMode.SOLID,
            Color.RED,
          )
        : new RectangleImage(
            CELL_SIZE,
            Math.min(CELL_SIZE * 20, CELL_SIZE * -this.garbage),
            OutlineMode.SOLID,
            Theme.SKYBLUE,
          );

    const meterwithgarbage = new OverlayOffsetAlign(
      AlignModeX.CENTER,
      AlignModeY.BOTTOM,
      garbagedisplay,
      0,
      0,
      meter,
    );
    const maxbar = new RectangleImage(
      CELL_SIZE,
      CELL_SIZE * this.maxaccept,
      OutlineMode.OUTLINE,
      Color.WHITE,
    );

    if (this.garbage > 20) {
      const extragarbage = new TextImage(`+ ${this.garbage - 20}`, 30, Color.RED);
      return new BesideAlignImage(
        AlignModeY.BOTTOM,
        new OverlayOffsetAlign(
          AlignModeX.CENTER,
          AlignModeY.BOTTOM,
          maxbar,
          0,
          0,
          meterwithgarbage,
        ),
        extragarbage,
      ).movePinhole(-extragarbage.getWidth() / 2, 0);
    } else if (this.garbage < -20) {
      const extrashield = new TextImage(`+ ${-(this.garbage + 20)}`, 30, Theme.SKYBLUE);
      return new BesideAlignImage(
        AlignModeY.BOTTOM,
        new OverlayOffsetAlign(
          AlignModeX.CENTER,
          AlignModeY.BOTTOM,
          maxbar,
          0,
          0,
          meterwithgarbage,
        ),
        extrashield,
      ).movePinhole(-extrashield.getWidth() / 2, 0);
    }

    return new OverlayOffsetAlign(
      AlignModeX.CENTER,
      AlignModeY.BOTTOM,
      maxbar,
      0,
      0,
      meterwithgarbage,
    );
  }
}
