import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

// find a solution to the initial board (using the A* algorithm)
public class Solver {
    private MinPQ<Node> q1, q2;
    private Board[] solution;

    private int moves;

    // Element of Priority Queue
    private class Node implements Comparable<Node>{
        Board board;
        Board predecessor;
        int movesMade;
        Node pre;
        Node(Board b, int m, Board p, Node pre) {
            this.board = b;
            this.movesMade = m;
            this.predecessor = p;
            this.pre = pre;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.movesMade + this.board.manhattan(), o.movesMade + o.board.manhattan());
        }
    }

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        this.q1 = new MinPQ<>();
        this.q2 = new MinPQ<>();
        q1.insert(new Node(initial, 0, null, null));
        q2.insert(new Node(initial.twin(), 0, null, null));
    }

    public boolean isSolvable() {
        return !(moves() < 0);
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solution != null) return moves;

        Node n1 = q1.delMin();
        Node n2 = q2.delMin();
        while (!n1.board.isGoal() && !n2.board.isGoal()) {
            for (Board neighbor : n1.board.neighbors()) {
                if (neighbor.equals(n1.predecessor)) continue;
                q1.insert(new Node(neighbor, n1.movesMade+1, n1.board, n1));
            }
            n1 = q1.delMin();

            for (Board neighbor : n2.board.neighbors()) {
                if (neighbor.equals(n2.predecessor)) continue;
                q2.insert(new Node(neighbor, n2.movesMade+1, n2.board, n2));
            }
            n2 = q2.delMin();
        }
        if (n1.board.isGoal()) {
            moves = n1.movesMade;
            solution = new Board[moves+1];
            Node tmp = n1;
            for (int j = moves; j >= 0; j--) {
                solution[j] = tmp.board;
                tmp = tmp.pre;
            }
        } else {
            moves = -1;
            solution = new Board[0];
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
                return index < moves+1;
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