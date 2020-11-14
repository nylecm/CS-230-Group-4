package generic_data_structures; //todo place silk bag in silk bag package.

import java.util.NoSuchElementException;

/**
 * The type Queue, a data structure based in the FIFO principle.
 *
 * @author nylecm
 * @param <T> the type parameter
 */
public class Queue<T> {
    /**
     * The front of the queue (item present for longest time).
     */
    protected Link<T> head;
    /**
     * The back of the queue (item present for shortest time).
     */
    protected Link<T> tail;

    /**
     * Instantiates a new empty Queue.
     */
    public Queue() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Adds a new element to the back of the queue.
     *
     * @param e the new element that is being enqueued.
     */
    public void enqueue(T e) {
        Link<T> newItem = new Link<T>(e, null);
        if (isEmpty()) {
            head = newItem;
            tail = newItem;
        } else {
            Link<T> prevTail = tail;
            prevTail.setNext(newItem);
            tail = newItem;
        }
    }

    /**
     * Removes the item at the front of the queue.
     */
    public void dequeue() throws NoSuchElementException {
        if (!isEmpty()) {
            if (head == tail) {
                tail = null;
                head = null;
            } else {
                head = head.getNext();
            }
        } else {
            throw new NoSuchElementException("Queue is empty, cannot dequeue!");
        }
    }

    /**
     * Peeks the front of the queue.
     *
     * @return the item at the front of the queue.
     */
    public T peek() {
        if (!isEmpty()) {
            return head.getData();
        } else {
            throw new NoSuchElementException("Queue is empty, cannot peek!");
        }
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true then the queue is empty (has no elements).
     */
    public boolean isEmpty() {
        return (head == null) && (tail == null);
    }

    /**
     * The entry point of application. //todo remove psvm ...
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Queue<String> q = new Queue<>();

        q.enqueue("President");
        q.enqueue("Vice-President");
        q.enqueue("President of the Senate");
        q.enqueue("Secretary of State");
        q.enqueue("Secretary of the Treasury");

        System.out.println(q.isEmpty());

        System.out.println(q.peek());
        q.dequeue();

        System.out.println(q.peek());
        q.dequeue();

        System.out.println(q.peek());
        q.dequeue();

        System.out.println(q.peek());
        q.dequeue();

        System.out.println(q.peek());

        try {
            q.dequeue();
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(q.isEmpty());

        q.enqueue("X");

        System.out.println(q.peek());
        q.dequeue();

        System.out.println(q.isEmpty());
    }
}
