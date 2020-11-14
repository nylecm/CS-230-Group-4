package generic_data_structures;

import java.util.TreeSet;

/**
 * A queue which is a data structure based in the FIFO principle; where the
 * presence of an element that is in a set of elements can be confirmed.
 * @author nylecm
 *
 * @param <T> the type parameter
 */
public class SearchQueue<T> extends Queue<T> {
    /**
     * Checks if an element that equals a member of a given set exits within
     * a queue.
     *
     * @param s the set of elements who you are trying to find a member of.
     * @return true if
     */
    public boolean isMemberOfPresent(TreeSet<T> s) {
        Link<T> cur = head;
        boolean isTailReached = false;

        while (!isTailReached) {
            if (s.contains(cur.getData())) {
                return true;
            }
            if (cur == tail) {
                isTailReached = true;
            } else {
                cur = cur.getNext();
            }
        }
        return false;
    }
}
