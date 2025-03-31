package me.dariansandru.numeralis.parser.operations;

public class SubOperator implements Operator {

    private final String name;
    private final int arity;
    private final String symbol;
    private final int precedence;

    public SubOperator(){
        this.name = "SUB";
        this.arity = 2;
        this.symbol = "-";
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

    public double evaluate(double left, double right) {
        return left - right;
    }

}
