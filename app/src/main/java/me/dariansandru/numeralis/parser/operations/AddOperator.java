package me.dariansandru.numeralis.parser.operations;

/**
 * Using an instantiation of this class will allow the user to evaluate
 * two floating point numbers using standard binary mathematical addition.
 */
public class AddOperator implements Operator {

    private final String name;
    private final int arity;
    private final String symbol;

    private final int precedence;

    public AddOperator(){
        this.name = "ADD";
        this.arity = 2;
        this.symbol = "+";
        this.precedence = 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getArity() {
        return arity;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }

    @Override
    public double evaluate(double left, double right) {
        return left + right;
    }
}
