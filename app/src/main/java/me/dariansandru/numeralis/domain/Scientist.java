package me.dariansandru.numeralis.domain;

public class Scientist implements User{
    private String name;
    private String password;

    public Scientist(){
        this.name = "None";
        this.password = "None";
    }

    public Scientist(String name, String password){
        this.name = name;
        this.password = password;
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
}
