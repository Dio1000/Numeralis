package me.dariansandru.numeralis.utils.structures;

import androidx.annotation.NonNull;

import me.dariansandru.numeralis.utils.algorithms.BaseConverter;

/**
 * Using an instantiation of this class will allow the user to use numbers that may not be decimal.
 */
public class BaseNumber {
    private String representation;
    private int base;

    public BaseNumber(String representation, int base){
        this.representation = representation;
        this.base = base;
    }

    public String getRepresentation(){
        return this.representation;
    }

    public void setRepresentation(String representation){
        this.representation = representation;
    }

    public int getBase(){
        return this.base;
    }

    public void setBase(int base){
        this.base = base;
    }

    public long toDecimal(){
        return BaseConverter.convertToDecimal(this);
    }

    @NonNull
    @Override
    public String toString() {
        return representation + " (Base " + base + ")";
    }
}
