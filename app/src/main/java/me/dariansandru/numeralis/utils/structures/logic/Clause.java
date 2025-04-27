package me.dariansandru.numeralis.utils.structures.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.utils.algorithms.logic.LogicHelper;

/**
 * Using an instantiation of this class will allow the user to work with a clause (a disjunction of literals).
 */
public class Clause {
    private final List<String> literals;

    public Clause(Expression expression) {
        this.literals = Arrays.stream(expression.toString().split("∨"))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public Clause(List<String> literals) {
        this.literals = literals;
    }

    public Clause(String literal) {
        this.literals = new ArrayList<>();
        this.literals.add(literal);
    }

    public Clause() {
        this.literals = new ArrayList<>();
    }

    public List<String> getLiterals() {
        return this.literals;
    }

    public Set<String> getLiteralsSet() {
        return new HashSet<>(this.literals);
    }

    public String getFirstLiteral() {
        return this.literals.get(0);
    }

    public boolean isEmpty() {
        return literals.isEmpty();
    }

    public boolean isUnitClause() {
        return literals.size() == 1;
    }

    public boolean containsLiteral(String literal) {
        for (String l : literals) {
            if (l.equals(literal)) return true;
        }

        return false;
    }

    public Clause eliminateLiteral(String literal) {
        List<String> newLiterals = new ArrayList<>();
        String negatedLiteral = LogicHelper.negateLiteral(literal);

        for (String l : literals) {
            if (!l.equals(negatedLiteral)) newLiterals.add(l);
        }
        return new Clause(newLiterals);
    }

    public boolean equalsTo(Clause other) {
        Set<String> thisSet = new HashSet<>(this.literals);
        Set<String> otherSet = new HashSet<>(other.literals);
        return thisSet.equals(otherSet);
    }
}
