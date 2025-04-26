package me.dariansandru.numeralis.utils.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.dariansandru.numeralis.utils.structures.logic.Clause;

/**
 * Using this abstract utility class will allow the user to solve SAT problems using a variety
 * of methods.
 */
public abstract class SATSolver {

    /**
     * Creates a list of clauses from a given input in CNF (Conjunctive Normal Form).
     * <per>
     *     Example:
     *     For the expression in CNF: (A ∨ B) ∧ (B ∨ ¬C) ∧ C
     *     The following list will be created: {A, B}, {B, ¬C}, {C}
     * </per>
     * @param input Expression in CNF (Conjunctive Normal Form)
     * @return List of instances of the Clause class.
     */
    public static List<Clause> parseClauses(String input) {
        List<Clause> clauses = new ArrayList<>();
        input = input.replaceAll("\\s+", "");
        String[] rawClauses = input.split("\\}");

        for (String rawClause : rawClauses) {
            rawClause = rawClause.replace("{", "");
            if (!rawClause.isEmpty()) {
                String[] literals = rawClause.split(",");
                List<String> clauseLiterals = new ArrayList<>();

                for (String literal : literals) {
                    if (!literal.isEmpty()) clauseLiterals.add(literal);
                }
                Clause clause = new Clause(clauseLiterals);
                clauses.add(clause);
            }
        }
        return clauses;
    }

    /**
     * Gets the literal from a unit clause.
     * @param clauses List of clauses to get the literal in a unit clause from.
     * @return First literal from a unit clause or null if no such clause was found.
     */
    public static String getUnitClauseLiteral(List<Clause> clauses) {
        for (Clause clause : clauses) {
            if (clause.isUnitClause()) return clause.getFirstLiteral();
        }
        return null;
    }

    /**
     * Checks whether a list of clauses has an empty clause.
     * @param clauses List of clauses to check in.
     * @return True if there is an empty clause, false otherwise.
     */
    public static boolean hasEmptyClause(List<Clause> clauses) {
        for (Clause clause : clauses) {
            if (clause.isEmpty()) return true;
        }
        return false;
    }

    /**
     * Handles the unit clause case.
     * @param clauses List of clauses to handle.
     * @param literal Literal in the unit clause.
     * @return A new list of clauses after handling the case.
     */
    public static List<Clause> handleLiteral(List<Clause> clauses, String literal) {
        List<Clause> newClauses = new ArrayList<>();

        for (Clause clause : clauses) {
            if (clause.containsLiteral(literal)) continue;
            newClauses.add(clause.eliminateLiteral(literal));
        }
        return newClauses;
    }

    /**
     * Gets the first pure literal that is found.
     * @param clauses List of clauses to search in.
     * @return First pure literal that is found or null if there is none.
     */
    public static String getPureLiteral(List<Clause> clauses) {
        Set<String> positiveLiterals = new HashSet<>();
        Set<String> negativeLiterals = new HashSet<>();

        for (Clause clause : clauses) {
            for (String literal : clause.getLiterals()) {
                if (literal.startsWith("¬")) negativeLiterals.add(literal.substring(1));
                else positiveLiterals.add(literal);
            }
        }

        for (String literal : positiveLiterals) {
            if (!negativeLiterals.contains(literal)) return literal;
        }

        for (String literal : negativeLiterals) {
            if (!positiveLiterals.contains(literal)) return "¬" + literal;
        }

        return null;
    }

    /**
     * Selects a free literal (unassigned variable) from the list of clauses.
     * <pre>
     * Example:
     * Given the clauses: {A, B}, {¬A, C}
     * It may select "A", "B", or "C" as a free literal.
     * </pre>
     */
    public static String selectFreeLiteral(List<Clause> clauses) {
        Set<String> assignedLiterals = new HashSet<>();

        for (Clause clause : clauses) {
            for (String literal : clause.getLiterals()) {
                String variable = literal.startsWith("¬") ? literal.substring(1) : literal;
                assignedLiterals.add(variable);
            }
        }

        return assignedLiterals.isEmpty() ? null : assignedLiterals.iterator().next();
    }

    /**
     * Checks if a list of clauses is satisfiable (there exists an interpretation under which
     * it is true).
     * @param clauses List of clauses equivalent to an expression in CNF.
     * @return True if there exists an interpretation under which the expression given by the clauses is true, false otherwise
     */
    public static boolean isSatisfiable(List<Clause> clauses) {
        if (clauses.isEmpty()) return true;
        if (hasEmptyClause(clauses)) return false;

        String unitClauseLiteral = getUnitClauseLiteral(clauses);
        if (unitClauseLiteral != null) {
            List<Clause> newClauses = handleLiteral(clauses, unitClauseLiteral);
            return isSatisfiable(newClauses);
        }

        String pureLiteral = getPureLiteral(clauses);
        if (pureLiteral != null) {
            List<Clause> newClauses = handleLiteral(clauses, pureLiteral);
            return isSatisfiable(newClauses);
        }

        String freeLiteral = selectFreeLiteral(clauses);
        if (freeLiteral == null) return false;

        List<Clause> clausesWithLiteral = handleLiteral(clauses, freeLiteral);
        if (isSatisfiable(clausesWithLiteral)) return true;

        List<Clause> clausesWithNegatedLiteral = handleLiteral(clauses, LogicHelper.negateLiteral(freeLiteral));
        return isSatisfiable(clausesWithNegatedLiteral);
    }
}
