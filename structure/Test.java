/*
Simple tests for LLRB.
1. Generate 1 million integers, randomize the order
2. Insert 1 million values, record time
3. Ask for 1 million values, record time
4. Remove all values in random order
5. Repeat 4 times
(c) 2014 Tony Liu
*/
    import java.util.*;
import structure5.*;

public class Test {
    
    public static void main(String args[]) {
	RedBlackSearchTree<Integer> RBTree = new RedBlackSearchTree<Integer>();
	LLRB<Integer> llrb = new LLRB<Integer>();
	ArrayList<Integer> list = new ArrayList <Integer>();
	ArrayList<OrderedStructure> structs = new ArrayList<OrderedStructure>();
	BinarySearchTree<Integer> bst = new BinarySearchTree <Integer>();
	SplayTree<Integer> splay = new SplayTree<Integer>();
	//SkipList<Integer> skip = new SkipList <Integer>();

	int size = 2000000;
	
	
	//initialize and randomize the list
	for(int i = 0; i < size; i++) {list.add(i);}
	Collections.shuffle(list);
	
	structs.add(RBTree);
	structs.add(llrb);
	structs.add(bst);
	structs.add(splay);
	//structs.add(skip);
	  
	
	
	

	System.out.println("Order: \n RedBlack Tree \n LLRB \n Binary Search Tree \n SplayTree");
	
	System.out.println();
	height(list);
	System.out.println();
  
	runTests(structs, list);
	
    }	
    
    
    public static void runTests(ArrayList<OrderedStructure> structs, ArrayList data) {
	
	for(int i = 0; i < structs.size(); i++){
	    populate(structs.get(i),data);
	}
	    System.out.println();	
	    Collections.shuffle(data);

	for(int i = 0; i < structs.size(); i++){
	    find(structs.get(i),data);
	}
	    System.out.println();	
	    Collections.shuffle(data);

	for(int i = 0; i < structs.size(); i++){
	    delete(structs.get(i),data);
	}
	    System.out.println();	

    }
    
    public static void populate(OrderedStructure struct, ArrayList data) {
	int totalTime = 0;
	int startTime = 0;
	int endTime = 0;
	int duration = 0;
	
	for (int x = 0; x<4; x++) {
	    
	    startTime = (int)System.currentTimeMillis();
	    for(int i = 0; i < data.size(); i++) {
		struct.add(data.get(i));
	    }
	    endTime = (int)System.currentTimeMillis();
	    duration = endTime - startTime;
	    totalTime = totalTime + duration;
	    
	    for (int i = 0; i < data.size(); i++) {
		struct.remove(data.get(i));
	    }
	}
	
	for (int i = 0; i < data.size(); i++) {
	    struct.add(data.get(i));
	}

	double averageTime = totalTime/4.0;
	

	System.out.println("Populating on average took " + averageTime + " milliseconds."); 
    }
    
    public static void find(OrderedStructure struct, ArrayList data) {
	int totalTime = 0;
	int startTime = 0;
	int endTime = 0;
	int duration = 0;
	
	for (int x = 0; x<4; x++) {
	    
	    startTime = (int)System.currentTimeMillis();
	    for(int i = 0; i < data.size(); i++) {
		if(!struct.contains(data.get(i))){
		    System.out.println("Failed to find number.");
		    return;
		}
	    }
	    endTime = (int)System.currentTimeMillis();
	    duration = endTime - startTime;
	    
	    totalTime = totalTime + duration;
	    
	}
	double averageTime = totalTime/4.0;
	System.out.println("Contains on average took " + averageTime + " milliseconds."); 	
    }
    
    public static void delete(OrderedStructure struct, ArrayList data) {
	int totalTime = 0;
	int startTime = 0;
	int endTime = 0;
	int duration = 0;
	
	for (int x = 0; x<4; x++) {
	    
	    startTime = (int)System.currentTimeMillis();
	    for(int i = 0; i < data.size(); i++) {
		struct.remove(data.get(i));
	    }
	    endTime = (int)System.currentTimeMillis();
	    duration = (endTime - startTime);	
       	        
	    if (!struct.isEmpty()) System.out.println("Failed to remove all values");
	    totalTime = totalTime + duration;

	    for (int i = 0; i < data.size(); i++) {
		struct.add(data.get(i));
	    }
	}
	
	double averageTime = totalTime/4.0;
	System.out.println("Delete on average took " + averageTime + " milliseconds"); 
    }

    public static void height(ArrayList<Integer> data){
	ArrayList<Double> heights = new ArrayList<Double>();
	for (int x = 0; x<4; x++) heights.add(0.0);
	for (int x=0; x<4; x++) {
	    RedBlackSearchTree<Integer> rb = new RedBlackSearchTree<Integer>();
	    LLRB<Integer> llrb = new LLRB<Integer>();
	    BinarySearchTree <Integer> bst = new BinarySearchTree<Integer>();
	    SplayTree<Integer> splay = new SplayTree<Integer>();
	    
	    for(int i = 0 ; i < data.size(); i++) {
		rb.add(data.get(i));
		llrb.add(data.get(i));
		bst.add(data.get(i));
		splay.add(data.get(i));
	    }
	    
	    
	    heights.set(0, heights.get(0) + rb.height());
	    heights.set(1, heights.get(1) + llrb.height());
	    heights.set(2, heights.get(2) + bst.height());
	    heights.set(3, heights.get(3) + splay.height());
	    
	}

	for (int x=0; x<4; x++) {
	    heights.set(x, heights.get(x)/4.0);
	}

        System.out.println("RB height average: " + heights.get(0));
	System.out.println("LLRB height average: " + heights.get(1));
	System.out.println("BST height average: " + heights.get(2));
	System.out.println("Splay Tree height average: " + heights.get(3));
    }
    
}





