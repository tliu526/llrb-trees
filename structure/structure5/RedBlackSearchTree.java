// This is an implementation of binary search trees.
// (c) 1998, 2001 duane a. bailey
package structure5;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Random;

public class RedBlackSearchTree<E extends Comparable<E>> extends AbstractStructure<E> implements OrderedStructure<E>
{
    protected RedBlackTree<E> root;

    protected int count;


    public RedBlackSearchTree()
    {
        root = new RedBlackTree<E>();
        count = 0;
    }

    public int height() {
	return root.height();
    }

    public boolean isEmpty()
    {
        return root.isEmpty();
    }

    public void clear()
    {
        root = new RedBlackTree<E>();
        count = 0;
    }


    public int size()
    {
        return count;
    }
    
    public void add(E value)
    {
        //Assert.pre(value instanceof Comparable,"value must implement Comparable");
        root = root.add(value);
        count++;
    }

    public E remove(E value){
        // Assert.pre(value instanceof Comparable,"value must implement Comparable");
        if (root.contains(value)){
            root = root.remove(value);
            count--;
            return value;
        }
        return null;
    }


    public boolean contains(E value){
        //Assert.pre(value instanceof Comparable,"value must implement Comparable");
        return root.contains(value);
    }
    
    public boolean isRedBlack()
    {
        return root.consistency();
    }
  
    public Iterator<E> iterator()
    {
        return root.iterator();
    }

    public String treeString(){
        return root.treeString();
    }

    public String toString(){
        return root.toString();
    }
    
    public int hashCode(){
        return root.hashCode();
    } 
}