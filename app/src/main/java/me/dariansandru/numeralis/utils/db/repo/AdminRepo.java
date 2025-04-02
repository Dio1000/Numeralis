package me.dariansandru.numeralis.utils.db.repo;

import java.util.List;

import me.dariansandru.numeralis.domain.Admin;
import me.dariansandru.numeralis.utils.db.DBConnection;

public class AdminRepo {
    private static List<Admin> admins;

    public static List<Admin> getAdmins(){
        return admins;
    }

    public static void saveAdmins(List<Admin> admins){
        DBConnection.saveAllAdmins(admins);
    }
}
