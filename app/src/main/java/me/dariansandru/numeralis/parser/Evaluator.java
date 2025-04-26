package me.dariansandru.numeralis.parser;

import me.dariansandru.numeralis.parser.operations.OperatorFactory;
import me.dariansandru.numeralis.utils.algorithms.Splitter;

import java.util.List;

/**
 * Using this abstract utility class will allow the user to evaluate standard mathematical
 * and logic expressions.
 */
public abstract class Evaluator {

    /**
     * Evaluates the parsed version of an expression in a nested list format.
     * <pre>
     *     Example:
     *     An example of a nested list is: [^, [+, [-, 3, 2], 5], 2]
     *     Which has the equivalent in standard infix mathematical notation: (5 + (3 - 2))^2
     *     And evaluates to: 36
     * </pre>
     * @param parsed A list of objects containing the nested lists from a mathematical expression.
     * @return The result of the evaluation of the expression.
     */
    public static double evaluate(List<Object> parsed) {
        if (parsed == null || parsed.isEmpty()) {
            throw new IllegalArgumentException("Empty or null expression");
        }

        if (parsed.size() == 1) {
            Object single = parsed.get(0);
            if (single instanceof String) {
                try {
                    return Double.parseDouble((String) single);
                }
                catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format: " + single);
                }
            }
            else if (single instanceof List) return evaluate((List<Object>) single);

            throw new IllegalArgumentException("Invalid single element type");
        }

        Object operatorObj = parsed.get(0);
        if (!(operatorObj instanceof String)) throw new IllegalArgumentException("Operator must be a string");

        String operator = (String) operatorObj;
        Object left = parsed.get(1);
        Object right = parsed.get(2);

        double leftVal = left instanceof List ? evaluate((List<Object>) left) : parseOperand(left);
        double rightVal = right instanceof List ? evaluate((List<Object>) right) : parseOperand(right);

        return OperatorFactory.getOperator(operator).evaluate(leftVal, rightVal);
    }

    /**
     * Auxiliary function to check if operands are standard mathematical floating point numbers.
     * @param operand Object containing the operand that is parsed.
     * @return The value (double) of an operand if it is valid.
     */
    private static double parseOperand(Object operand) {
        if (operand == null) throw new IllegalArgumentException("Operand cannot be null");

        try {
            return Double.parseDouble(operand.toString());
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid operand format: " + operand);
        }
    }

    /**
     * Transforms a nested list containing the parsed version of a logical expression
     * into its equivalent arithmetic expression.
     * <pre>
     *     The following formulas will be used:
     *     ¬P - 1 - P
     *     P ∧ Q - P * Q
     *     P ∨ Q - P + Q - P * Q
     *     P ⇒ Q - 1 - P + P * Q
     *     P ⇔ Q - 1 - (P - Q)^2
     *
     *     Example:
     *     An example of a nested list is: [⇒, [P], [∧, Q, R]]
     *     Which has the equivalent in standard infix mathematical notation: P ⇒ (Q ∧ R)
     *     And the equivalent in standard infix mathematical arithmetic: 1 - P + P * (Q * R)
     * </pre>
     * @param node A nested list containing the parsed version of a logic expression.
     * @return A string containing the arithmetic equivalent in standard mathematical notation of the expression.
     */
    public static String transform(Object node) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        if (!(node instanceof List)) {
            return formatOperand(node.toString());
        }

        List<?> list = (List<?>) node;
        if (list.isEmpty()) return "";
        if (list.size() == 1 && !(list.get(0) instanceof List)) return formatOperand(list.get(0).toString());

        Object operatorObj = list.get(0);
        if (!(operatorObj instanceof String)) throw new IllegalArgumentException("Operator must be a string");

        String operator = operatorObj.toString();
        String left = transform(list.get(1));
        String right = transform(list.get(2));

        switch (operator) {
            case "∨":
                return formatOperation(left + " + " + right + " - " + left + " * " + right);
            case "∧":
                return formatOperation(left + " * " + right);
            case "⇒":
                return formatOperation("1 - " + left + " + " + left + " * " + right);
            case "⇔":
                return formatOperation("1 - (" + left + " - " + right + ")^2");
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    /**
     * Auxiliary function to parse operands, organize parenthesis and correctly parse negated operands.
     * @param s String containing the operand.
     * @return The correctly parsed arithmetic version of the operand.
     */
    private static String formatOperand(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Operand cannot be empty");
        }
        s = s.trim();

        if (s.startsWith("¬")) {
            String inner = s.substring(1).trim();
            if (inner.isEmpty()) {
                throw new IllegalArgumentException("Negation must have an operand");
            }

            if (containsLogicalOperators(inner)) {
                Expression expr = new Expression(inner);
                List<String> operators = OperatorRegistry.getLogicalOperatorSymbols();
                List<Object> parsed = Splitter.recursiveSplit(expr, operators);
                String transformed = transform(parsed);
                return "(1 - (" + transformed + "))";
            }
            else return "(1 - " + inner + ")";

        }
        return isSimpleOperand(s) ? s : "(" + s + ")";
    }

    /**
     * Auxiliary function to check for the existence of logical operators. This function is used
     * to check for expressions that have more than one literal.
     * @param s String to check in.
     * @return True if the expression is complex (contains logical operators), false otherwise.
     */
    private static boolean containsLogicalOperators(String s) {
        List<String> operators = OperatorRegistry.getLogicalOperatorSymbols();
        for (String operator: operators) {
            if (s.contains(operator)) return true;
        }
        return false;
    }

    /**
     * Formats the expression to have parenthesis.
     * @param expr Expression to format.
     * @return String containing the formatted expression.
     */
    private static String formatOperation(String expr) {
        return "(" + expr + ")";
    }

    /**
     * Checks if an operands contains a single literal.
     * @param s String representing the operand.
     * @return True if the operand contains a single literal, false otherwise.
     */
    private static boolean isSimpleOperand(String s) {
        return s.matches("^[A-Za-z0-9]+$") && !s.contains(" ");
    }
}
