import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleLinkedList<E> implements Iterable<E> {
    Node<E> first;
    Node<E> last;

    static class Node<E> {
        E item;
        Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    static class NodeIterator<E> implements Iterator<E> {
        private Node<E> node;

        NodeIterator(Node<E> node) {
            this.node = node;
        }

        public boolean hasNext() {
            return node != null;
        }

        public E next() {
            Node<E> currentNode = node;
            if (currentNode == null) throw new NoSuchElementException();
            node = currentNode.next;
            return currentNode.item;
        }
    }

    public Iterator<E> iterator() {
        return new NodeIterator<>(first);
    }

    public void add(E element) {
        // Copied from java.util.LinkedList
        Node l = last;
        Node<E> newNode = new Node<>(element, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
    }

    public void concatWith(SimpleLinkedList other) {
        if (last != null) last.next = other.first;
        else first = other.first;

        if (other.last != null) last = other.last;
    }
}