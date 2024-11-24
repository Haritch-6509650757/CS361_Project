package com.example.cs361_project;

import static com.example.cs361_project.Api.URL_PROFILE;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private boolean isEditProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // เข้าถึง TextView ที่แสดงข้อมูล
        TextView textViewUserId = findViewById(R.id.id_user);
        TextView textViewUsername = findViewById(R.id.username_user);

        // สร้าง RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // สร้าง StringRequest สำหรับดึงข้อมูล
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // แปลง response (String) เป็น JSONObject
                            JSONObject jsonResponse = new JSONObject(response);

                            // ดึงข้อมูล JSON Array ที่ชื่อ "data"
                            JSONArray users = jsonResponse.getJSONArray("data");

                            // พิมพ์จำนวนผู้ใช้ (length) ลงใน Logcat
//                            Log.d("MainActivity", "Number of users: " + users.length());

                            // ถ้าข้อมูลมีอยู่ใน array
                            if (users.length() > 0) {
                                // ดึงข้อมูลผู้ใช้แรกใน array
                                JSONObject user = users.getJSONObject(0);
                                String userId = user.getString("id");
                                String username = user.getString("username");

                                // ตั้งค่าข้อความใน TextView
                                textViewUserId.setText("ID : " + userId);
                                textViewUsername.setText("Username : " + username);
                            }
                        } catch (Exception e) {
                            textViewUserId.setText("Error parsing data");
                            textViewUsername.setText("Error parsing data");
                            Log.e("MainActivity", "Error parsing JSON", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textViewUserId.setText("Failed to load data");
                        textViewUsername.setText("Failed to load data");
                        Log.e("MainActivity", "Volley Error", error);
                    }
                });

        // เพิ่ม StringRequest ลงใน Queue
        queue.add(stringRequest);

        Button editProfileButton = findViewById(R.id.button_edit_profile);
        editProfileButton.setOnClickListener(v -> {
            if (!isEditProfile) {
                setContentView(R.layout.activity_edit_profile);
                isEditProfile = true;

                ImageView backButton = findViewById(R.id.button_back);
                backButton.setOnClickListener(back -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Confirm back");
                    builder.setMessage("Do you want to return to the previous page?");

                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        setContentView(R.layout.activity_profile);
                        isEditProfile = false;
                        recreate();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                });
            }
        });
    }
}