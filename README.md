# <h1 align="center">BST-AVL-Tree</h1>



## BST

A binary search tree (BST) is a type of binary tree where each node has at most two children, referred to as the left child and the right child. The key property of a BST is that for each node:

1. All nodes in the left subtree have keys less than the node's key.
2. All nodes in the right subtree have keys greater than the node's key. 

This Java program provides an implementation of a BST with the following functionalities:

- Adding elements to the tree.
- Removing elements from the tree.
- Checking if the tree contains specific elements.
- Iterating through the elements in different orders (in-order, pre-order, post-order).
- Finding the minimum and maximum elements in the tree.
- Retrieving elements based on their index in the tree.
- Finding the ceiling and floor elements in the tree.
- Checking the equality of two trees.
- Converting the tree into a string format.
- Getting a range of elements from the tree.


## AVL Tree

Unlike a regular binary search tree (BST) where the height can become unbalanced, an AVL tree ensures that the height difference between the left and right subtrees of any node is at most 1. 

The AVL Tree is able to self-balance throgh the follpwoing methods. 

- Left rotation (rotateLeft): Balances the tree by rotating a node to the left.
- Right rotation (rotateRight): Balances the tree by rotating a node to the right.
- Left-Right rotation (rotateLeftRight): Combines a left rotation followed by a right rotation to balance the tree.
- Right-Left rotation (rotateRightLeft): Combines a right rotation followed by a left rotation to balance the tree.



This Java program provides an implementation of an AVL Tree with the following functionalities:

- Adding elements to the tree while maintaining balance.
- Removing elements from the tree while maintaining balance.
- Checking if the tree contains specific elements.
- Iterating through the elements in different orders (in-order, pre-order, post-order).
- Finding the minimum and maximum elements in the tree.
- Finding the ceiling and floor elements in the tree.
- Checking the equality of two trees.
- Converting the tree into a string format.
- Getting a range of elements from the tree.
