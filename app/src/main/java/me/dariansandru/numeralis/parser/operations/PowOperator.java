package me.dariansandru.numeralis.parser.operations;

public class PowOperator implements Operator {

    private final String name;
    private final int arity;
    private final String symbol;
    private final int precedence;

    public PowOperator(){
        this.name = "POW";
        this.arity = 2;
        this.symbol = "^";
        this.precedence = 3;
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
        return Math.pow(left, right);
    }

}
