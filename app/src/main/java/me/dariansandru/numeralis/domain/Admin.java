package me.dariansandru.numeralis.domain;

import java.util.ArrayList;
import java.util.List;

public class Admin implements User {
    private String name;
    private String password;
    private List<String> permissions;

    public Admin() {
        this.name = "None";
        this.password = "None";
        this.permissions = new ArrayList<>();
    }

    public Admin(String name, String password, List<String> permissions) {
        this.name = name;
        this.password = password;
        this.permissions = permissions;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
