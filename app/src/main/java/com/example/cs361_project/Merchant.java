package com.example.cs361_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

//import java.util.Base64;

public class Merchant {
    private int Mid;
    private String Mitem;
    private double Mprice;
    private int Mamount;
    private String Mimage;

    public Merchant(int Mid, String Mitem, double Mprice, int Mamount, String Mimage){
        this.Mid = Mid;
        this.Mitem = Mitem;
        this.Mprice = Mprice;
        this.Mamount = Mamount;
        this.Mimage = Mimage;
    }

    public int Mid() { return Mid; }
    public void setMid(Integer Mid) {
        this.Mid = Mid;
    }

    public String getMitem() { return Mitem; }
    public void setMitem(String Mitem){
        this.Mitem = Mitem;
    }

    public double getMprice() { return Mprice; }
    public void setMprice(double Mprice){
        this.Mprice = Mprice;
    }

    public int getMamount() {
        return Mamount;
    }
    public void setMamount(int Mamount){
        this.Mamount = Mamount;
    }

    public void setMimage(String Mimage) { this.Mimage = Mimage; }
    public String getMimage() { return Mimage; }

}
