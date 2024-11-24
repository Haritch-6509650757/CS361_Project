package com.example.cs361_project;

import static com.example.cs361_project.Api.URL_EDIT_PROFILE;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editVisaCard, editCvv;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // ตั้งค่า OnClickListener สำหรับปุ่ม Save
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    saveProfile();
                }
        });
    }

    // ฟังก์ชันสำหรับบันทึกข้อมูลในฐานข้อมูล
    private void saveProfile() {
        // กำหนดตัวแปรกับ EditText และ Button
        editName = findViewById(R.id.edit_name);
        editVisaCard = findViewById(R.id.edit_visa);
        editCvv = findViewById(R.id.edit_cvv);
        buttonSave = findViewById(R.id.button_save);

        String username = editName.getText().toString();
        String visa = editVisaCard.getText().toString();
        String cvv = editCvv.getText().toString();

        Log.d("EditProfile", "Username: " + username);
        Log.d("EditProfile", "Visa: " + visa);
        Log.d("EditProfile", "CVV: " + cvv);

        // ตรวจสอบว่าไม่มีช่องว่าง
        if (username.isEmpty() || visa.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // สร้างคำขอ POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // แสดงผลเมื่อบันทึกสำเร็จ
                        new AlertDialog.Builder(EditProfileActivity.this)
                                .setTitle("Success")
                                .setMessage("Profile updated successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // ปิด Dialog เมื่อกดปุ่ม OK
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // แสดงผลเมื่อเกิดข้อผิดพลาด
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", username);
                        params.put("visa", visa);
                        params.put("cvv", cvv);
                        return params;
                    }
        };

        // สร้าง RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // เพิ่มคำขอไปยัง Queue
        queue.add(stringRequest);

    }
}
