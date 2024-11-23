package com.example.cs361_project;

public class Product {
    private String Pid;
    private String Pname;
    private String Pprice;
    private String Pamount;

    public Product(String Pid, String Pname, String Pprice, String Pamount){
        this.Pid = Pid;
        this.Pname = Pname;
        this.Pprice = Pprice;
        this.Pamount = Pamount;
    }

    public String getPid() {
        return Pid;
    }
    public String getPname() {
        return Pname;
    }
    public String getPprice() {
        return Pprice;
    }
    public String getPamount() {
        return Pamount;
    }
}
