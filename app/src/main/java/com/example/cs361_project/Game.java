package com.example.cs361_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

//import java.util.Base64;

public class Game {
    private String Pid;
    private String Pname;
    private Bitmap Pimage;
    private double Pprice;
    private int Pamount;
    private String Pcategory;

    public Game(String Pid, String Pname, Bitmap Pimage, double Pprice, int Pamount, String Pcategory){
        this.Pid = Pid;
        this.Pname = Pname;
        this.Pimage = Pimage;
        this.Pprice = Pprice;
        this.Pamount = Pamount;
        this.Pcategory = Pcategory;
    }

    public String getPid() { return Pid; }
    public void setPid(String Pid) {
        this.Pid = Pid;
    }

    public String getPname() { return Pname; }
    public void setPname(String Pname){
        this.Pname = Pname;
    }

    public Bitmap getPimage(){
        return Pimage;
    }
    public void setPimagel(Bitmap Pimage){
        this.Pimage = Pimage;
    }

    public double getPprice() { return Pprice; }
    public void setPprice(double Pprice){
        this.Pprice = Pprice;
    }

    public int getPamount() {
        return Pamount;
    }
    public void setPamount(int Pamount){
        this.Pamount = Pamount;
    }

    public void setPcategory(String Pcategory) { this.Pcategory = Pcategory; }
    public String getPcategory() { return Pcategory; }

}
