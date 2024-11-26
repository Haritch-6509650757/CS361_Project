package com.example.cs361_project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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

public class LogoutUtils {

    public static void logout(Context context, SharedPreferences sharedPreferences){
        new AlertDialog.Builder(context)
                .setTitle(R.string.noti_title_exit)
                .setMessage(R.string.noti_title_logout)
                .setPositiveButton(R.string.noti_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(context);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_LOGOUT, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject c = null;
                                try {
                                    c = new JSONObject(response);
                                    String status = c.getString("status");
                                    if(status.equals("success")){
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged", "");
                                        editor.putString("username", "");
                                        editor.putString("apiKey", "");
                                        editor.apply();



                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finishAffinity();
                                    } else {
                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("DbVolley", "Volley::onErrorResponse():" + error.getMessage());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> paramV = new HashMap<>();
                                paramV.put("username", sharedPreferences.getString("username",""));
                                paramV.put("apiKey", sharedPreferences.getString("apiKey",""));
                                return paramV;
                            }
                        };
                        queue.add(stringRequest);
                    }
                })
                .setNegativeButton(R.string.noti_close, null)
                .show();
    }
}
