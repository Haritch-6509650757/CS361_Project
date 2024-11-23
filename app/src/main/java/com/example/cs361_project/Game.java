package com.example.cs361_project;

public class Game {
    private String Pid;
    private String Pname;
    private String Pimageurl;
    private double Pprice;
    private int Pamount;

    public Game(String Pid, String Pname, String Pimageurl, double Pprice, int Pamount){
        this.Pid = Pid;
        this.Pname = Pname;
        this.Pimageurl = Pimageurl;
        this.Pprice = Pprice;
        this.Pamount = Pamount;
    }

    public String getPid() {
        return Pid;
    }
    public void setPid(String Pid) {
        this.Pid = Pid;
    }

    public String getPname() {
        return Pname;
    }
    public void setPname(String Pname){
        this.Pname = Pname;
    }

    public String getPimageurl(){
        return Pimageurl;
    }
    public void setPimageurl(String Pimageurl){
        this.Pimageurl = Pimageurl;
    }

    public double getPprice() {
        return Pprice;
    }
    public void setPprice(double Pprice){
        this.Pprice = Pprice;
    }

    public int getPamount() {
        return Pamount;
    }
    public void setPamount(int Pamount){
        this.Pamount = Pamount;
    }
}
