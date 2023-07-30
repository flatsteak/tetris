export class CycleIndex {
  first: boolean[][];
  left: boolean[][];
  right: boolean[][];
  flip: boolean[][];

  constructor(n: boolean[][], l: boolean[][], r: boolean[][], f: boolean[][]) {
      this.first = n;
      this.left = l;
      this.right = r;
      this.flip = f;
  }

  cycleRight() {
      let rotfirst = [...this.first];
      let rotleft = [...this.left];
      let rotright = [...this.right];
      let rotflip = [...this.flip];
      this.first = rotright;
      this.right = rotflip;
      this.flip = rotleft;
      this.left = rotfirst;
  }

  cycleLeft() {
      let rotfirst = [...this.first];
      let rotleft = [...this.left];
      let rotright = [...this.right];
      let rotflip = [...this.flip];
      this.first = rotleft;
      this.left = rotflip;
      this.flip = rotright;
      this.right = rotfirst;
  }

  cycleFlip() {
      this.cycleLeft();
      this.cycleLeft();
  }
}
