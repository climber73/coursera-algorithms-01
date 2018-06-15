import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int n;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    public Deque() {
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

    public void addFirst(Item item) {
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

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node<Item> oldLast = last;
        last = new Node<>();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (isEmpty())
            first = last;
        else
            oldLast.next = last;
        n++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Node<Item> removed = first;
        if (size() == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        n--;
        return removed.item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Node<Item> removed = last;
        if (size() == 1) {
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        n--;
        return removed.item;
    }

    public Iterator<Item> iterator() {

        return new Iterator<Item>() {

            private Node<Item> current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item item = current.item;
                current = current.next;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }

    public static void main(String[] args) {
        Deque<Integer> d = new Deque<>();
        d.addFirst(1);
        d.addLast(2);
        d.addLast(3);
        d.addFirst(0);
        d.removeFirst();
        d.removeLast();
        System.out.println(d);
    }
}
