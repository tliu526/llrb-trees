//An in-order iterator for traversal of LLRB trees
// (c) 2014 Michael Shaw

import structure5.*;

public class LLRBIterator {
    StackVector stack = new StackVector(); //Holds values in level-order                                                                            

    //Calls add method to initialize stack                                                                                                          
    protected LLRBIterator() {
	add(root);
    }

    //Pushes all values on to a stack for a level-order traversal                                                                                   
    protected void add(Node x) {
	if (x.right != null) add(x.right);
	stack.push(x);
	if (x.left != null) add(x.left);
    }

    //Current node returned                                                                                                                         
    protected Node get() {
	return stack.get();
    }

    //Progresses traversal and returns current node                                                                                                 
    protected Node next() {
	Node temp =stack.pop();
	return temp;
    }

    public LLRBIterator iterator() {
	return new LLRBIterator();
    }

}
