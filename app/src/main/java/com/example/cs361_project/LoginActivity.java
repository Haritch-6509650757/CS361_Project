package com.example.cs361_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    String username, apiKey,job;
    SharedPreferences sharedPreferences;

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
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        final TextView register_txtview = findViewById(R.id.register);
        register_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        final Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    } //end OnCreate

    public void login(){
        final EditText login_username = findViewById(R.id.loginusername);
        final EditText login_password = findViewById(R.id.loginpassword);

        final AlertDialog.Builder dialogerr = new AlertDialog.Builder(this);
        dialogerr.setTitle(R.string.err_title);
        dialogerr.setPositiveButton(R.string.noti_close, null);

        if(login_username.getText().length() == 0){
            dialogerr.setMessage(R.string.noti_input_username);
            dialogerr.show();
            login_username.requestFocus();
            return;
        }

        if(login_password.getText().length() == 0){
            dialogerr.setMessage(R.string.noti_input_password);
            dialogerr.show();
            login_password.requestFocus();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("DbVolley", response);
                        int jsonStart = response.indexOf('{');
                        try {
                            JSONObject c = new JSONObject(response.substring(jsonStart));
                            String status = c.getString("status");
                            String message = c.getString("message");
                            Log.i("CheckerrJson", c.toString());
                            if(status.equals("success")){
                                username = c.getString("username");
                                apiKey = c.getString("apiKey");
                                job = c.getString("job");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("logged", "true");
                                editor.putString("username", username);
                                editor.putString("apiKey", apiKey);
                                editor.apply();
                                if(job.equals("แม่ค้า") || job.equals("Seller")){
                                    Intent intent = new Intent(LoginActivity.this, MockHomeSellerActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MockHomeCustomerActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else if(message.equals("Retry with correct username and password")){
                                dialogerr.setMessage(R.string.retryconrectuserpass);
                                dialogerr.show();
                            } else {
                                Log.e("err", message);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("DbVolley", "Volley::onErrorResponse():"+error.getMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("username",login_username.getText().toString());
                paramV.put("password",login_password.getText().toString());
                return paramV;
            }
        };
        queue.add(stringRequest);

    }
}
