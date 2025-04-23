package me.dariansandru.numeralis.parser;

import me.dariansandru.numeralis.parser.operations.OperatorFactory;

import java.util.List;

public class Evaluator {

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

    private static double parseOperand(Object operand) {
        if (operand == null) throw new IllegalArgumentException("Operand cannot be null");

        try {
            return Double.parseDouble(operand.toString());
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid operand format: " + operand);
        }
    }

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
                return formatOperation("(" + left + " - " + right + ")^2");
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    private static String formatOperand(String s) {
        if (s == null || s.trim().isEmpty()) throw new IllegalArgumentException("Operand cannot be empty");
        s = s.trim();

        if (s.startsWith("¬")) {
            String inner = s.substring(1).trim();
            if (inner.isEmpty()) throw new IllegalArgumentException("Negation must have an operand");

            String formattedInner = formatOperand(inner);
            return !isSimpleOperand(formattedInner) ?
                    "(1 - " + formattedInner + ")" :
                    "1 - " + formattedInner;
        }

        return isSimpleOperand(s) ? s : "(" + s + ")";
    }

    private static String formatOperation(String expr) {
        return "(" + expr + ")";
    }

    private static boolean isSimpleOperand(String s) {
        return s.matches("^[A-Za-z0-9]+$") && !s.contains(" ");
    }
}
