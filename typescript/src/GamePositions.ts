export class GamePositions {
  constructor(public readonly width: number, public readonly height: number) {}

  get boardColumns() {
    return 10;
  }

  get boardRows() {
    return 20;
  }

  get cellSize() {
    return 30;
  }

  get meterSpacing() {
    return this.cellSize * 2;
  }

  get firstMeterSpacing() {
    return this.cellSize * 4;
  }

  get boardLeft() {
    return this.width / 2 - this.cellSize * this.boardColumns / 2;
  }

  get boardRight() {
    return this.width / 2 + this.cellSize * (this.boardColumns + 1)/ 2;
  }

  get boardTop() {
    return 50;
  }

  get boardBottom() {
    return this.boardTop + this.cellSize * (this.boardRows + 1);
  }
}
