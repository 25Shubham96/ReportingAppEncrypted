package com.example.shubhammundra.fromandto.SQLite;

public class SQLITEpojo {

    private int ID;
    private String myIP = "", Instance = "", Name = "", Username = "", Password = "", company = "";

    public SQLITEpojo(int id, String myIP, String instance, String name, String username, String password, String company) {
        ID = id;
        this.myIP = myIP;
        Instance = instance;
        Name = name;
        Username = username;
        Password = password;
        this.company = company;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMyIP() {
        return myIP;
    }

    public void setMyIP(String myIP) {
        this.myIP = myIP;
    }

    public String getInstance() {
        return Instance;
    }

    public void setInstance(String instance) {
        Instance = instance;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
