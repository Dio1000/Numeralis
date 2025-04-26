package me.dariansandru.numeralis.parser.operations;

import java.util.HashMap;
import java.util.Map;

/**
 * Using this abstract utility class will allow the user to generate a new instance of
 * the Operator interface using the symbol of the desired operation.
 */
public abstract class OperatorFactory {
    private static final Map<String, Operator> OPERATORS = new HashMap<>();

    static {
        OPERATORS.put("+", new AddOperator());
        OPERATORS.put("-", new SubOperator());
        OPERATORS.put("*", new MulOperator());
        OPERATORS.put("/", new DivOperator());
        OPERATORS.put("%", new ModOperator());
        OPERATORS.put("^", new PowOperator());
    }

    public static Operator getOperator(String symbol) {
        Operator operator = OPERATORS.get(symbol);
        if (operator == null) {
            throw new IllegalArgumentException("Unknown operator: " + symbol);
        }
        return operator;
    }
}
