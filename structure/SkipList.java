// An implementation of SkipLists.
// (c) The Great Class of Fall 2013, especially Michael Shaw

// Various external interfaces and classes
import structure5.*;
import java.util.Random;
import java.util.Iterator;
import java.util.Scanner;
import java.util.*;

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
    private ArrayList<SkipListNode<K>> next;
    
    /**
     * Constructs a SkipListNode.
     * @pre 0 < p < 1
     * @post constructs a node with random height; all nexts are null
     */
    public SkipListNode(K data, double p) {
	this.data = data;
	int height = randomHeight(p);
	next = new ArrayList<SkipListNode<K>>();
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
	data = null;
	next = new ArrayList<SkipListNode<K>>();
	next.add(null);
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
	if (hasNext()) {
	    K element = current.data();
	    current  = current.next(0);
	    return element; 
	} else {
	    return null; 
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
	// create a dummy header node (intially: height 1)
	this.head = new SkipListNode<K>();
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
	// Head is a dummy node in all linked lists; there are height of these
	return head.height();
    }

    /**
     * Returns a ArrayList of predecessors of any node containing value.
     * If value is not in the list, these predecessors indicate the nodes
     * that might be updated by the addition of value.  If value is in the
     * list it can be found in the node referenced by result.get(0).next.get(0)
     *
     * @post returns a vector of height() SkipListNodes that fall before any node with value
     * @return a ArrayList of SkipListNodes that might need updating
     */
    private ArrayList<SkipListNode<K>> findPredecessors(K value)
    {
	// Searches the SkipList for the node prior to the value for each list (levels 0 through height()-1)
	// Starts at head node and goes through each list, starting at height()-1
	// After each execution of the loop, starts searching for next predecessor at predecessor of the previous level 
	
       	//ArrayList that holds predecessor nodes
	ArrayList<SkipListNode<K>> predecessors = new ArrayList<SkipListNode<K>>(height());

	SkipListNode<K> curr = head; //Current node being examined, initialized to head

	//Traverse each i-level list, starting at i = height()-1
	for (int i = height()-1; i>=0; i--) {
	    //While curr is not the last node in the i list and its next(i) reference is less than value, advance curr to its next(i) reference
	    while (curr.next(i) != null && curr.next(i).data().compareTo(value) < 0) {
		curr = curr.next(i); //curr.next(i) is a potential predecessor, so curr is set to it
	    }
	    //Once curr is the last node in the i list or its next(i) reference is >= value, add curr to predecessors at index i  
	    predecessors.add(0,curr); 
	}
	return predecessors; //Return predecessors list
	
    }

    /**
     * Returns true iff value is in this SkipList
     * @post returns true iff value is in this
     * @return true if value in list, otherwise false
     */
    public boolean contains(K value)
    {
	ArrayList<SkipListNode<K>> pred = findPredecessors(value); //Predecessors
	SkipListNode<K> predNode = pred.get(0); //Predecessor at level 0

	//If the node is not the last in the list and its data is equal to value, contains is true. 
	//If the node is the last in the list or the value after predNode does not equal value, contains must be false
	return (predNode.next(0) != null) && (predNode.next(0).data().compareTo(value) == 0);
    }

    /**
     * Add value to this SkipList
     *
     * @param value to be added
     * @post value has been added
     */
    public void add(K value)
    {
	SkipListNode<K> newNode = new SkipListNode<K>(value, p); //New node with random height
	int h = newNode.height(); 
	head.ensureHeight(h); 
	ArrayList<SkipListNode<K>> pred = findPredecessors(value); //ArrayList of predecessors
	SkipListNode<K> predNode = new SkipListNode<K>();

	//For each sub-list, adjusts next of newNode and next of predecessor
	while (h > 0) {
	    predNode = pred.get(h-1);
	    newNode.setNext(h-1, predNode.next(h-1)); //Set next of newNode at level h-1 to h-1 predecessor's next at level h-1
	    predNode.setNext(h-1, newNode); //Set the h-1 predecessor's next reference to newNode at level h-1
	    h--; //Decrement h
	}
	size++; //Increment size to indicate new node
    }
    
    /**
     * Removes value from this skip list
     * @param value the value to be removed
     * @post removes the first instance of value, if any, from this list
     * @return the value removed
     */
    public K remove(K value)
    {
	ArrayList<SkipListNode<K>> pred = findPredecessors(value);
	SkipListNode<K> predNode = pred.get(0); //Predecessor at level 0
	SkipListNode<K> removal = predNode.next(0);
	
	//Checks same condition as contains method. 
	//This method avoids calling contains because that would require calling findPredecessors twice
	if ((predNode.next(0) != null) && (predNode.next(0).data().compareTo(value) == 0)) {
	    
	    //Loop executes for each predecessor
	    for (int i = 0; i<removal.height(); i++) {
		predNode = pred.get(i); //predNode is predecessor at level i
		predNode.setNext(i, removal.next(i)); //Sets next of predNode at level i to node after the node being removed 
	    }
	    size --; //Decrements size to indicate one fewer node
	    return value; //Returns removed value
	} else {
	    return null; //Returns null if the value is not in the SkipList
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
     * @post returns new iterator to traverse this list.
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


    public static void main(String[] args){
	SkipList<Integer> skip = new SkipList<Integer>(0.5);
	int size = 500000;
	System.out.println("Number of elements: " + size);

	System.out.println("");
		
	ArrayList<Integer> ints = new ArrayList<Integer>();
	for (int i = 0; i<size; i++){
	    ints.add(i);
	}
	Collections.shuffle(ints);
	
	int start = (int)System.currentTimeMillis();
	for (int x = 0; x<ints.size(); x++) {
	    skip.add(ints.get(x));
	}
	int end = (int)System.currentTimeMillis();
	int dif = end-start;
	System.out.println("Adding took: " + dif + " milliseconds");	


	System.out.println("");

	start = (int)System.currentTimeMillis();
	for (int x = 0; x<ints.size(); x++) {
	    if (! skip.contains(ints.get(x))) System.out.println("Error: does not contain all ints");
	}
	end = (int)System.currentTimeMillis();
	dif = end-start;
	System.out.println("Contains took: " + dif + " milliseconds");

	System.out.println("");

	start = (int)System.currentTimeMillis();
	for (int x = 0; x<ints.size(); x++) {
	    skip.remove(ints.get(x));	   
	}
	end = (int)System.currentTimeMillis();
	dif = end-start;
	System.out.println("Removing took: " + dif + " milliseconds");
    }



}

