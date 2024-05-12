import java.util.Queue;
import java.util.LinkedList;
import java.lang.invoke.TypeDescriptor;
import java.util.*;
import java.lang.Class;

/**
 * @author Elijah Philip
 * This class is an implementation of an AVL tree, a self-balancing binary search tree.
 * The AVL tree maintains its balance by performing rotations during insertion and removal operations when the height is updated.
 */

public class AVL < E extends Comparable < E >> implements Iterable < E > , Cloneable {

  private Node < E > head;
  private int size;
  private int height;

  /**
   * Constructs a new, empty tree.
   */
  public AVL() {
    size = 0;
    height = 0;
    this.head = null;
  }

  public AVL(E[] collection) {
    for (E e: collection) {
      if (e == null) throw new NullPointerException("Given Element cannot be null.");
      this.add(e);
    }
  }

  @Override
  public AVL < E > clone() {
    try {
      // TODO: copy mutable state here, so the clone can't change the internals of the original
      return (AVL < E > ) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  private class InOrderIter implements Iterator < E > {
    Node < E > cNode;
    Stack < E > inorder;

    public InOrderIter() {
      cNode = head;
      inorder = inOrder(new Stack < > (), cNode);
      Collections.reverse(inorder);
    }

    private Stack < E > inOrder(Stack < E > storage, Node < E > node) {
      if (node == null) return storage;
      inOrder(storage, node.left);
      storage.push(node.data);
      inOrder(storage, node.right);
      return storage;
    }

    public Stack < E > getStack() {
      return inorder;
    }

    @Override
    public boolean hasNext() {
      return !inorder.isEmpty();
    }

    @Override
    public E next() {
      return inorder.pop();
    }
  }

  private class PreOrderIter implements Iterator < E > {
    Node < E > cNode;
    Stack < E > preorder;

    public PreOrderIter() {
      cNode = head;
      preorder = preOrder(new Stack < > (), cNode);
      Collections.reverse(preorder);
    }

    private Stack < E > preOrder(Stack < E > storage, Node < E > node) {
      if (node == null) return storage;
      storage.push(node.data);
      preOrder(storage, node.left);
      preOrder(storage, node.right);
      return storage;
    }

    @Override
    public boolean hasNext() {
      return !preorder.isEmpty();
    }

    @Override
    public E next() {
      return preorder.pop();
    }
  }

  private class PostOrderIter implements Iterator < E > {
    Node < E > cNode;
    Stack < E > postorder;

    public PostOrderIter() {
      cNode = head;
      postorder = postOrder(new Stack < > (), cNode);
      Collections.reverse(postorder);
    }

    private Stack < E > postOrder(Stack < E > storage, Node < E > node) {
      if (node == null) return storage;
      postOrder(storage, node.left);
      postOrder(storage, node.right);
      storage.push(node.data);
      return storage;
    }

    @Override
    public boolean hasNext() {
      return !postorder.isEmpty();
    }

    @Override
    public E next() {
      return postorder.pop();
    }
  }

  @Override
  public Iterator < E > iterator() {
    return new InOrderIter();
  }

  public Iterator < E > preorderIterator() {
    return new PreOrderIter();
  }

  public Iterator < E > postorderIterator() {
    return new PostOrderIter();
  }

  private static class Node < E extends Comparable < E >> {

    E data;
    int height;
    Node < E > left,
    right;

    public Node(E data, Node < E > left, Node < E > right) {
      this.data = data;
      this.left = left;
      this.right = right;
    }
  }

  int compareTo(E e) {
    if (e instanceof Number n2 && head.data instanceof Number n1) {
      return Double.compare(n1.doubleValue(), n2.doubleValue());
    } else if (e instanceof String s2 && head.data instanceof String s1) {
      return s2.compareTo(s1);
    } else if (e instanceof Character c2 && head.data instanceof Character c1) {
      return c2.compareTo(c1);
    }
    return 0;
  }

  int getHeightHelper(Node < E > cNode) {
    if (cNode == null) {
      return 0; //If the first node is null it means there are no nodes (empty).
    } else {
      int leftTree = getHeightHelper(cNode.left);
      int rightTree = getHeightHelper(cNode.right);
      return 1 + Math.max(leftTree, rightTree); //Get the max of the acquired heights.
    }
  }

  int getHeight() {
    Node < E > cNode = head;
    return getHeightHelper(cNode);
  }

  /**
   * Recursive helper method to add a new element to the AVL tree while maintaining balance.
   *
   * @param e     The element to be added to the tree.
   * @param cNode The current node in the recursion process.
   * @return The updated node after adding the new element.
   */
  private Node < E > addHelper(E e, Node < E > cNode) {
    if (cNode == null) {
      return new Node < > (e, null, null);
    }

    int compareVal = e.compareTo(cNode.data);

    if (compareVal < 0) {
      cNode.left = addHelper(e, cNode.left);
    } else if (compareVal > 0) {
      cNode.right = addHelper(e, cNode.right);
    } else {
      return cNode;
    }

    cNode.height = 1 + Math.max(getHeight(cNode.left), getHeight(cNode.right));

    int balance = getBalance(cNode);

    if (balance > 1) {
      if (e.compareTo(cNode.left.data) < 0) {
        return rotateRight(cNode);
      } else {
        return rotateLeftRight(cNode);
      }
    }

    if (balance < -1) {
      if (e.compareTo(cNode.right.data) > 0) {
        return rotateLeft(cNode);
      } else {
        return rotateRightLeft(cNode);
      }
    }

    return cNode;
  }

  /**
   * Public method to add a new element to the AVL tree.
   *
   * @param e The element to be added to the tree.
   * @return True if the element is added successfully.
   */
  public boolean add(E e) {
    if (e == null) throw new NullPointerException("Element cannot be null");
    head = addHelper(e, head);
    size++;
    height = getHeight();
    return true;
  }

  /**
   * Public method to add all elements from a collection to the AVL tree.
   *
   * @param collection The collection of elements to be added to the tree.
   * @return True if all elements are added successfully.
   */
  public boolean addAll(Collection < ? extends E > collection) {
    for (E e: collection) {
      if (e == null) throw new NullPointerException("Element cannot be null.");
      this.add(e);
    }
    return true;
  }

  /**
   * Private method to find the minimum element in a subtree starting from the given node.
   *
   * @param n The starting node for finding the minimum element.
   * @return The minimum element in the subtree.
   */
  private E findMin(Node < E > n) {
    while (n.left != null) {
      n = n.left;
    }
    return n.data;
  }

  /**
   * Recursive helper method to remove an element from the AVL tree while maintaining balance.
   *
   * @param o     The object to be removed from the tree.
   * @param cNode The current node in the recursion process.
   * @return The updated node after removing the specified object.
   */
  private Node < E > removeHelper(Object o, Node < E > cNode) {
    if (cNode == null) {
      return null; // If the node is null, the element is not in the tree.
    } else if (!cNode.data.getClass().isInstance(o)) {
      throw new ClassCastException("The specified object cannot be compared with the elements currently in the set");
    }

    int compareVal = ((Comparable < E > ) o).compareTo(cNode.data);

    if (compareVal < 0) {
      cNode.left = removeHelper(o, cNode.left);
    } else if (compareVal > 0) {
      cNode.right = removeHelper(o, cNode.right);
    } else {
      // Node to be removed found

      if (cNode.left == null && cNode.right == null) {
        // If it's a leaf node, just remove the node.
        return null;
      } else if (cNode.left != null && cNode.right == null) {
        // If it has a child node on the left, return the left child.
        return cNode.left;
      } else if (cNode.left == null && cNode.right != null) {
        // If it has a child node on the right, return the right child.
        return cNode.right;
      } else {
        // Node with two children
        cNode.data = findMin(cNode.right); // Find the inorder successor
        cNode.right = removeHelper(cNode.data, cNode.right); // Remove the inorder successor
      }
    }

    // Update height
    cNode.height = 1 + Math.max(getHeight(cNode.left), getHeight(cNode.right));

    // Rebalance the tree
    int balance = getBalance(cNode);

    // Left Heavy
    if (balance > 1) {
      if (getBalance(cNode.left) >= 0) {
        return rotateRight(cNode);
      } else {
        return rotateLeftRight(cNode);
      }
    }

    // Right Heavy
    if (balance < -1) {
      if (getBalance(cNode.right) <= 0) {
        return rotateLeft(cNode);
      } else {
        return rotateRightLeft(cNode);
      }
    }

    return cNode;
  }

  public boolean remove(Object o) {
    if (o == null) throw new NullPointerException("Specified element cannot be null.");
    head = removeHelper(o, head);

    // Update height after removal
    height = getHeight();

    return true;
  }

  public boolean containsHelper(Object o, Node < E > cNode) {
    if (cNode == null) return false;

    if (!cNode.data.getClass().isInstance(o)) {
      throw new ClassCastException("The specified object cannot be compared with the elements currently in the set");
    }

    if (cNode.data.equals(o)) return true;

    return containsHelper(o, cNode.left) || containsHelper(o, cNode.right);
  }

  public boolean contains(Object o) {
    Node < E > cNode = head;
    if (o == null) throw new NullPointerException("Specified element cannot be null.");
    return containsHelper(o, cNode);
  }

  public boolean containsAll(Collection < ? > c) {
    if (c == null) throw new NullPointerException("Specified collection cannot be null.");
    for (Object o: c) {
      if (!this.contains(o)) return false;
    }
    return true;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return head == null;
  }

  public int height() {
    return height;
  }

  public E get(int index) {
    if (index < 0 || index >= size)
      throw new IndexOutOfBoundsException(String.format("%s%d%s%d", "The specified index should be between ", 0, " and ", size - 1));
    Iterator < E > iter = new InOrderIter();
    while (index != 0) {
      iter.next();
      index--;
    }
    return iter.next();
  }

  private E ceilingHelper(E e, Node < E > cNode) {
    if (e == null) throw new NullPointerException("Element cannot be null");
    if (cNode == null) return null;
    if (!cNode.data.getClass().isInstance(e)) {
      throw new ClassCastException("The specified object cannot be compared with the elements currently in the set");
    }
    if (cNode.data.compareTo(e) == 0) return cNode.data;
    else if (cNode.data.compareTo(e) < 0) {
      return ceilingHelper(e, cNode.right);
    }
    E ceil = ceilingHelper(e, cNode.left);
    return (ceil != null && (ceil.compareTo(e) == 0 || ceil.compareTo(e) > 0)) ? ceil : cNode.data;
  }

  public E ceiling(E e) {
    Node < E > cNode = head;
    return ceilingHelper(e, cNode);
  }

  private E floorHelper(E e, Node < E > cNode) {
    if (e == null) throw new NullPointerException("Element cannot be null");
    if (cNode == null) return null;
    if (!cNode.data.getClass().isInstance(e)) {
      throw new ClassCastException("The specified object cannot be compared with the elements currently in the set");
    }
    if (cNode.data.compareTo(e) == 0) return cNode.data;
    if (cNode.data.compareTo(e) > 0) {
      return floorHelper(e, cNode.left);
    }
    E floor = floorHelper(e, cNode.right);

    return (floor != null && (floor.compareTo(e) < 0 || floor.compareTo(e) == 0)) ? floor : cNode.data;
  }

  public E floor(E e) {
    Node < E > cNode = head;
    return floorHelper(e, cNode);
  }

  public E first() {
    if (head == null) throw new NoSuchElementException("The tree is currently empty.");
    Node < E > cNode = head;
    while (cNode.left != null) cNode = cNode.left;
    return cNode.data;
  }

  public E last() {
    if (head == null) throw new NoSuchElementException("The tree is currently empty.");
    Node < E > cNode = head;
    while (cNode.right != null) cNode = cNode.right;
    return cNode.data;
  }

  private E lowerHelper(E e, Node < E > cNode) {
    if (e == null) throw new NullPointerException("Element cannot be null");
    if (cNode == null) return null;
    if (!cNode.data.getClass().isInstance(e)) {
      throw new ClassCastException("The specified object cannot be compared with the elements currently in the set");
    }
    if (cNode.data.compareTo(e) >= 0) {
      return lowerHelper(e, cNode.left);
    }
    E lower = lowerHelper(e, cNode.right);
    return (lower != null && (lower.compareTo(e) < 0) ? lower : cNode.data);
  }

  public E lower(E e) {
    Node < E > cNode = head;
    return lowerHelper(e, cNode);
  }

  private E higherHelper(E e, Node < E > cNode) {
    if (e == null) throw new NullPointerException("Element cannot be null");
    if (cNode == null) return null;
    if (!cNode.data.getClass().isInstance(e)) {
      throw new ClassCastException("The specified object cannot be compared with the elements currently in the set");
    }
    if (cNode.data.compareTo(e) <= 0) {
      return higherHelper(e, cNode.right);
    }
    E higher = higherHelper(e, cNode.left);

    return (higher != null && (higher.compareTo(e) > 0)) ? higher : cNode.data;
  }

  public E higher(E e) {
    Node < E > cNode = head;
    return higherHelper(e, cNode);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object obj) {
    if (this.getClass().isInstance(obj)) {
      AVL < E > compareTree = (AVL < E > ) obj;
      if (compareTree.size() == this.size()) {
        //Traverse the tree using for loop and use the get method to check if each element is equal with the compareTo method.
        Iterator < E > it1 = iterator();
        Iterator < E > it2 = iterator();
        while (it1.hasNext()) {
          if (!(it1.next().compareTo(it2.next()) == 0)) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    Iterator < E > it = iterator();
    StringBuilder sb = new StringBuilder("[");
    if (size() == 0) return "[]";
    while (it.hasNext()) {
      sb.append(String.valueOf(it.next() + ", "));
    }
    sb.delete(sb.length() - 2, sb.length());
    sb.append("]");

    return sb.toString();
  }

  public Object[] toArray() {
    Object[] o = new Object[size()];
    for (int i = 0; i < size(); i++) {
      o[i] = get(i);
    }
    return o;
  }

  public void toStringTreeFormat(Node < E > currentNode, int currentDepth, StringBuilder resultBuilder) {
    // Insert spaces for indentation at each level of depth
    for (int i = 0; i < currentDepth; i++) {
      resultBuilder.append("|  ");
    }

    // If the current node is absent, append a representation of null and exit
    if (currentNode == null) {
      resultBuilder.append("-null\n");
      return;
    }

    // Append the value of the present node
    resultBuilder.append("-").append(currentNode.data).append("\n");

    // Recursively invoke the method for the left and right subtrees
    toStringTreeFormat(currentNode.left, currentDepth + 1, resultBuilder);
    toStringTreeFormat(currentNode.right, currentDepth + 1, resultBuilder);
  }

  int getHeight(Node < E > cNode) {
    return (cNode == null) ? 0 : cNode.height;
  }

  int getBalance(Node < E > cNode) {
    return (cNode == null) ? 0 : getHeight(cNode.left) - getHeight(cNode.right);
  }

  private Node < E > rotateRight(Node < E > node2) {
    Node < E > node1 = node2.left;
    Node < E > nodeT2 = node1.right;

    node1.right = node2;
    node2.left = nodeT2;

    node2.height = 1 + Math.max(getHeight(node2.left), getHeight(node2.right));
    node1.height = 1 + Math.max(getHeight(node1.left), getHeight(node1.right));

    return node1;
  }

  private Node < E > rotateLeft(Node < E > node1) {
    Node < E > node2 = node1.right;
    Node < E > nodeT2 = node2.left;

    node2.left = node1;
    node1.right = nodeT2;

    node1.height = 1 + Math.max(getHeight(node1.left), getHeight(node1.right));
    node2.height = 1 + Math.max(getHeight(node2.left), getHeight(node2.right));

    return node2;
  }

  private Node < E > rotateRightLeft(Node < E > node3) {
    node3.right = rotateRight(node3.right);
    return rotateLeft(node3);
  }

  private Node < E > rotateLeftRight(Node < E > node3) {
    node3.left = rotateLeft(node3.left);
    return rotateRight(node3);
  }

}
