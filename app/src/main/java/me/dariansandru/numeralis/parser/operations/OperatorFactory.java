package me.dariansandru.numeralis.parser.operations;

import java.util.HashMap;
import java.util.Map;

public abstract class OperatorFactory {
    private static final Map<String, Operator> OPERATORS = new HashMap<>();

    static {
        OPERATORS.put("+", new AddOperator());
        OPERATORS.put("-", new SubOperator());
        OPERATORS.put("*", new MulOperator());
        OPERATORS.put("/", new DivOperator());
        OPERATORS.put("%", new ModOperator());
        OPERATORS.put("^", new PowOperator());
        OPERATORS.put("sqrt", new SqrtOperator());
    }

    public static Operator getOperator(String symbol) {
        Operator operator = OPERATORS.get(symbol);
        if (operator == null) {
            throw new IllegalArgumentException("Unknown operator: " + symbol);
        }
        return operator;
    }
}
