import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {

    private int[][] blocks;
    private int n;
    private int i0 = n, j0 = n;

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
                if (blocks[i][j] == 0) {
                    i0 = i; j0 = j;
                    continue;
                }
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
                if (c == 0) {
                    i0 = i; j0 = j;
                    continue;
                }
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
        int[][] b = createNewBlocks();
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

    public Iterable<Board> neighbors() {
        findZeroPosition();
        Board[] neighbors = new Board[4];
        for (int i = 0; i < 4; i++) {
            neighbors[i] = move(i);
        }

        return () -> new Iterator<Board>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                while (index < 4) {
                    if (neighbors[index] != null) break;
                    index++;
                }
                return index < 4;
            }

            @Override
            public Board next() {
                if (!hasNext()) throw new NoSuchElementException();
                Board next = neighbors[index];
                index++;
                return next;
            }
        };
    }

    // 0 - left, 1 - up, 2 - right, 3 - down
    private Board move(int direction) {
        int[][] arr;
        switch (direction) {
            case 0:
                if (j0 - 1 < 0) return null;
                arr = createNewBlocks();
                arr[i0][j0] = arr[i0][j0-1];
                arr[i0][j0-1] = 0;
                return new Board(arr);
            case 1:
                if (i0 - 1 < 0) return null;
                arr = createNewBlocks();
                arr[i0][j0] = arr[i0-1][j0];
                arr[i0-1][j0] = 0;
                return new Board(arr);
            case 2:
                if (j0 + 1 >= n) return null;
                arr = createNewBlocks();
                arr[i0][j0] = arr[i0][j0+1];
                arr[i0][j0+1] = 0;
                return new Board(arr);
            case 3:
                if (i0 + 1 >= n) return null;
                arr = createNewBlocks();
                arr[i0][j0] = arr[i0+1][j0];
                arr[i0+1][j0] = 0;
                return new Board(arr);
            default: return null;
        }
    }

    private int[][] createNewBlocks() {
        int[][] arr = new int[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(blocks[i], 0, arr[i], 0, n);
        return arr;
    }

    private void findZeroPosition() {
        if (i0 == n || j0 == n)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (blocks[i][j] == 0) {
                        i0 = i;
                        j0 = j;
                    }
    }

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

        for (Board z : b.neighbors()) {
            System.out.println("n1: " + z);
        }
    }
}