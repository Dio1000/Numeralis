package me.dariansandru.numeralis.utils.structures;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {
    private final int size;
    private int top;
    private final List<T> elements;

    public Stack(int size){
        this.size = size;
        this.elements = new ArrayList<>(size);
        this.top = -1;
    }

    public void push(T element){
        if (this.isFull()) return;
        this.elements.add(element);
        this.top++;
    }

    public void pop(){
        if (this.isEmpty()) return;
        this.elements.remove(top);
        this.top--;
    }

    public boolean isEmpty(){
        return this.top == -1;
    }

    public boolean isFull(){
        return this.top == size - 1;
    }

    public T peek(){
        if (this.isEmpty()) return null;
        return this.elements.get(top);
    }
}
