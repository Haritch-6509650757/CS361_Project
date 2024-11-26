package com.example.cs361_project;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ShowItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_item_page);

        // การโหลดภาพจาก URL โดยใช้ Glide
        ImageView imageView = findViewById(R.id.imageView);
        String baseUrl = "http://"+ Api.IPV4 + ":8080/api/images/24-11-2024-1732473400-48102.jpg";

        Glide.with(this)
                .load(baseUrl)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    } //end onCreate

    @Override
    public void onBackPressed() {
        // สร้าง AlertDialog เพื่อยืนยันการออกจากหน้า
        new AlertDialog.Builder(this)
                .setTitle("ยืนยันการออก")
                .setMessage("คุณต้องการออกจากหน้านี้หรือไม่?")
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // เมื่อผู้ใช้กดยืนยันจะปิด Activity
                        ShowItemActivity.super.onBackPressed(); // เรียก onBackPressed() เดิม
                    }
                })
                .setNegativeButton("ไม่", null) // ปิด Dialog โดยไม่ทำอะไร
                .show();
    }
}
