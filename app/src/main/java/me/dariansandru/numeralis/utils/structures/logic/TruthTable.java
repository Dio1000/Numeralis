package me.dariansandru.numeralis.utils.structures.logic;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.utils.algorithms.LogicHelper;

/**
 * Using an instantiation of this class will allow the user to create a truth table for a given expression.
 */
public class TruthTable {
    public final Expression expression;
    public final List<String> literals;
    public List<Map<String, String>> table = new ArrayList<>();

    public TruthTable(Expression expression) {
        this.expression = expression;
        this.literals = LogicHelper.getLiterals(expression);

        this.literals.add(expression.toString());
        this.buildTable();
    }

    public void buildTable() {
        int numRows = (int) Math.pow(2, literals.size() - 1);
        for (int i = 0; i < numRows; i++) {
            StringBuilder binaryString = new StringBuilder(Integer.toBinaryString(i));
            while (binaryString.length() < literals.size() - 1) {
                binaryString.insert(0, "0");
            }

            Map<String, String> currentMap = new HashMap<>();
            for (int j = 0; j < literals.size() - 1; j++) {
                currentMap.put(literals.get(j), String.valueOf(binaryString.charAt(j)));
            }

            currentMap.put(expression.toString(),
                    String.valueOf(LogicHelper.evaluateExpression(expression, currentMap)));
            table.add(currentMap);
        }
    }

    @NonNull
    public String toString() {
        StringBuilder tableBuilder = new StringBuilder();

        for (String literal : literals) {
            tableBuilder.append(literal).append("\t");
        }
        tableBuilder.append("\n");

        for (Map<String, String> row : table) {
            for (String literal : literals) {
                tableBuilder.append(row.get(literal)).append("\t");
            }
            tableBuilder.append("\n");
        }

        return tableBuilder.toString();
    }

    public List<List<String>> getRows() {
        List<List<String>> rows = new ArrayList<>();

        for (Map<String, String> row : table) {
            List<String> newRow = new ArrayList<>(row.values());
            rows.add(newRow);
        }

        return rows;
    }

    public List<String> getLiterals() {
        return literals;
    }

    public List<Map<String, String>> getTable() {
        return table;
    }
}