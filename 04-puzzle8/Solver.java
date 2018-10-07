import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Iterator;
import java.util.NoSuchElementException;

// find a solution to the initial board (using the A* algorithm)
public class Solver {
    private MinPQ<Node> q;
    private Board[] solution;

    private int moves;

    // Element of Priority Queue
    private class Node implements Comparable<Node>{
        Board board;
        Board predecessor;
        int movesMade;
        int manhattan;
        Node(Board b, int m, Board p) {
            this.board = b;
            this.movesMade = m;
            this.predecessor = p;
            this.manhattan = b.manhattan();
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.movesMade + this.manhattan, o.movesMade + o.manhattan);
        }
    }

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        this.q = new MinPQ<>();
        Node n = new Node(initial, 0, null);
        q.insert(n);
    }

    public boolean isSolvable() {
        return !(moves() < 0);
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solution != null) return moves;

        Node n = q.delMin();
        while (!n.board.isGoal()) {
            for (Board neighbor : n.board.neighbors()) {
                if (neighbor.equals(n.predecessor)) continue;
                q.insert(new Node(neighbor, n.movesMade+1, n.board));
            }
            n = q.delMin();
        }
        moves = n.movesMade;
        solution = new Board[moves];
        for (int j = moves-1; j >= 0; j--) {
            solution[j] = n.board;
            n.board = n.predecessor;
        }
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return () -> new Iterator<Board>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < moves;
            }

            @Override
            public Board next() {
                if (!hasNext()) throw new NoSuchElementException();
                return solution[index++];
            }
        };
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}