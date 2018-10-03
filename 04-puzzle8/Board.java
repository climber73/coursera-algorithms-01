public class Board {

    private int[][] blocks;
    private int n;

    public Board(int[][] blocks) {  // (where blocks[i][j] = block in row i, column j)
        n = blocks.length;
        if (n < 2 || n > 128) {
            throw new IllegalArgumentException();
        }
        int k = blocks[0].length;
        if (k != n) {
            throw new IllegalArgumentException();
        }
        this.blocks = blocks;
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int i, j, res = 0;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (blocks[i][j] == 0) continue;
                if (blocks[i][j] != i*n+j+1) res++;
            }
        }
        return res;
    }

    public int manhattan() {
        int c, x, y, res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c = blocks[i][j];
                if (c == 0) continue;
                x = Math.abs((c-1) % n - j);
                y = Math.abs((c-1) / n - i);
//                System.out.println(c + ": " + x + ", " + y);
                res += x+y;
            }
        }
        return res;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        int[][] b = blocks;
        int x = 0, y = 0, tmp, v = 0;
        boolean flag = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (b[i][j] == 0) continue;
                if (!flag) {
                    x = j;
                    y = i;
                    v = b[i][j];
                    flag = true;
                    continue;
                }
                tmp = b[i][j];
                b[i][j] = v;
                b[y][x] = tmp;
                return new Board(b);
            }
        }
        return new Board(b);
    }

    public boolean equals(Object y) {
        if (y instanceof Board) {
            Board other = (Board) y;
            if (n != other.dimension()) {
                return false;
            }
            return manhattan() == other.manhattan();
        }
        return false;
    }

//    public Iterable<Board> neighbors()     // all neighboring boards

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%3s", blocks[i][j]));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int n = 3;
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = i*n+j+1;
            }
        }
        a[0][0] = 8;
        a[2][2] = 0;
        a[2][1] = 1;
        Board b = new Board(a);
        System.out.println(b);
        System.out.println(b.twin());
        System.out.println("hamming: " + b.hamming());
        System.out.println("manhattan: " + b.manhattan());
    }
}