package me.dariansandru.numeralis.utils.structures;

public class AST<T> {
    private final ASTNode<T> root;
    private ASTNode<T> currentNode;

    public AST(ASTNode<T> root) {
        if (root == null) {
            throw new IllegalArgumentException("Root node cannot be null.");
        }
        this.root = root;
        this.currentNode = root;
    }

    public AST() {
        this.root = null;
        this.currentNode = null;
    }

    public ASTNode<T> getRoot() {
        return root;
    }

    public ASTNode<T> getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(T element) {
        if (currentNode != null) {
            currentNode.setElement(element);
        }
    }

    public boolean moveLeft() {
        if (currentNode != null && currentNode.getLeftChild() != null) {
            currentNode = currentNode.getLeftChild();
            return true;
        }
        return false;
    }

    public boolean moveRight() {
        if (currentNode != null && currentNode.getRightChild() != null) {
            currentNode = currentNode.getRightChild();
            return true;
        }
        return false;
    }

    public boolean moveUp() {
        if (currentNode != null && currentNode.getParent() != null) {
            currentNode = currentNode.getParent();
            return true;
        }
        return false;
    }

    public void createBinaryChild() {
        if (currentNode != null && currentNode.getLeftChild() == null && currentNode.getRightChild() == null) {
            ASTNode<T> left = new ASTNode<>();
            ASTNode<T> right = new ASTNode<>();
            left.setParent(currentNode);
            right.setParent(currentNode);
            currentNode.setLeftChild(left);
            currentNode.setRightChild(right);
        }
    }

    public void createUnaryChild() {
        if (currentNode != null && currentNode.getLeftChild() == null) {
            ASTNode<T> child = new ASTNode<>();
            child.setParent(currentNode);
            currentNode.setLeftChild(child);
        }
    }
}
