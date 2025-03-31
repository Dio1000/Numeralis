package me.dariansandru.numeralis.parser.operations;

public interface Operator {
    public String getName();

    public int getArity();

    public String getSymbol();

    public int getPrecedence();

    public double evaluate(double left, double right);
}
