package me.dariansandru.numeralis.utils.structures;

public class ASTNode<T> {
    private T element;
    private ASTNode<T> leftChild;
    private ASTNode<T> rightChild;
    private ASTNode<T> parent;

    public ASTNode(T element) {
        this.element = element;
    }

    public ASTNode() {
        this(null);
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public ASTNode<T> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(ASTNode<T> node) {
        if (node != null) {
            node.setParent(this);
        }
        this.leftChild = node;
    }

    public ASTNode<T> getRightChild() {
        return rightChild;
    }

    public void setRightChild(ASTNode<T> node) {
        if (node != null) {
            node.setParent(this);
        }
        this.rightChild = node;
    }

    public ASTNode<T> getParent() {
        return parent;
    }

    public void setParent(ASTNode<T> parent) {
        this.parent = parent;
    }
}
