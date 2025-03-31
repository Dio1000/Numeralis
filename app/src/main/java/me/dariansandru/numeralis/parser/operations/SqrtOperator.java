package me.dariansandru.numeralis.parser.operations;

public class SqrtOperator implements Operator {

    private final String name;
    private final int arity;
    private final String symbol;
    private final int precedence;

    public SqrtOperator(){
        this.name = "SQRT";
        this.arity = 1;
        this.symbol = "sqrt";
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


    // TODO Don't do this
    @Override
    public double evaluate(double left, double right) {
        return Math.sqrt(left);
    }

}
