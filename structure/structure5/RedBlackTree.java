// An implementation of red-black trees.
// (c) 2000, 2001 duane a. bailey
package structure5;
import java.util.Iterator;

public class RedBlackTree<E extends Comparable<E>>
{
    protected RedBlackTree<E> left;

    protected RedBlackTree<E> right;

    protected RedBlackTree<E> parent;


    protected E value;


    protected boolean isRed;


    public static final RedBlackTree EMPTY = new RedBlackTree<String>();


    public RedBlackTree()
    {
        value = null;
        parent = null;
        left = right = this;
        isRed = false;  // empty trees below the leaves should be black
    }


    public RedBlackTree(E v)
    {
        Assert.pre(v != null, "Red-black tree values must be non-null.");
        value = v;
        parent = null;
        left = right = new RedBlackTree<E>();
        isRed = false;  // roots of tree should be colored black
    }

    public int height() {
	if(isEmpty()) return -1;
	return 1 + Math.max(left().height(), right().height());
    }


    protected boolean isRed()
    {
        return isRed;
    }


    protected boolean isBlack()
    {
        return !isRed;
    }

    protected void setRed()
    {
        isRed = true;
    }


    protected void setRed(boolean isRed)
    {
        this.isRed = isRed;
    }


    protected void setBlack()
    {
        isRed = false;
    }

    protected E value()
    {
        return value;
    }

    protected RedBlackTree<E> left()
    {
        return left;
    }


    protected RedBlackTree<E> right()
    {
        return right;
    }

    protected RedBlackTree<E> parent()
    {
        return parent;
    }


    protected void setParent(RedBlackTree<E> newParent)
    {
        parent = newParent;
    }


    protected void setLeft(RedBlackTree<E> newLeft)
    {
        if (isEmpty()) return;
        if (left.parent() == this) left.setParent(null);
        left = newLeft;
        left.setParent(this);
    }


    protected void setRight(RedBlackTree<E> newRight)
    {
        if (isEmpty()) return;
        if (right.parent() == this) right.setParent(null);
        right = newRight;
        right.setParent(this);
    }

    public boolean isLeftChild(){
        if (parent() == null) return false;
        return this == parent().left();
    }


    public boolean isRightChild(){
        if (parent() == null) return false;
        return this == parent().right();
    }

    public boolean isEmpty()
    {
        return value == null;
    }

    protected boolean isRoot()
    {
        return parent == null;
    }

    public RedBlackTree<E> root()
    {
        RedBlackTree<E> result = this;
        while (!result.isRoot()) {
            result = result.parent();
        }
        return result;
    }


    public int depth(){
        if (parent() == null) return 0;
        return 1 + parent.depth();
    }

    protected void rotateRight()
    {
        // all of this information must be grabbed before
        // any of the references are set.  Draw a diagram for help
        RedBlackTree<E> parent = parent();
        RedBlackTree<E> newRoot = left();
        // is the this node a child; if so, a right child?
        boolean wasChild = !isRoot();
        boolean wasLeftChild = isLeftChild();

        // hook in new root (sets newRoot's parent, as well)
        setLeft(newRoot.right());

        // puts pivot below it (sets this's parent, as well)
        newRoot.setRight(this);


        if (wasChild) {
            if (wasLeftChild) parent.setLeft(newRoot);
            else              parent.setRight(newRoot);
        } else Assert.post(newRoot.isRoot(),"Rotate at root preserves root.");
    }


    protected void rotateLeft()
    {
        // all of this information must be grabbed before
        // any of the references are set.  Draw a diagram for help
        RedBlackTree<E> parent = parent();  // could be null
        RedBlackTree<E> newRoot = right();
        // is the this node a child; if so, a left child?
        boolean wasChild = !isRoot();
        boolean wasRightChild = isRightChild();

        // hook in new root (sets newRoot's parent, as well)
        setRight(newRoot.left());

        // put pivot below it (sets this's parent, as well)
        newRoot.setLeft(this);

        if (wasChild) {
            if (wasRightChild) parent.setRight(newRoot);
            else               parent.setLeft(newRoot);
        } else Assert.post(newRoot.isRoot(),"Left rotate at root preserves root.");
    }


    public RedBlackTree<E> add(E c)
    {
        RedBlackTree<E> tree = insert(c);  // first, do a plain insert
        tree.setRed();  // we insert nodes as red nodes - a first guess
        tree.redFixup();  // now, rebalance the tree
        return tree.root();  // return new root
    }


    protected RedBlackTree<E> insert(E c)
    {
        // trivial case - tree was empty:
        if (isEmpty()) return new RedBlackTree<E>(c);

        // decide to insert value to left or right of root:
        if (c.compareTo(value()) < 0) {

            // if to left and no left child, we insert value as leaf 
            if (left().isEmpty()) {
                RedBlackTree<E> result = new RedBlackTree<E>(c);
                setLeft(result);
                return result;
            } else {
                // recursively insert to left
                return left().insert(c);
            }
        } else {

            // if to right and no left child, we insert value as leaf
            if (right().isEmpty()) {
                RedBlackTree<E> result = new RedBlackTree<E>(c);
                setRight(result);
                return result;
            } else {
                // recursively insert to right
                return right().insert(c);
            }
        }
    }

    public void redFixup()
    {
        if (isRoot() || !parent().isRed()) {
            // ensure that root is black (might have been insertion pt)
            root().setBlack();
        } else {
            RedBlackTree<E> parent = parent();  // we know parent exists
            // since parent is red, it is not root; grandParent exists & black
            RedBlackTree<E> grandParent = parent.parent();
            RedBlackTree<E> aunt;  // sibling of parent (may exist)

            if (parent.isLeftChild())
            {
                aunt = grandParent.right();
                if (aunt.isRed()) {
                    // this:red, parent:red, grand:black, aunt:red
                    // push black down from gp to parent-aunt, but
                    // coloring gp red may introduce problems higher up
                    grandParent.setRed();
                    aunt.setBlack();
                    parent.setBlack();
                    grandParent.redFixup();
                } else {
                    if (isRightChild()) {
                        // this:red, parent:red, grand:black, aunt:black
                        // ensure that this is on outside for later rotate
                        parent.rotateLeft();
                        parent.redFixup(); // parent is now child of this
                    } else {
                        // assertion: this is on outside
                        // this:red, parent:red, gp: black, aunt:black
                        // rotate right @ gp, and make this & gp red sibs
                        // under black parent
                        grandParent.rotateRight();
                        grandParent.setRed();
                        parent.setBlack();
                    }
                }
            } else // parent.isRightChild()
            {
                aunt = grandParent.left();
                if (aunt.isRed()) {
                    // this:red, parent:red, grand:black, aunt:red
                    // push black down from gp to parent-aunt, but
                    // coloring gp red may introduce problems higher up
                    grandParent.setRed();
                    aunt.setBlack();
                    parent.setBlack();
                    grandParent.redFixup();
                } else {
                    if (isLeftChild()) {
                        // this:red, parent:red, grand:black, aunt:black
                        // ensure that this is on outside for later rotate
                        parent.rotateRight();
                        parent.redFixup(); // parent is now child of this
                    } else {
                        // assertion: this is on outside
                        // this:red, parent:red, gp: black, aunt:black
                        // rotate right @ gp, and make this & gp red sibs
                        // under black parent
                        grandParent.rotateLeft();
                        grandParent.setRed();
                        parent.setBlack();
                    }
                }
            }
        }
    }

    public RedBlackTree<E> remove(E c)
    {
        // find the target node - the node whose value is removed
        RedBlackTree<E> target = locate(c);
        if (target.isEmpty()) return root();

        // determine the node to be disconnected:
        // two cases: if degree < 2 we remove target node;
        //            otherwise, remove predecessor
        RedBlackTree<E> freeNode;
        if (target.left().isEmpty() ||
            target.right().isEmpty()) // simply re-root tree at right
        {
            // < two children: target node is easily freed
            freeNode = target;
        } else {
            // two children: find predecessor
            freeNode = target.left();
            while (!freeNode.right().isEmpty())
            {
                freeNode = freeNode.right();
            }
            // freeNode is predecessor
        }

        target.value = freeNode.value; // move value reference

        // child will be orphaned by the freeing of freeNode;
        // reparent this child carefully (it may be EMPTY)
        RedBlackTree<E> child;
        if (freeNode.left().isEmpty())
        {
            child = freeNode.right();
        } else {
            child = freeNode.left();
        }

        // if child is empty, we need to set its parent, temporarily
        child.setParent(freeNode.parent());
        if (!freeNode.isRoot())
        {
            if (freeNode.isLeftChild())
            {
                freeNode.parent().setLeft(child);
            } else {
                freeNode.parent().setRight(child);
            }
        }

        // Assertion: child has been reparented
        RedBlackTree<E> result = child.root();  
        if (freeNode.isBlack()) child.blackFixup();
        return result.root();
    }

    protected void blackFixup()
    {
        // if root - we're actually balanced; if red, set to black
        if (isRoot() || isRed())
        {
            setBlack();
        } else {
            RedBlackTree<E> sibling, parent; // temporary refs to relates
            // we hold onto our parent because the nodes shift about
            parent = parent();

            if (isLeftChild())
            {
                // our sibling: can't be a leaf (see text)
                sibling = parent.right();

                if (sibling.isRed()) // and, thus, parent is black
                {
                    // lower this, but leave black heights the same
                    // then reconsider node with a red parent
                    sibling.setBlack();
                    parent.setRed();
                    parent.rotateLeft();
                    blackFixup(); // this node might have adopted 
                } else
                if (sibling.left().isBlack() && sibling.right().isBlack())
                {
                    // sibling black with black children: sib can be red
                    // remove sib as one black node in sibling paths, and
                    // push missing black problem up to parent
                    sibling.setRed();
                    parent.blackFixup();
                } else {
                    if (sibling.right().isBlack())
                    {
                        // this:black, sib:black, sib.l:red, sib.r:black
                        // heighten sibling tree, making sib:r red and
                        // sib.l black (both sib.l's children were black)
                        sibling.left().setBlack();
                        sibling.setRed();
                        sibling.rotateRight();
                        sibling = parent.right();
                    }
                    // this: black, sib:black, sib:l black, sib.r:red 
                    // this tree deepens with parent as new black node
                    // sibling holds the previous parent color and
                    // sibling color (black) moves down to right;
                    // this adds a black node to all paths in this tree
                    // so we're done; finish by checking color of root
                    sibling.setRed(parent.isRed()); // copy color
                    parent.setBlack();
                    sibling.right().setBlack();
                    parent.rotateLeft();
                    root().blackFixup(); // finish by coloring root
                }
            } else { // isRightChild
                // our sibling: can't be a leaf (see text)
                sibling = parent.left();

                if (sibling.isRed()) // and, thus, parent is black
                {
                    // lower this, but leave black heights the same
                    // then reconsider node with a red parent
                    sibling.setBlack();
                    parent.setRed();
                    parent.rotateRight();
                    blackFixup(); // this node might have adopted 
                } else
                if (sibling.left().isBlack() && sibling.right().isBlack())
                {
                    // sibling black with black children: sib can be red
                    // remove sib as one black node in sibling paths, and
                    // push missing black problem up to parent
                    sibling.setRed();
                    parent.blackFixup();
                } else {
                    if (sibling.left().isBlack())
                    {
                        // this:black, sib:black, sib.r:red, sib.l:black
                        // heighten sibling tree, making sib:l red and
                        // sib.r black (both sib.r's children were black)
                        sibling.right().setBlack();
                        sibling.setRed();
                        sibling.rotateLeft();
                        sibling = parent.left();
                    }
                    // this: black, sib:black, sib:r black, sib.l:red 
                    // this tree deepens with parent as new black node
                    // sibling holds the previous parent color and
                    // sibling color (black) moves down to left;
                    // this adds a black node to all paths in this tree
                    // so we're done; finish by checking color of root
                    sibling.setRed(parent.isRed()); // copy color
                    parent.setBlack();
                    sibling.left().setBlack();
                    parent.rotateRight();
                    root().blackFixup(); // finish by coloring root
                }
            } 
        }
    }

    public boolean contains(E c)
    {
        return locate(c) != null;
    }
    

    protected RedBlackTree<E> locate(E c)
    {
        if (isEmpty()) return null;
        int relation = c.compareTo(value());
        if (relation == 0) return this;
        if (relation < 0) return left().locate(c);
        else return right().locate(c);
    }


    public E get(E c)
    {
        RedBlackTree<E> n = locate(c);
        if (n == null) return null;
        else return n.value();
    }

    public boolean consistency()
    {
        return/* wellConnected(null) &&*/ redConsistency() && blackConsistency();
    }

    protected int blackHeight()
    {
        if (isEmpty()) return 0;
        if (isBlack()) return 1 + left().blackHeight();
        else return  left().blackHeight();
    }

    protected boolean redConsistency()
    {
        if (isEmpty()) return true;
        if (isRed() && (left().isRed() || right().isRed())) return false;
        return left().redConsistency() && right().redConsistency();
    }

    protected boolean blackConsistency()
    {
        if (!isRoot()) // must be called on root
        {
            Assert.debug("Tree consistency not tested at root.");
            return false;
        }
        if (!isBlack()) // root must be black
        {
            Assert.debug("Root is not black.");
            return false;
        }
        // the number of black nodes on way to any leaf must be same
        if (!consistentlyBlackHeight(blackHeight()))
        {
            Assert.debug("Black height inconsistent.");
            return false;
        }
        return true;
    }


    protected boolean consistentlyBlackHeight(int height)
    {
        if (isEmpty()) return height == 0;
        if (isBlack()) height--;
        return left().consistentlyBlackHeight(height) &&
            right().consistentlyBlackHeight(height);
    }

    public void print()
    {
        if (isEmpty()) return;
        left().print();
        System.out.println(value());
        right().print();
    }

    public Iterator<E> iterator(){
        return new RedBlackIterator<E>(this);
    }
    

    public int hashCode()
    {
        if (isEmpty()) return 0;
        int result = left().hashCode() + right().hashCode();
        if (value() != null) result += value().hashCode();
        return result;
    }


    public String treeString(){
        String s = "";
        for (int i=0; i < this.depth(); i++){
            s += "\t|";
        }
        
        s += ("<" + value() + " : " + 
              getHand() + " : " + getColor()+ ">\n");
        
        if (left  != EMPTY) s += left.treeString();
        if (right != EMPTY) s += right.treeString();

        return s;
    }


    private String getHand(){
        if (isRightChild()) return "R";
        if (isLeftChild()) return "L";
        return "Root";  
    }

    private String getColor(){
        if (isRed) return "Red";
        return "Black";
    }

    public String toString()
    {
        if (isEmpty()) return "";
        if (isRed()) return "(" + left() + value() + right() +")";
        else         return "[" + left() + value() + right() +"]";
    }
}
