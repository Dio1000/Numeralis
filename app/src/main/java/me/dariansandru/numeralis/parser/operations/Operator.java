package me.dariansandru.numeralis.parser.operations;

/**
 * Implementing this interface will allow an operation to inherit getters and setters,
 * as well as the evaluate method for standard binary mathematical operations between
 * floating point numbers.
 */
public interface Operator {
    public String getName();

    public int getArity();

    public String getSymbol();

    public int getPrecedence();

    public double evaluate(double left, double right);
}
