package com.example.cs361_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
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

        // ตั้งค่า Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://yourserver.com/") // เปลี่ยนเป็น URL ของ API คุณ
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api apiService = retrofit.create(Api.class);

        // เรียกใช้ API
        apiService.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ดึงข้อมูลจาก API
                    List<User> users = response.body();
                    StringBuilder userInfo = new StringBuilder();
                    for (User user : users) {
                        userInfo.append("Name: ").append(user.name).append("\n");
                        userInfo.append("Email: ").append(user.email).append("\n\n");
                    }
                    textView.setText(userInfo.toString());
                } else {
                    textView.setText("Failed to load data");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textView.setText("Error: " + t.getMessage());
                Log.e("MainActivity", "API Call Failed", t);
            }

        Button editProfileButton = findViewById(R.id.button_edit_profile);
        editProfileButton.setOnClickListener(v -> {

            if (!isEditProfile) {
                setContentView(R.layout.activity_edit_profile); // เปลี่ยนหน้า
                isEditProfile = true;

                ImageView backButton = findViewById(R.id.button_back);
                backButton.setOnClickListener(back -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Confirm back");
                    builder.setMessage("Do you want to return to the previous page?");

                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        // กลับไปหน้า activity_profile
                        setContentView(R.layout.activity_profile);
                        isEditProfile = false;
                        recreate(); // รีโหลดเพื่อให้ปุ่ม Edit Profile ทำงานได้ใหม่
                    });
                    builder.setNegativeButton("No", (dialog, which) -> {
                        // ปิด dialog
                        dialog.dismiss();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                });
            }
        });
    }
}