package me.dariansandru.numeralis.utils.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.dariansandru.numeralis.parser.Evaluator;
import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.parser.OperatorRegistry;
import me.dariansandru.numeralis.utils.structures.logic.Clause;
import me.dariansandru.numeralis.utils.structures.logic.TruthTable;

/**
 * Using this abstract utility class allows the user to take advantage of multiple functions
 * used for CNF / DNF conversion and clauses for the SAT problem.
 */
public abstract class LogicHelper {

    /**
     * Gets the literals from a given expression.
     * <pre>
     *     Example:
     *     For expression: ((A1 ∨ A2) ∧ B) ⇔ C
     *     The extracted literals are: A1, A2, B, C
     * </pre>
     * @param expression The expression to extract the literals from.
     * @return A list of Strings representing the found literals.
     */
    public static List<String> getLiterals(Expression expression) {
        String expressionString = expression.toString();
        List<String> literals = new ArrayList<>();

        int index = 0;
        while (index < expressionString.length()) {
            String literal = getLiteral(expressionString.substring(index));
            if (!literal.isEmpty() && !literals.contains(literal)) {
                literals.add(literal);
                index += literal.length();
            }
            else index++;
        }

        return literals;
    }

    /**
     * Gets the first literal from an expression String.
     * @param s String containing an expression.
     * @return The first extracted literal (or an empty String if none was found).
     */
    public static String getLiteral(String s) {
        StringBuilder literal = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (literal.toString().isEmpty() && Character.isLetter(c)) literal.append(c);
            else if (!literal.toString().isEmpty() && Character.isLetterOrDigit(c)) literal.append(c);
            else break;
        }

        return literal.toString();
    }

    /**
     * Creates an interpretable expression (contains the actual values of literals).
     * <pre>
     *     Example:
     *     For expression: ((A1 ∨ A2) ∧ B) ⇒ C
     *     Will be converted into the arithmetic expression: 1 - ((A1 + A2 - A1 * A2) * B) + ((A1 + A2 - A1 * A2) * B) * C
     *     And the table: A1 - 1, A2 - 0, B - 0, C - 1
     *     The returned interpretable string is: 1 - ((1 + 0 - 1 * 0) * 0) + ((1 + 0 - 1 * 0) * 0) * 1
     * </pre>
     * @param expression The expression in standard logic notation to convert.
     * @param table Mapping from all literals to their values under a given interpretation.
     * @return String containing new interpretable expression.
     */
    public static Expression getInterpretableExpression(Expression expression, Map<String, String> table) {
        List<String> operators = OperatorRegistry.getLogicalOperatorSymbols();
        List<Object> result = Splitter.recursiveSplit(expression, operators);
        String expressionString = Evaluator.transform(result);

        StringBuilder interpretableStringBuilder = new StringBuilder();

        for (int i = 0; i < expressionString.length() ; i++) {
            String literal = getLiteral(expressionString.substring(i));
            if (!literal.isEmpty()) interpretableStringBuilder.append(table.get(literal));
            else interpretableStringBuilder.append(expressionString.charAt(i));
        }

        return new Expression(interpretableStringBuilder.toString());
    }

    /**
     * Evaluates an expression in standard logic notation under a given interpretation.
     * @param expression Expression to evaluate.
     * @param table Mapping from all literals to their values under a given interpretation.
     * @return Integer representing the truth value of the expression under the given interpretation.
     */
    public static int evaluateExpression(Expression expression, Map<String, String> table) {
        Expression interpretableExpression = getInterpretableExpression(expression, table);
        List<String> operators = OperatorRegistry.getOperatorSymbols();
        return (int) Evaluator.evaluate(Splitter.recursiveSplit(interpretableExpression, operators));
    }

    /**
     * Convert an instance of TruthTable to an expression in CNF (Conjunctive Normal Form).
     * @param truthTable Truth table of the expression.
     * @return An expression in CNF equivalent to the expression given by the truth table.
     */
    public static Expression truthTableToCNF(TruthTable truthTable) {
        List<String> literals = truthTable.getLiterals();
        List<List<String>> rows = truthTable.getRows();
        StringBuilder expressionString = new StringBuilder();

        for (List<String> row : rows) {
            int size = row.size();
            String truthValue = row.get(size - 1);

            if (!"0".equals(truthValue)) continue;

            StringBuilder clause = new StringBuilder();
            for (int i = 0; i < size - 1; i++) {
                String literal = literals.get(i);
                String value = row.get(i);

                if ("0".equals(value)) clause.append(literal);
                else clause.append("¬").append(literal);

                if (i < size - 2) clause.append(" ∨ ");
            }
            expressionString.append("(").append(clause).append(") ∧ ");
        }

        int len = expressionString.length();
        if (len >= 3) {
            expressionString.setLength(len - 3);
        }

        return new Expression(expressionString.toString());
    }

    /**
     * Convert an instance of TruthTable to an expression in DNF (Disjunctive Normal Form).
     * @param truthTable Truth table of the expression.
     * @return An expression in DNF equivalent to the expression given by the truth table.
     */
    public static Expression truthTableToDNF(TruthTable truthTable) {
        List<String> literals = truthTable.getLiterals();
        List<List<String>> rows = truthTable.getRows();
        StringBuilder expressionString = new StringBuilder();

        for (List<String> row : rows) {
            int size = row.size();
            String truthValue = row.get(size - 1);

            if (!"1".equals(truthValue)) continue;

            StringBuilder clause = new StringBuilder();
            for (int i = 0; i < size - 1; i++) {
                String literal = literals.get(i);
                String value = row.get(i);

                if ("0".equals(value)) clause.append("¬").append(literal);
                else clause.append(literal);

                if (i < size - 2) clause.append(" ∧ ");
            }
            expressionString.append("(").append(clause).append(") ∨ ");
        }

        int len = expressionString.length();
        if (len >= 3) {
            expressionString.setLength(len - 3);
        }

        return new Expression(expressionString.toString());
    }

    /**
     * Creates a list of clauses from a given expression in CNF (Conjunctive Normal Form).
     * @param expression Expression in CNF to get the clauses from.
     * @return A list of clauses that are equivalent to the expression.
     */
    public static List<Clause> getClausesFromCNF(Expression expression) {
        List<Clause> clauses = new ArrayList<>();
        String expressionString = expression.toString();
        List<String> disjunctions = Arrays.stream(expressionString.split("∧"))
                                    .map(String::trim)
                                    .collect(Collectors.toList());

        for (String disjunction : disjunctions) {
            Expression disjunctionExpression = new Expression(disjunction);
            Clause newClause = new Clause(disjunctionExpression);
            clauses.add(newClause);
        }

        return clauses;
    }

    /**
     * Auxiliary function to negate a literal (A <- ¬A, ¬A <- A)
     * @param literal Literal to negate.
     * @return String representing the new negated literal.
     */
    public static String negateLiteral(String literal) {
        if (literal.charAt(0) == '¬') return String.valueOf(literal.charAt(1));
        else return ("¬" + literal);
    }
}
