package me.dariansandru.numeralis.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

import me.dariansandru.numeralis.parser.operations.Operator;

/**
 * Using this abstract utility class will allow the user to access all the standard notation
 * mathematical or logic notations used as operators.
 */
public abstract class OperatorRegistry {

    private static final Reflections reflections = new Reflections("me.dariansandru.numeralis.parser", "me.dariansandru.numeralis.parser.operations");

    public static boolean isOperator(String input) {
        return getOperatorSymbols().contains(input);
    }

    // TODO Fix temporary manual insertion of operations using Reflexions
    public static List<String> getOperatorSymbols() {
        List<String> result = new ArrayList<>();
        result.add("+");
        result.add("-");
        result.add("*");
        result.add("/");
        result.add("%");
        result.add("^");

        return result;
    }

    public static List<String> getLogicalOperatorSymbols() {
        List<String> result = new ArrayList<>();
        result.add("⇔");
        result.add("⇒");
        result.add("∧");
        result.add("∨");

        return result;
    }
}
