package com.example.cs361_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    final String ipv4 = "localhostipv4";
    final String url_getusers = "http://"+ipv4+":8080/api/getusers.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final TextView register_txtview = findViewById(R.id.register);

        register_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //ทดสอบ show ข้อมูลจากฐานข้อมูล

        final Button login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchUsers();
            }
        });
        //On Create
    }
    private void fetchUsers() {
        new Thread(() -> {
            try {
                URL url = new URL(url_getusers);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                runOnUiThread(() -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response.toString());
                        StringBuilder userInfo = new StringBuilder();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject user = jsonArray.getJSONObject(i);
                            String username = user.getString("username");
                            String password = user.getString("password");
                            String job = user.getString("job");

                            Log.d("USER", "Username: " + username + ", Job: " + job);

                            userInfo.append("Username: ").append(username)
                                    .append(", Password: ").append(password)
                                    .append(", Job: ").append(job).append("\n");

                        }
                        Toast.makeText(LoginActivity.this, "Users fetched successfully!" + userInfo.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Failed to parse JSON!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed to fetch users!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
