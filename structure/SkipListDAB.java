// An implementation of SkipLists.
// (c) The Great Class of Fall 2013, especially <your name here>

// Various external interfaces and classes
import structure5.*;
import java.util.Random;
import java.util.Iterator;
import java.util.Scanner;

/**
 * A class that carries data and one or more pointers to next items
 * in singly linked lists.  The number of lists that this node participates
 * in is the "height" of the node.
 *
 * @version (c) 2013
 */
class SkipListNode<K>
{
    /** random number generator for determining node heights */
    private static Random r = new Random();
    /** data held by this node */
    private K data;
    /** the pointers to the next nodes in participating lists */
    private Vector<SkipListNode<K>> next;
    
    /**
     * Constructs a SkipListNode.
     * @pre 0 < p < 1
     * @post constructs a node with random height; all nexts are null
     */
    public SkipListNode(K data, double p) {
	this.data = data;
	int height = randomHeight(p);
	next = new Vector<SkipListNode<K>>();
	for (int i = 0; i < height; i++) {
	    next.add(null);
	}
    }

    /**
     * Default constructor; constructs a dummy node.
     *
     * @post constructs a node suitable to be the head node
     */
    public SkipListNode() {
	// construct a dummy node
	this(null,0.5);
    }

    /**
     * Data associated with this node
     * @return data stored within the node.
     */
    public K data()
    {
	return this.data;
    }

    /**
     * Get the next reference from this node in list i.
     * @param i the linked list to be followed
     * @pre 0 <= i < height()
     * @return the next node in the i'th linked list
     */
    public SkipListNode<K> next(int i)
    {
	return next.get(i);
    }

    /**
     * Get the next reference from this node in list i.
     * @param i the linked list to be followed
     * @pre 0 <= i < height()
     * @return the next node in the i'th linked list
     */
    public SkipListNode<K> setNext(int i, SkipListNode<K> node)
    {
	return next.set(i,node);
    }

    /**
     * Returns the height of this node.
     * @post Returns the height of this node.
     * @return height of node - number of lists this node participates in.
     */
    public int height()
    {
	return this.next.size();
    }

    /**
     * Ensure that the height of this node is at least h.
     * @pre h > 0
     * @post height() >= h
     */
    public void ensureHeight(int h)
    {
	int currH = height();
	Assert.pre(data() == null, "Ensure height only called on dummy node.");
	if (currH < h) {
	    int i;
	    for (i = currH; i < h; i++) {
		next.add(null);
	    }
	}
    }	

    /**
     * Select a random height for this node.  Every node has height 1 or 
     * greater.  p percent of the nodes have height 2 or greater. etc.
     * @pre 0 < p < 1
     * @post returns a height > 0
     * @return randomly selected node height
     */
    private static int randomHeight(double p)
    {
	int h = 1;
	while (r.nextDouble() < p) {
	    // make it higher!
	    h++;
	}
	return h;
    }

    /**
     * Generate the string representation of this node.
     * @post Returns the string representation of the data at this node.
     * @return string representing node contents.
     */
    public String toString()
    {
	if (data() == null) {
	    return "null";
	} else {
	    return data().toString();
	}
    }
}

/**
 * A class that implements an OrderedStructure<K> of values, provided
 * K is Comparable.
 *
 * The expected performance of a SkipList is O(log n) for contains, add,
 * get, and remove.
 */
public class SkipList<K extends Comparable<K>> 
    extends AbstractStructure<K> implements OrderedStructure<K>
{
    /** the probability that a node in a list is in the next higher list */
    private double p;
    /** the head -- a dummy node -- that holds pointers to heads of all lists*/
    private SkipListNode<K> head;
    /** the number of elements stored in this SkipList */
    private int size;

    /**
     * Construct an empty SkipList whose lists decrease in length by a
     * a factor of p (expected).
     */
    public SkipList(double p)
    {
	this.p = p;
	this.size = 0;
	this.head = new SkipListNode<K>(); // create a dummy header node
    }

    /**
     * Construct a skip list with log2(n) access times.
     */
    public SkipList()
    {
	this(0.5);
    }

    /**
     * Return the number of lists supporting this skip list.
     * @post height() >= 1
     * @return number of lists that are rooted in the head node of this SkipList
     */
    public int height()
    {
	return head.height();
    }

    /**
     * Returns a Vector of predecessors of any node containing value.
     * If value is not in the list, these predecessors indicate the nodes
     * that might be updated by the addition of value.  If value is in the
     * list it can be found in the node referenced by result.get(0).next.get(0)
     *
     * @post returns a vector of height() SkipListNodes that fall before any node with value
     * @return a Vector of SkipListNodes that might need updating
     */
    private Vector<SkipListNode<K>> findPredecessors(K value)
    {
	// start searching at the dummy node, at height h-1
	SkipListNode<K> current = this.head;
	int h = height();
	Vector<SkipListNode<K>> result = new Vector<SkipListNode<K>>();
	result.setSize(h);
	do {
	    h--;
	    while (current.next(h) != null &&
		   current.next(h).data().compareTo(value) < 0) {
		current = current.next(h);
	    }
	    result.set(h,current);
	} while (h > 0);
	return result;
    }

    /**
     * Returns true iff value is in this SkipList
     * @post returns true iff value is in this
     * @return true if value in list, otherwise false
     */
    public boolean contains(K value)
    {
	Vector<SkipListNode<K>> pred = findPredecessors(value);
	SkipListNode ideal = pred.get(0).next(0);
	return ideal != null &&
	       ideal.data().equals(value);
    }

    /**
     * Add value to this SkipList
     *
     * @param value to be added
     * @post value has been added
     */
    public void add(K value)
    {
	SkipListNode<K> newNode = new SkipListNode<K>(value, p);
	int h = newNode.height();
	head.ensureHeight(h);

	Vector<SkipListNode<K>> pred = findPredecessors(value);
	// ensure the dummy node can handle this new height node
	for (int i = 0; i < h; i++) {
	    newNode.setNext(i,pred.get(i).next(i));
	    pred.get(i).setNext(i,newNode);
	}
	size++;
    }
    
    /**
     * Removes value from this skip list
     * @param value the value to be removed
     * @post removes the first instance of value, if any, from this list
     * @return the value removed
     */
    public K remove(K value)
    {
	Vector<SkipListNode<K>> pred = findPredecessors(value);
	SkipListNode<K> target = pred.get(0).next(0);
	if (target != null &&
	    target.data().equals(value)) {
	    for (int i = 0; i < target.height(); i++) {
		pred.get(i).setNext(i, target.next(i));
		target.setNext(i, null);
	    }
       	    size--;
	    return target.data();
	} else {
	    return null;
	}
    }

    /**
     * Remove all values from this skip list.
     */
    public void clear()
    {
	size = 0;
	head = new SkipListNode<K>();
    }

    /**
     * Return the number of values found in this SkipList.
     * @return the number of data values found in this SkipList.
     */
    public int size()
    {
	return size;
    }

    /**
     * Return iterator associated with this list.	
     * @post returns new iterator to travers this list.
     * @return iterator that traverses this list.
     */
    public Iterator<K> iterator()
    {
	return new SkipListIterator<K>(this.head);
    }

    /**
     * Return a string representing the contents of this SkipList.
     * @post Return a string representing this SkipList.
     * @return string that represents the contents of this SkipList.
     */
    public String toString()
    {
	String result = "<SkipList:";
	for (K x : this) {
	    result += " " + (x == null ? "null" : x.toString());
	}
	result += ">";
	return result;
    }
}

/**
 * A class that supports the traversal of SkipList elements.
 */
class SkipListIterator<K extends Comparable<K>> extends AbstractIterator<K> 
{
    /** the subordinate SkipList being traversed */
    private SkipListNode<K> targetHead;

    /** the next node to be visited */
    protected SkipListNode<K> current;

    /**
     * Construct an iterator based on a SkipList.
     * @param target the subordinate SkipList
     * @pre target is non-null
     */
    public SkipListIterator(SkipListNode<K> head) {
	targetHead = head;
	current = targetHead.next(0);
    }

    /**
     * Return the iterator to its initial state.
     */
    public void reset()
    {
	current = targetHead.next(0);
    }

    /**
     * Returns true iff some nodes have not yet been visited.
     * @post true iff there are some nodes that have yet to be visited.
     * @return true iff there are unvisited nodes
     */
    public boolean hasNext()
    {
	return current != null;
    }
    
    /**
     * Get the current node value in the SkipList; the traversal is not advanced.
     * @pre hasNext()
     * @post returns the value at the current node
     * @return the value at the current node
     */
    public K get()
    {
	return current.data();
    }

    /**
     * Return the next unvisited element and advance the iterator.
     * @pre hasNext()
     * @post returns the value at the current point of the traversal
     * @return the value at the current node
     */
    public K next()
    {
	K result = current.data();
	current = current.next(0);
	return result;
    }
}