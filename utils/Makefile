typically:	LLRB.png RedBlackTree.png SkipList.png BinarySearchTree.png SplayTree.png

LLRB.png:	../structure/LLRB.java
	python3 profile.py <../structure/LLRB.java >LLRB.ppm
	convert LLRB.ppm LLRB.png

RedBlackTree.png:	../structure/RedBlackTree.java
	python3 profile.py <../structure/RedBlackTree.java >RedBlackTree.ppm
	convert RedBlackTree.ppm RedBlackTree.png

SkipList.png:	../structure/SkipList.java
	python3 profile.py <../structure/SkipList.java >SkipList.ppm
	convert SkipList.ppm SkipList.png

SplayTree.png:	../structure/structure5/SplayTree.java
	python3 profile.py <../structure/structure5/SplayTree.java >SplayTree.ppm
	convert SplayTree.ppm SplayTree.png

BinarySearchTree.png:	../structure/structure5/BinarySearchTree.java
	python3 profile.py <../structure/structure5/BinarySearchTree.java >BinarySearchTree.ppm
	convert BinarySearchTree.ppm BinarySearchTree.png

clean:
	rm -f *.ppm *.png *~