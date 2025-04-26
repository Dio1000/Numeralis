package me.dariansandru.numeralis.utils.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.dariansandru.numeralis.utils.structures.logic.Clause;

public abstract class SATSolver {

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

    public static String getUnitClauseLiteral(List<Clause> clauses) {
        for (Clause clause : clauses) {
            if (clause.isUnitClause()) return clause.getFirstLiteral();
        }
        return null;
    }

    public static boolean hasEmptyClause(List<Clause> clauses) {
        for (Clause clause : clauses) {
            if (clause.isEmpty()) return true;
        }
        return false;
    }

    public static List<Clause> handleLiteral(List<Clause> clauses, String literal) {
        List<Clause> newClauses = new ArrayList<>();

        for (Clause clause : clauses) {
            if (clause.containsLiteral(literal)) continue;
            newClauses.add(clause.eliminateLiteral(literal));
        }
        return newClauses;
    }

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
