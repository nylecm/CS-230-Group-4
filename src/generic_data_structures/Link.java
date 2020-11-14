package generic_data_structures;

/**
 * Represents a link that has an object it refers to, and a reference to another
 * link.
 *
 * @author nylecm
 * @param <E> the type parameter
 */
public class Link<E> {
    private E data;
    private Link<E> next;

    /**
     * Instantiates a new Link.
     *
     * @param data the object that the link refers to.
     * @param next the link this link refers to.
     */
    public Link(E data, Link<E> next) {
        this.data = data;
        this.next = next;
    }

    /**
     * Gets the object the link refers to.
     *
     * @return the object the link refers to.
     */
    public E getData() {
        return data;
    }

    /**
     * Sets the object the link refers to.
     *
     * @param data the object the link refers to.
     */
    public void setData(E data) {
        this.data = data;
    }

    /**
     * Gets the link this link refers to.
     *
     * @return the link this link refers to.
     */
    public Link<E> getNext() {
        return next;
    }

    /**
     * Sets the link this link refers to.
     *
     * @param next the link this link refers to.
     */
    public void setNext(Link<E> next) {
        this.next = next;
    }
}
