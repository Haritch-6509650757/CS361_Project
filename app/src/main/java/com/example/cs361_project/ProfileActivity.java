package com.example.cs361_project;

import static com.example.cs361_project.Api.URL_PROFILE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
        setContentView(R.layout.profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if(!sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROFILE,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            ImageView profileImageView = findViewById(R.id.profile_image);
                            TextView textViewUserId = findViewById(R.id.id_user);
                            TextView textViewUsername = findViewById(R.id.username_user);
                            TextView textViewJob = findViewById(R.id.job_user);
                            TextView textViewEmail = findViewById(R.id.email_user);
                            TextView textViewPhone = findViewById(R.id.phone_user);

                            JSONObject c = new JSONObject(response);
                            String status = c.getString("status");
                            String message = c.getString("message");
                            String id = c.getString("id");
                            String username = c.getString( "username");
                            String job = c.getString( "job");
                            String profile_image = c.getString("profile_image");
                            String email = c.getString("email");
                            String phone = c.getString("phone");
                            String pic_profile;

                            if(profile_image.isEmpty()){
                                pic_profile = null;
                            }else {
                                pic_profile = "http://" + Api.IPV4 + ":8080/api/images/" + profile_image;
                            }

                            if (status.equals("success")) {
                                textViewUserId.setText("ID: " + id);
                                textViewUsername.setText("Username: " + username);
                                textViewJob.setText("Job: " + job);
                                textViewEmail.setText("Email: " + email);
                                textViewPhone.setText("Phone: " + phone);

                                Glide.with(ProfileActivity.this)
                                        .load(pic_profile != null ? pic_profile : R.drawable.profile_image)
                                        //.placeholder(R.drawable.profile_image)
                                        .into(profileImageView);

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


        Button editProfileButton = findViewById(R.id.btn_edit_profile);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageView buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle(R.string.confirm);
                builder.setMessage(R.string.sure);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ProfileActivity.this, GameActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        ImageView menu_btn = findViewById(R.id.menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_profile){
                            Toast.makeText(ProfileActivity.this, R.string.profilethis, Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (id == R.id.menu_logout){
                            LogoutUtils.logout(ProfileActivity.this, sharedPreferences);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
}
