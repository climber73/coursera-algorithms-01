import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private double[] x;
    private double mean = -1;
    private double stddev = -1;
    private final int n;
    private final int trials;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.n = n;
        this.trials = trials;
        this.x = new double[trials];
    }

    private void run() {
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                p.open(row, col);
            }
            x[i] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    public double mean() {
        if (mean >= 0) return mean;
        mean = StdStats.mean(x);
        return mean;
    }

    public double stddev() {
        if (stddev >= 0) return stddev;
        stddev = StdStats.stddev(x);
        return stddev;
    }

    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Expected 2 args");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        stats.run();
        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
