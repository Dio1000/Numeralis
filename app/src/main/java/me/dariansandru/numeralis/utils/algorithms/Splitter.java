package me.dariansandru.numeralis.utils.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.parser.OperatorRegistry;

public abstract class Splitter {

    public static List<Expression> splitExpression(Expression expression, String symbol) {
        List<Expression> expressions = new ArrayList<>();
        String expr = expression.getExpression();

        StringBuilder leftHandSide = new StringBuilder();
        StringBuilder rightHandSide = new StringBuilder();

        boolean inBrackets = false;
        boolean foundSymbol = false;

        int index = 0;
        while (index < expr.length()) {
            if (expr.charAt(index) == '(') inBrackets = true;
            else if (expr.charAt(index) == ')') inBrackets = false;

            if (!inBrackets && !foundSymbol && Objects.equals(String.valueOf(expr.charAt(index)), symbol)) {
                foundSymbol = true;
                index++;
                continue;
            }

            if (!foundSymbol) leftHandSide.append(expr.charAt(index));
            else rightHandSide.append(expr.charAt(index));

            index++;
        }

        if (!leftHandSide.toString().isEmpty()) expressions.add(new Expression(leftHandSide.toString().trim()));
        if (!rightHandSide.toString().isEmpty()) expressions.add(new Expression(rightHandSide.toString().trim()));

        return expressions;
    }

    public static List<Expression> singleBLSplit(Expression expression, List<String> operators) {
        String expr = expression.getExpression();
        if (expr.startsWith("(") && expr.endsWith(")")) {
            expr = expr.substring(1, expr.length() - 1);
            expression = new Expression(expr);
        }

        for (String operator : operators) {
            List<Expression> expressions = splitExpression(expression, operator);
            if (expressions.size() == 2) {
                expressions.add(0, new Expression(operator));
                return expressions;
            }
        }

        return new ArrayList<>();
    }

    public static List<Object> recursiveSplit(Expression expression, List<String> operators) {
        List<Expression> split = singleBLSplit(expression, operators);
        if (split.isEmpty()) return List.of(expression.getExpression());

        List<Object> result = new ArrayList<>();
        result.add(split.get(0).getExpression());
        result.add(recursiveSplit(split.get(1), operators));
        result.add(recursiveSplit(split.get(2), operators));

        return result;
    }
}