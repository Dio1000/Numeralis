package me.dariansandru.numeralis.parser;

import me.dariansandru.numeralis.parser.operations.OperatorFactory;

import java.util.List;

public class Evaluator {

    public static double evaluate(List<Object> parsed) {
        if (parsed.size() == 1 && parsed.get(0) instanceof String) {
            return Double.parseDouble((String) parsed.get(0));
        }

        String operator = (String) parsed.get(0);
        Object left = parsed.get(1);
        Object right = parsed.get(2);

        double leftVal = left instanceof List ? evaluate((List<Object>) left) : Double.parseDouble((String) left);
        double rightVal = right instanceof List ? evaluate((List<Object>) right) : Double.parseDouble((String) right);

        return OperatorFactory.getOperator(operator).evaluate(leftVal, rightVal);
    }
}