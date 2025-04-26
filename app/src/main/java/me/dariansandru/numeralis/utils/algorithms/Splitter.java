package me.dariansandru.numeralis.utils.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.dariansandru.numeralis.parser.Expression;

/**
 * Using this abstract utility class will allow the user to parse and split expressions.
 */
public abstract class Splitter {

    /**
     * Splits an expression into two subexpressions based on the first occurrence
     * of the given symbol at the base level (not inside parentheses).
     *
     * <pre>
     * Example:
     * Given expression: (A ∨ B) ∧ (C ∨ D)
     * Splitting on '∧' results in two expressions: (A ∨ B), (C ∨ D)
     * </pre>
     *
     * @param expression Expression to be split.
     * @param symbol Symbol (operator) at which to split the expression.
     * @return A list of two subexpressions split at the given symbol.
     */
    public static List<Expression> splitExpression(Expression expression, String symbol) {
        List<Expression> expressions = new ArrayList<>();
        String expr = expression.getExpression();

        StringBuilder leftHandSide = new StringBuilder();
        StringBuilder rightHandSide = new StringBuilder();

        int bracketDepth = 0;
        boolean foundSymbol = false;

        int index = 0;
        while (index < expr.length()) {
            char c = expr.charAt(index);

            if (c == '(') bracketDepth++;
            else if (c == ')') bracketDepth--;
            if (bracketDepth == 0 && !foundSymbol && Objects.equals(String.valueOf(c), symbol)) {
                foundSymbol = true;
                index++;
                continue;
            }

            if (!foundSymbol) leftHandSide.append(c);
            else rightHandSide.append(c);

            index++;
        }

        if (!leftHandSide.toString().isEmpty()) expressions.add(new Expression(leftHandSide.toString().trim()));
        if (!rightHandSide.toString().isEmpty()) expressions.add(new Expression(rightHandSide.toString().trim()));

        return expressions;
    }

    /**
     * Splits an expression into operator and two subexpressions based on the first operator
     * that appears at the base level from a given list of operators.
     *
     * <pre>
     * Example:
     * Given expression: (A ∨ B) ∧ (C ∨ D)
     * Operators list: ["∧", "∨"]
     * Result: ["∧", (A ∨ B), (C ∨ D)]
     * </pre>
     *
     * @param expression Expression to split.
     * @param operators List of operators to split on.
     * @return A list containing the operator first, then the two subexpressions, or an empty list if no split occurs.
     */
    public static List<Expression> singleBLSplit(Expression expression, List<String> operators) {
        String expr = expression.getExpression();
        if (expr.startsWith("(") && expr.endsWith(")")) {
            int balance = 0;
            boolean valid = true;

            for (int i = 0; i < expr.length(); i++) {
                if (expr.charAt(i) == '(') balance++;
                else if (expr.charAt(i) == ')') balance--;

                if (balance == 0 && i < expr.length() - 1) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                expr = expr.substring(1, expr.length() - 1);
                expression = new Expression(expr);
            }
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

    /**
     * Recursively splits an expression into a nested list based on the operators provided,
     * creating a tree-like structure where each node is an operator and leaves are literals.
     *
     * <pre>
     * Example:
     * Given expression: (A ∨ B) ∧ (C ∨ D)
     * Operators list: ["∧", "∨"]
     * Result:
     * ["∧", ["∨", "A", "B"], ["∨", "C", "D"]]
     * </pre>
     *
     * @param expression Expression to recursively split.
     * @param operators List of operators to split on.
     * @return A nested List of Strings and Lists representing the expression structure.
     */
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
