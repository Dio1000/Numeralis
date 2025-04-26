package me.dariansandru.numeralis.parser;

import androidx.annotation.NonNull;

/**
 * Using an instantiation of this class will allow the user to create an expression.
 */
public class Expression {
    private final String expr;

    public Expression(String expr){
        this.expr = expr;
    }

    public String getExpression(){
        return this.expr;
    }

    public boolean isOperator(){
        return OperatorRegistry.isOperator(this.expr);
    }

    public boolean isPure(){
        if (this.expr == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(this.expr);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return this.expr;
    }
}
