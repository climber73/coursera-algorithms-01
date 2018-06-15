import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int n;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    public RandomizedQueue() {
        first = null;
        last = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    private Node<Item> find(int index) {
        int i;
        Node<Item> current;
        if (index < n/2 ) {
            i = 0;
            current = first;
            while (i < n && i != index) {
                current = current.next;
                i++;
            }
            return current;
        } else {
            i = n - 1;
            current = last;
            while (i >= 0 && i != index) {
                current = current.prev;
                i--;
            }
            return current;
        }
    }

     private Node<Item> findExcept(int index, Item[] except) {
        int i = 0;
        Node<Item> current = first;
        while (i < n && i < index) {
            current = current.next;
            i++;
        }
        while (i < n && contain(except, current.item)) {
            current = current.next;
            i++;
        }
        if (i == n) return null;
        else return current;
    }

    private boolean contain(Item[] arr, Item e) {
        for (Item item : arr) {
            if (item == null) return false;
            if (item.equals(e)) return true;
        }
        return false;
    }

    private void remove(Node<Item> current) {
        Node<Item> tmp;
        if (current == first) {
            tmp = first.next;
            tmp.prev = null;
            first = tmp;
        } else if (current == last) {
            tmp = last.prev;
            tmp.next = null;
            last = tmp;
        } else {
            tmp = current.prev;
            tmp.next = current.next;
            tmp = current.next;
            tmp.prev = current.prev;
        }
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node<Item> second = first;
        first = new Node<>();
        first.item = item;
        first.next = second;
        first.prev = null;
        if (second == null)
            last = first;
        else
            second.prev = first;
        n++;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Node<Item> found;
        if (n == 1) {
            found = first;
            first = last = null;
        } else {
            int i = StdRandom.uniform(n);
            found = find(i);
            remove(found);
        }
        n--;
        return found.item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return find(StdRandom.uniform(n)).item;
    }

    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int showed = 0;
        private Item[] showedItems = (Item[]) new Object[n];

        @Override
        public boolean hasNext() {
            return showed < n;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node<Item> found = findExcept(StdRandom.uniform(n - showed), showedItems);
            showedItems[showed] = found.item;
            showed++;
            return found.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(902);
        System.out.println(rq.sample());
//        RandomizedQueue<String> queue = new RandomizedQueue<>();
////        String[] a = new String[10];
//        for (int i = 0; i < 10; i++) {
//            queue.enqueue(Integer.toString(i));
////            a[i] = Integer.toString(i);
//            System.out.print(i);
//        }
//        System.out.println();

//        a[0] = "9";
//        a[1] = "7";
//        System.out.println("except array: ");
//        for (String s:a) {
//            System.out.print(s);
//        }
//        System.out.println();
//        Node node = queue.findExcept(2, a);
//        if (node == null) System.out.println(node);
//        else System.out.println(node.item);

//        for (String s : queue) {
//            System.out.println(s);
//        }
    }
}
