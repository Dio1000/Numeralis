package me.dariansandru.numeralis.utils.db.repo;

import me.dariansandru.numeralis.domain.Scientist;
import me.dariansandru.numeralis.utils.db.DBConnection;

import java.util.List;

public abstract class ScientistRepo {
    private static List<Scientist> scientists;

    public static List<Scientist> getScientists(){
        return scientists;
    }

    public static void saveScientists(List<Scientist> scientists){
        DBConnection.saveAllScientists(scientists);
    }

    public static void init(){
        scientists = DBConnection.getAllScientists();
    }
}
