
//An implementation of Left leaning red black trees, based off Duane Bailey's 
//Red Black Tree and Sedgewick's LLRB.
// (c) 2014, Tony Liu and Michael Shaw
//package structure5;

import java.util.*;
import structure5.*;

/**
 * This class implements a left leaning variant of a red-black tree, holding
 * a one-to-one correspondence with 2-3 trees. It is a recursive structure,
 * with most functions utilizing both a wrapper function and a recursive function.
 * Relationships between nodes are singly linked, with no parent pointers. 
 * LLRB utilizes nodes rather than subtrees, with an internal Node class. It
 * also utilizes a version number to support modifications to the tree while
 * iterating.
 *
 * @author, 2014 tony liu, michael shaw
 * @see structure.RedBlackTree
 * @see structure.BinaryTree
 * @see structure.BinarySearchTree
 */

public class LLRB<E extends Comparable<E>> extends AbstractStructure<E> implements OrderedStructure<E>{


    /**
     * Color is stored in the child node
     */
    private static final boolean RED = true;
    private static final boolean BLACK = false; 

    private Node root;
    private int size;

    /**
     * Version number, used for the iterator
     */
    private int version;


    /**
     * Internal Node class used to hold data 
     */
    public class Node {

	/**
	 * The color of this node
	 */
	private boolean color;

	/**
	 * The left child of this node, or null
	 */
	private Node left;

	/**
	 * The right child of this node, or null
	 */
	private Node right;

	/**
	 * The value stored in this node
	 */
	private E value; 
	
	/**
	 * Constructs a single node with type E value
	 * @post Private constructor that generates a red node with no children
	 * @return A red node with given value and null leaves 
	 */
	protected Node(E value) {
	    this.color = RED; 
	    this.value = value; 
	    this.left = this.right = null;
	}
	/**
	 * Default constructor for a single onde
	 * @post Private constructor that generates a black node with no value
	 * or children
	 * @return A black node with null leaves
	 */
	protected Node() {
	    this.color = BLACK;
	    this.value = null;
	    this.left = this.right = null;
	}
	/**
	 * Returns value of node
	 * @return value of node
	 * @post returns value of node
	 */       
	protected E value(){
	    return value;
	}

	/**
	 * Returns whether node has a left child
	 * @return whether node has left child
	 * @post returns whether node has left child
	 */
	protected boolean hasLeft() {
	    return this.left != null;
	}
	
	protected boolean hasRight() {
	    return this.right != null;
	}

	protected Node left() {
	    if (this.hasLeft()) return this.left;
	    else return null;
	}

	protected Node right() {
	    if (this.hasRight()) return this.right;
	    else return null;
	}
	
    }
    
    //default constructor
    public LLRB() {
	root = null;
	size = 0;
	version = 0;
    }
    
    //root must always be black
    public LLRB(E value) {
	root = new Node(value);
	root.color = BLACK;
	size = 1;
	version = 0;
    }
    
    protected boolean isRed(Node x) {
	if (x != null) return x.color == RED;
	else return false;
    }

    public boolean isEmpty() {
	return root == null;
    }
    

    /**
     * Returns version number of the tree
     * @return version number of the tree
     * @post return version number of the tree
     */
    protected int version() {
	return version;
    }
   
    public int size() {
	return this.size;
    }


    /**
     * Internal iterator class designed for traversing LLRB.
     * LLRBIterator iterates through the tree via a stack.
     * It has the ability to remove the current value while
     * maintaining the tree traversal. This is accomplished
     * by checking its version number with the LLRB's version
     * number; if they are not the same, the iterator will
     * be reconstructed and updated. 
     *
     */
    private class LLRBIterator extends AbstractIterator<E>{

	/**
	 * Nodes are stored within a StackVector stack
	 */
	StackVector <Node> stack;

	/**
	 * LLRBIterator has a separate copy of version number
	 */
	private int iterVersion;

	/**
	 * LLRBIterator constructor, initialies stack and iterVersion
	 * and pushes all necessary nodes to begin traversal.
	 * @return an iterator with the current LLRB version number
	 * @post returns an iterator with the current LLRB version number
	 */
	public LLRBIterator() {
	    iterVersion = version();
	    stack = new StackVector<Node>();
	    stack.push(root);

	    //pushes all left children from root
	    if (root.hasLeft()) goLeft(root);
	}

	/**
	 * Checks if the iterator is on the correct version of LLRB, then
	 * gets the current node in the traversal
	 * @return the current value in the traversal
	 * @post returns the current value in the traversal
	 */
	public E get() {
	    if(!upToDate()) adjust();
	    return stack.get().value();
	}


	/**
	 * Checks if the iterator is on the correct version of LLRB, then
	 * gets the current node in the traversal
	 * @return the current value in the traversal
	 * @post returns the current value in the traversal
	 */
	public E next() {
	    if (!upToDate()) {
		System.out.println("Adjusted stack.");
		adjust();
	    }
	    Node temp =stack.pop();
	    if (temp.hasRight()) {
		stack.push(temp.right());
		goLeft(temp.right());
	    }
	    return temp.value(); 
	}
	
	public boolean hasNext() {
	    if(!upToDate()) adjust();
	    return !stack.isEmpty();
	}

	public void reset() {
	    stack = new StackVector<Node>();
	    stack.push(root);
	    goLeft(root.left());
	    
	}
	
	protected void goLeft(Node x) {
	    while (x.hasLeft()) {
		stack.push(x.left());
		x = x.left();
	    }
	}

	/**
	 * Checks if the iterator is on the correct version of LLRB
	 * @return whether iterVersion is the same as LLRB version
	 * @post returns iterVersion == LLRBVersion
	 */
	protected boolean upToDate() {
	    //System.out.println("version " + version());
	    //System.out.println("Iterator version " + iterVersion);
	    return iterVersion == version();
	}

	/**
	 * Rebuilds the iterator stack beginning at the current node
	 * @post LLRBiterator is up to date and ready for traversal
	 */
	protected void adjust() {

	    //get value to start stack again, reset stack
	    //note: can be changed to support removal of current node or next node
	    E start = get();
	    stack = new StackVector<Node>();

	    adjustHelper(root, start);

	    //update version number
	    iterVersion = version();
	}

	/**
	 * Helper function for adjust
	 * @pre Node x is not null, value start is not null
	 * @post LLRBiterator is updated and ready for traversal
	 */
	protected void adjustHelper(Node x, E start){
	    if(x == null) throw new Error("Adjust failed.");

	    int cmp = start.compareTo(x.value());

	    if(cmp < 0){
		stack.push(x);
		adjustHelper(x.left(), start);
	    }
	    //if start is larger, already visited
	    else if(cmp > 0) adjustHelper(x.right(), start);
	    else stack.push(x);
	}
    }
    /**
     * Returns an iterator for this particular LLRB
     * @return a LLRBiterator for the tree
     * @post returns a LLRBiterator for the tree
     */
    public Iterator<E> iterator() {
	return new LLRBIterator();
    }
    

    /**
     * Clears the entire tree
     * @post The root of the tree is null.
     */
    public void clear() {
	root = null;
    }

    public String toString() {
	return recursivePrint(root);
    }
    /**
     * Protected recursive helper for toString()
     * @pre begins recursive call at root
     * @return String representation of LLRB
     * @post returns a String representation of LLRB
     */
    protected String recursivePrint(Node h) {
	if (h == null) return "";
	if (isRed(h)) return "(" + recursivePrint(h.left()) + h.value() + recursivePrint(h.right()) + ")"; 
	else return "[" + recursivePrint(h.left()) + h.value() + recursivePrint(h.right()) + "]";
    }

    /**
     * Performs a search within LLRB on whether it contains type E value
     * @pre value is not null
     * @return whether the value is present within the tree
     * @post returns a boolean on whether the value is present within the tree
     */
    public boolean contains(E value) { 
	Node x = root;
	while (x != null) {
	    int cmp = value.compareTo(x.value());
	    if(cmp == 0) return true;
	    else if(cmp < 0) x = x.left;
	    else x = x.right;
	}
	return false;
    }
    /**
     * A fundamental function. Reverses the colors of Node h and its two 
     * children. Called when h's children are both red.
     * @pre Node h's children are red
     * @post Node h is red, its two children are black
     */
    protected void colorFlip(Node h) {
	h.color = !h.color;
	h.left.color = !h.left.color;
	h.right.color = !h.right.color;
    }
    /** 
     * A fundamental function. Rotates left about Node h, exchanging the
     * colors of its two children. Usually called when h's right child is red.
     * @pre Node h's right child is red
     * @return the new parent node (Node h's old right child)
     * @post returns a new parent node, with the left child being red
     */
    protected Node rotateLeft(Node h) {
	Node x = h.right;
	h.right = x.left;
	x.left = h;
	x.color = h.color;
	h.color = RED;
	return x; 
    }

    /** 
     * A fundamental function. Rotates right about Node h, exchanging the
     * colors of its two children. Usually called when h's left child and its left
     * child are both red (two reds in a row).
     * @pre Node h's left child and left child's left child are red
     * @return the new parent node (Node h's old left child)
     * @post returns a new parent node, with the right child being red
     */
    protected Node rotateRight(Node h) {
	Node x = h.left;
	h.left = x.right; 
	x.right = h;
	x.color = h.color;
	h.color = RED;
	return x; 
    }
    /**
     * Returns the root of the tree.
     * @return Node root
     * @post returns the Node root
     */
    public Node root() {
	return root;
    }

    public E get(E value) {
	Node x = root;
	while(x != null) {
	    int cmp = value.compareTo(x.value());
	    if(cmp == 0) return x.value();
	    else if (cmp < 0) x = x.left;
	    else if(cmp > 0) x = x.right;
	}

	return null;
    }

    /**
     * Adds type E value into the tree
     * @pre value is not null
     * @post value is added into the tree
     */
    public void add(E value) {
	root = addRecursive(root, value);
	root.color = BLACK;
	version++;
    }
    /**
     * Private recursive helper function to add().
     * @pre value is not null
     * @post value is inserted into the tree at correct location,
     * and the tree is rebalanced
     */
    private Node addRecursive(Node h, E value) {
	if (h == null) {
	    size++;
	    return new Node(value);
	}
	int compare = value.compareTo(h.value);
	if (compare < 0) h.left = addRecursive(h.left, value);
	if (compare > 0) h.right = addRecursive(h.right, value);
	//Is this necessary? No duplicates:
	//if (value.compareTo(h.value) == 0) h.val = value;   

	if (isRed(h.right) && (!isRed(h.left))) h = rotateLeft(h);
	if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
	if (isRed(h.left) && isRed(h.right)) colorFlip(h); 

	return h;
    }
    /**
     * A fundamental function. Called on the way down the tree
     * during remove to ensure remove ends on a red node. Pulls a
     * red node from Node h's right child and makes it the left
     * child.
     * @pre if neither h's left child or its left child's left child is red
     * @return Node h
     * @post returns Node h, which now has a red left child
     */
    protected Node moveRedLeft(Node h) {
	colorFlip(h);
	if(isRed(h.right.left)) {
	    h.right = rotateRight(h.right);
	    h = rotateLeft(h);
	    colorFlip(h);
	}
	return h;
    }

    /**
     * A fundamental function. Called on the way down the tree
     * during remove to ensure remove ends on a red node. Pulls a
     * red node from Node h's left child's left child and makes it 
     * the right child.
     * @pre if neither h's right child or its right child's left child is red
     * @return Node h
     * @post returns Node h, which now has a red right child
     */
    protected Node moveRedRight(Node h) {
	colorFlip(h);
	if(isRed(h.left.left)){
	    h = rotateRight(h);
	    colorFlip(h);
	}
	return h;
    }
    /**
     * Removes the minimum value from the tree
     * @post minimum value of the tree is removed
     */
    private void removeMin() {
	root = removeMinRecursive(root);
	root.color = BLACK;
    }

    /** 
     * Recursive helper function for removeMin. Also called
     * in remove when replacing a removed internal node.
     * @post the minimum node within a given subtree is removed
     */
    private Node removeMinRecursive(Node h) {
	if (h.left == null) {
	    size--;
	    return null;
	}	

	if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
	
	h.left = removeMinRecursive(h.left);
	
	return fixUp(h);
    }
    /**
     * Removes type E value from the tree.
     * @return type E value, null if value is not within the tree
     * @post value is removed from the tree and returned
     */
    public E remove(E value) {
	if (!contains(value)) return null;
	root = removeRecursive(root, value);
	if(root != null) root.color = BLACK;
	version++;
	return value; 
    }
    /**
     * Recursive helper function for remove().
     */
    private Node removeRecursive(Node h, E value){

	if(value.compareTo(h.value()) < 0) {
	    if(!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
	    h.left = removeRecursive(h.left, value); 
	}

	else {
	    if(isRed(h.left))
		h = rotateRight(h);
	    //found it, no replacement needed
	    if((value.compareTo(h.value) == 0) && (h.right == null)){
		size--;
		return null;
	    }
	    //if there is no red node to rotate into down the right path
	    if(!isRed(h.right) && !isRed(h.right.left))
		h = moveRedRight(h);
	    //found it, need to replace with successor
	    if(value.compareTo(h.value) == 0){
		h.value = min(h.right).value();
		h.right = removeMinRecursive(h.right);
	    }
	    //continue down the path on right
	    else h.right = removeRecursive(h.right, value);
	}
	return fixUp(h);
    }

    //duplicate code from add
    /**
     * Helper function for removeRecursive(). Fixes the tree using
     * the same process as insert.
     */
    private Node fixUp(Node h) {
	
	if(isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
	if(isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
	if(isRed(h.left) && isRed(h.right)) colorFlip(h);
	
	return h;
    }
    /**
     * Recursively returns the minimum value of a given subtree.
     * @return the minimum Node
     * @post returns the minimum value node
     */
    public Node min(Node h){
	if(h.left == null) return h;

	return min(h.left);
    }
    /**
     * Returns the height of the tree
     * @return height of the tree
     * @post returns the height of the tree
     */
    public int height() {
	return heightRecursive(root);
    }

    /**
     * Recursive helper function for height().
     */
    protected int heightRecursive(Node h) {
	if(h == null) return -1;
	
	return 1 + Math.max(heightRecursive(h.left()), heightRecursive(h.right()));
    }

}