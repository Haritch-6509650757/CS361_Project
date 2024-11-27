package com.example.cs361_project;

import static com.example.cs361_project.Api.URL_PROFILE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
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
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);



        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ImageView profileImageView = findViewById(R.id.profile_image);
                            TextView textViewUserId = findViewById(R.id.id_user);
                            TextView textViewUsername = findViewById(R.id.username_user);

                            JSONObject c = new JSONObject(response);
                            String status = c.getString("status");
                            String message = c.getString("message");
                            String id = c.getString("id");
                            String username = c.getString("username");
                            String profile_image = c.getString("profile_image");


                            String pic_profile = "http://" + Api.IPV4 + ":8080/api/images/" + profile_image;

                            if(status.equals("success")){
                                textViewUserId.setText(id);
                                textViewUsername.setText(username);
                                Glide.with(ProfileActivity.this).load(pic_profile).into(profileImageView);

                            } else {
                                Log.e("err", message);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("apiKey", sharedPreferences.getString("apiKey",""));
                return paramV;
            }
        };
        queue.add(stringRequest);

        Button editProfileButton = findViewById(R.id.button_edit_profile);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
















        //

        // รับชื่อรูปภาพจาก Intent
        //String imageName = getIntent().getStringExtra("profile_image_name");

        //if (imageName != null && !imageName.isEmpty()) {
            // สร้าง URL สำหรับรูปภาพ
            //String imageUrl = "http://your-server.com/uploads/" + imageName;

            // ใช้ Glide โหลดรูปภาพ
            //Glide.with(this)
                    //.load(imageUrl)
                    //.placeholder(R.drawable.placeholder_image) // รูปภาพที่จะแสดงระหว่างโหลด
                    //.error(R.drawable.error_image) // รูปภาพเมื่อเกิดข้อผิดพลาด
                    //.into(profileImageView);
        //}

    }
}