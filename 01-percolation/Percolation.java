import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int openedSitesNumber = 0;
    private boolean[][] grid;
    private final int n;
    private final WeightedQuickUnionUF uf;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        if (n > 1) {
            for (int i = 1; i <= n; i++) {
                uf.union(0, i);
                uf.union(n * n + 1, n * n - n + i);
            }
        }
        grid = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }
    }

    public void open(int row, int col) {
        validate(row, col);
        if (grid[row - 1][col - 1]) return;

        grid[row - 1][col - 1] = true;
        openedSitesNumber++;
        int index = index(row, col);
        union(row, col - 1, index);
        union(row, col + 1, index);
        union(row - 1, col, index);
        union(row + 1, col, index);
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        return isOpen(row, col) && uf.find(index(row, col)) == uf.find(0);
    }

    public int numberOfOpenSites() {
        return openedSitesNumber;
    }

    public boolean percolates() {
        return uf.find(0) == uf.find(n * n + 1);
    }

    private boolean check(int row, int col) {
        return row > 0 && row <= n && col > 0 && col <= n;
    }

    private void validate(int row, int col) {
        if (check(row, col)) return;
        throw new IllegalArgumentException();
    }

    private int index(int row, int col) {
        return col + (row - 1) * n;
    }

    private void union(int r, int c, int opened) {
        if (check(r, c))
            if (grid[r - 1][c - 1])
                uf.union(index(r, c), opened);
    }

    private void printGrid() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j])
                    System.out.print("o ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }

    private void printUF() {
        System.out.println(uf.find(0));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(uf.find(i * n + j + 1) + " ");
            }
            System.out.println();
        }
        System.out.println(uf.find(n * n + 1));
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(10);
        for (int i = 1; i <= 10; i++) {
            p.open(i, 3);
            if (!p.isOpen(i, 3)) throw new RuntimeException();
        }
        p.printGrid();
        p.printUF();
    }
}
