    import java.util.*;


    /**
     * @author Elijah Philip
     * This class is an implmentation of a BST tree. It is able to take in elements and automatically sort them to the left tree (if less), or right tree (if greater).
     */

    /**
     * Constructs a new, empty tree.
     */
    public class BST < E extends Comparable < E >> implements Iterable < E > , Cloneable {

      private Node < E > head;
      private int size;
      private int height;

      /**
       * Constructs a new, empty tree.
       */
      public BST() {
        size = 0;
        height = 0;
        this.head = null;
      }

      /**
       * Constructs a new tree containing elements from the specified collection.
       *
       * @param collection Collection whose elements will comprise the new tree.
       * @throws NullPointerException if the specified collection is null.
       */
      public BST(E[] collection) { //Case Where collection si empty?
        for (E e: collection) {
          if (e == null) throw new NullPointerException("Given Element cannot be null.");
          this.add(e);
        }
      }

      @Override
      public BST < E > clone() {
        try {
          // TODO: copy mutable state here, so the clone can't change the internals of the original
          return (BST < E > ) super.clone();
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
      /**
       * Returns an iterator over the elements in the tree in ascending order.
       *
       * @return an iterator over the elements in the tree in ascending order.
       */
      public Iterator < E > iterator() {
        return new InOrderIter();
      }

      /**
       * Returns an iterator over the elements in the tree in order of the preorder traversal.
       *
       * @return an iterator over the elements in the tree in order of the preorder traversal.
       */
      public Iterator < E > preorderIterator() {
        return new PreOrderIter();
      }
      /**
       * Returns an iterator over the elements in the tree in order of the postorder traversal.
       *
       * @return an iterator over the elements in the tree in order of the postorder traversal.
       */
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

      private Node < E > addHelper(E e, Node < E > cNode) {
        if (cNode == null) { //If the BST is empty make the first node equal to the specified element.
          //height = Math.max(calcHeight(head.left), calcHeight(head.right)) + 1;
          return new Node < > (e, null, null);
        }
        int compareVal = e.compareTo(cNode.data);
        if (compareVal < 0) {
          cNode.left = addHelper(e, cNode.left);
        } else if (compareVal > 0) {
          cNode.right = addHelper(e, cNode.right);
        }
        return cNode;
      }
      /**
       * Adds the specified element to the tree.
       *
       * @param e Element to be added to the tree.
       * @return true if this set did not already contain the specified element.
       * @throws NullPointerException if the specified element is null.
       */
      public boolean add(E e) {
        if (e == null) throw new NullPointerException("Element cannot be null");
        head = addHelper(e, head);
        size++; //increase size after every addition of a node.
        height = getHeight();
        return true;
      }

      /**
       * Adds all elements from the specified collection to the tree.
       *
       * @param collection Collection containing elements to be added to the tree.
       * @return true if this set changed as a result of the call.
       * @throws NullPointerException if the specified collection is null.
       */
      public boolean addAll(Collection < ? extends E > collection) {
        for (E e: collection) {
          if (e == null) throw new NullPointerException("Element cannot be null.");
          this.add(e);
        }
        return true;
      }

      private E findMin(Node < E > n) {
        while (n.left != null) {
          n = n.left;
        }
        return n.data;
      }

      private Node < E > removeHelper(Object o, Node < E > cNode) {
        if (cNode == null) {
          return null; // If the node is null, the element is not in the tree.
        } else if (!cNode.data.getClass().isInstance(o)) {
          throw new ClassCastException("The specified object cannot be compared with the elements currently in the set");
        }
        if (cNode.data.equals(o)) {
          if (cNode.right == null && cNode.left == null) {
            return null; // If it's a leaf node, just remove the node.
          } else if (cNode.left != null && cNode.right == null) {
            return cNode.left; // If it has a child node, return the child node.
          } else if (cNode.left == null && cNode.right != null) {
            return cNode.right; // If it has a child node, return the child node.
          } else {
            // If it's Node with two children, then replace the node that's to be removed with the inorder successor.
            cNode.data = findMin(cNode.right);
            cNode.right = removeHelper(cNode.data, cNode.right);
            return cNode;
          }
        }

        cNode.left = removeHelper(o, cNode.left);
        cNode.right = removeHelper(o, cNode.right);
        return cNode;
      }
      /**
       * Removes the specified element from the tree.
       *
       * @param o Object to be removed from the tree, if present.
       * @return true if the tree contained the specified element.
       * @throws ClassCastException if the specified object cannot be compared with the elements in the tree.
       * @throws NullPointerException if the specified element is null.
       */
      public boolean remove(Object o) {
        if (o == null) throw new NullPointerException("Specified element cannot be null.");
        Node < E > cNode = head;
        head = removeHelper(o, cNode);
        return true;
      }

      /**
       * Removes all elements from the tree.
       */
      public void clear() {
        head = null;

      }

      public boolean containsHelper(Object o, Node < E > cNode) {
        if (cNode == null) return false;

        if (!cNode.data.getClass().isInstance(o)) {
          throw new ClassCastException("The specified object cannot be compared with the elements currently in the set");
        }

        if (cNode.data.equals(o)) return true;

        return containsHelper(o, cNode.left) || containsHelper(o, cNode.right);
      }

      /**
       * Checks if the tree contains the specified element.
       *
       * @param o Object to be checked for containment in the tree.
       * @return true if the tree contains the specified element.
       * @throws ClassCastException if the specified object cannot be compared with the elements in the tree.
       * @throws NullPointerException if the specified element is null.
       */
      public boolean contains(Object o) {
        Node < E > cNode = head;
        if (o == null) throw new NullPointerException("Specified element cannot be null.");
        return containsHelper(o, cNode);
      }

      /**
       * Checks if the tree contains all elements from the specified collection.
       *
       * @param c Collection to be checked for containment in the tree.
       * @return true if the tree contains all elements in the specified collection.
       * @throws NullPointerException if the specified collection contains null elements or if the specified collection is null.
       */
      public boolean containsAll(Collection < ? > c) {
        if (c == null) throw new NullPointerException("Specified collection cannot be null.");
        for (Object o: c) {
          if (!this.contains(o)) return false;
        }
        return true;
      }

      /**
       * Returns the number of elements in the tree.
       *
       * @return the number of elements in the tree.
       */
      public int size() {
        return size;
      }

      /**
       * Checks if the tree is empty.
       *
       * @return true if the tree contains no elements.
       */
      public boolean isEmpty() {
        return head == null;
      }

      /**
       * Returns the height of the tree.
       *
       * @return the height of the tree or zero if the tree is empty.
       */
      public int height() {
        return height;
      }

      /**
       * Returns the element at the specified position in the tree.
       *
       * @param index Index of the element to return.
       * @return the element at the specified position in the tree.
       * @throws IndexOutOfBoundsException if the index is out of range.
       */
      public E get(int index) {
        if (size() == 0) return null;
        if (index < 0 || index >= size)
          throw new IndexOutOfBoundsException(String.format("%s%d%s%d", "The specified index should be between ", 0, " and ", size - 1));
        Iterator < E > iter = new InOrderIter();
        while (index != 0 && iter.hasNext()) {
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
      /**
       * Returns the least element in the tree greater than or equal to the given element.
       *
       * @param e The value to match.
       * @return the least element greater than or equal to e, or null if there is no such element.
       * @throws ClassCastException   if the specified element cannot be compared with the elements in the tree.
       * @throws NullPointerException if the specified element is null.
       */
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

      /**
       * Returns the greatest element in the tree less than or equal to the given element.
       *
       * @param e The value to match.
       * @return the greatest element less than or equal to e, or null if there is no such element.
       * @throws ClassCastException   if the specified element cannot be compared with the elements in the tree.
       * @throws NullPointerException if the specified element is null.
       */
      public E floor(E e) {
        Node < E > cNode = head;
        return floorHelper(e, cNode);
      }
      /**
       * Returns the first (lowest) element currently in the tree.
       *
       * @return the first (lowest) element currently in the tree.
       * @throws NoSuchElementException if the tree is empty.
       */
      public E first() {
        if (head == null) throw new NoSuchElementException("The tree is currently empty.");
        Node < E > cNode = head;
        while (cNode.left != null) cNode = cNode.left;
        return cNode.data;
      }
      /**
       * Returns the last (highest) element currently in the tree.
       *
       * @return the last (highest) element currently in the tree.
       * @throws NoSuchElementException if the tree is empty.
       */
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

      /**
       * Returns the greatest element in the tree strictly less than the given element.
       *
       * @param e The value to match.
       * @return the greatest element less than e, or null
       */
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
          BST < E > compareTree = (BST < E > ) obj;
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

      private String getIndentation(int depth) {
        StringBuilder indentation = new StringBuilder();
        indentation.append("   |".repeat(Math.max(0, depth)));
        return indentation.toString();
      }
      // Method to get a string representation of the tree in tree format
      public String toStringTreeFormat() {
        StringBuilder sb = new StringBuilder();
        toStringTreeFormat(head, 0, sb);
        return sb.toString();
      }

      public void toStringTreeFormat(Node < E > currentNode, int currentDepth, StringBuilder resultBuilder) {
        for (int i = 0; i < currentDepth; i++) {
          resultBuilder.append("|  ");
        }

        if (currentNode == null) {
          resultBuilder.append("-null\n");
          return;
        }

        resultBuilder.append("-").append(currentNode.data).append("\n");

        toStringTreeFormat(currentNode.left, currentDepth + 1, resultBuilder);
        toStringTreeFormat(currentNode.right, currentDepth + 1, resultBuilder);
      }

      /**
       * Returns a collection whose elements range from fromElement to toElement.
       *
       * @param fromElement Low endpoint (inclusive) of the returned collection.
       * @param toElement   High endpoint (inclusive) of the returned collection.
       * @return a collection containing a portion of this tree whose elements range from fromElement to toElement.
       * @throws NullPointerException     if fromElement or toElement is null.
       * @throws IllegalArgumentException if fromElement is greater than toElement.
       */
      public ArrayList < E > getRange(E fromElement, E toElement) {
        if (fromElement == null || toElement == null) throw new NullPointerException("This function's argument does not accept null.");

        if (fromElement.compareTo(toElement) > 0) throw new IllegalArgumentException("The range should start from a low endpoint, to a higher endpoint.");

        ArrayList < E > result = new ArrayList < > ();
        getRangeHelper(head, fromElement, toElement, result);
        return result;
      }

      private void getRangeHelper(Node < E > node, E fromElement, E toElement, ArrayList < E > result) {
        if (node != null) {
          int compareFrom = fromElement.compareTo(node.data);
          int compareTo = toElement.compareTo(node.data);

          // Traverse left subtree if needed
          if (compareFrom < 0) {
            getRangeHelper(node.left, fromElement, toElement, result);
          }

          // Add the current node's data if it's within the range
          if (compareFrom <= 0 && compareTo >= 0) {
            result.add(node.data);
          }

          // Traverse right subtree if needed
          if (compareTo > 0) {
            getRangeHelper(node.right, fromElement, toElement, result);
          }
        }
      }
    }

