//A program to test LLRBIterator
//(c) 2014 Michael Shaw

import java.util.*;

public class IteratorTest {
    

    public static void main (String args[]) {
	/*
	Random intgen = new Random();
	
	LLRB<Integer> tree = new LLRB<Integer>();
	int x = intgen.nextInt(100);
	int count = 0;
	while (count < 10) {
	    System.out.println(x);
	    tree.add(x);
	    x = intgen.nextInt(100);
	    count++;
	}	       
	
	System.out.println("-----------------------------------------------");
	
	Iterator iterator = tree.iterator();

	while (iterator.hasNext()) System.out.println(iterator.next());

	System.out.println("----------------------------------------------");

	System.out.println(tree.toString());

	System.out.println("");
	System.out.println("Next section");
	System.out.println("");
	

	tree = new LLRB<Integer>();
	
	for(int i = 0; i < 20; i++) {
	    tree.add(i);
	}

	System.out.println("-----------------------------------------------");

	iterator = tree.iterator();
	
	while (iterator.hasNext()) {
	    int y = (Integer)iterator.next();
	    System.out.println(y);
	    tree.remove(y);
	    System.out.println(tree.toString());	
	}

	

	System.out.println(tree);

	while (iterator.hasNext()) {
	    int y = (Integer)iterator.next();
	    if (y == 3) {
		tree.remove(y);
		//System.out.println(iterator);
	    }
	    if (y > 3) System.out.println(y);
	    //System.out.println(iterator);
	}
	System.out.println(tree);
	*/

	testRemove(56, 5000);
    }


    public static void testRemove(int val, int size) {
	ArrayList <Integer> list = new ArrayList<Integer>();
	LLRB<Integer> tree = new LLRB<Integer>();
	Iterator iterator;

	for(int i = 0; i < size; i++) {
	    list.add(i);
	}
	Collections.shuffle(list);

	for(int i = 0; i < size; i++) {
	    tree.add(list.get(i));   
	}

	//System.out.println(tree);

	//first check if iterator works
	iterator = tree.iterator();
	for(int i = 0; i < size; i++) {
	    if(iterator.hasNext()){
		if((Integer)iterator.next() != i) {
		    System.out.println("Initial iteration failed");
		    return;
		}
	    }
	}
	System.out.println("Iteration passed.");

	//next check a single remove
	iterator = tree.iterator();
	while(iterator.hasNext()){
	    int y = (Integer)iterator.next();
	    if(y == val) {
		tree.remove(y);
		break;
	    }
	}

	//System.out.println(tree);

	for(int i = val + 1; i < size; i++){
	    if(iterator.hasNext()){
		int v = (Integer)iterator.next();
		
		if(v != i) {
		    System.out.println(v);
		    System.out.println("Remove iteration failed.");
		    return;
		}
		
	    }
	}
	System.out.println("Successfully removed " + val + " while iterating, size: " + size);
    }
}