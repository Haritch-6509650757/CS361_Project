package com.example.cs361_project;

import static com.example.cs361_project.Api.URL_DELETE;
import static com.example.cs361_project.Api.URL_EDIT_PROFILE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
//import java.util.Base64;
import java.util.HashMap;
import java.util.Map;



public class EditProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if(!sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(EditProfileActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }


        final Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        Button buttonDelete = findViewById(R.id.btn_delete);
        EditText editTextConfirm = findViewById(R.id.edit_text);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirmationText = editTextConfirm.getText().toString().trim();
                if ("DELETE".equals(confirmationText)) {
                    confirmDeleteAccount();
                } else {
                    Toast.makeText(EditProfileActivity.this, R.string.confirm_delete1, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle(R.string.confirm);
                builder.setMessage(R.string.sure);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
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

        Button buttonCancle = findViewById(R.id.button_cancle);
        buttonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle(R.string.confirm_cancel);
                builder.setMessage(R.string.sure);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
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

        profileImageView = findViewById(R.id.profile_image);
        profileImageView.setOnClickListener(v -> openGallery());

        ImageView menu_btn = findViewById(R.id.menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(EditProfileActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_profile){
                            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                            Toast.makeText(EditProfileActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                            return true;
                        } else if (id == R.id.menu_logout){
                            LogoutUtils.logout(EditProfileActivity.this, sharedPreferences);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    } //endOnCreate

    private void saveProfile() {
        EditText editName = findViewById(R.id.edit_name);
        EditText editVisaCard = findViewById(R.id.edit_visa);
        EditText editCvv = findViewById(R.id.edit_cvv);
        EditText editEmail = findViewById(R.id.edit_email);
        EditText editPhone = findViewById(R.id.edit_phone);

        String username = editName.getText().toString();
        String visa = editVisaCard.getText().toString();
        String cvv = editCvv.getText().toString();
        String email = editEmail.getText().toString();
        String phone = editPhone.getText().toString();

        if (username.isEmpty() || visa.isEmpty() || cvv.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, R.string.file_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        if (visa.length() < 10 && visa.length() > 10) {
            Toast.makeText(EditProfileActivity.this, R.string.file_visa, Toast.LENGTH_SHORT).show();
            return;
        }
        if (cvv.length() < 3 && cvv.length() > 3) {
            Toast.makeText(EditProfileActivity.this, R.string.file_cvv, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(EditProfileActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.matches("\\d{10}")) {
            Toast.makeText(EditProfileActivity.this, R.string.invalid_phone_format, Toast.LENGTH_SHORT).show();
            return;
        }

        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();
        Bitmap bitmap = profileImageView.getDrawingCache();
        String encodedImage = encodeImageToBase64(bitmap);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.getBoolean("success")) {
                                String imageName = jsonResponse.optString("image_name", "");

                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setTitle(R.string.success)
                                        .setMessage(R.string.profile_success)
                                        .setPositiveButton(R.string.noti_ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                intent.putExtra("profile_image", imageName);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            } else {
                                Toast.makeText(EditProfileActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EditProfileActivity.this, R.string.error_response, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, R.string.profile_failed, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("apiKey", sharedPreferences.getString("apiKey",""));
                params.put("username", username);
                params.put("visa", visa);
                params.put("cvv", cvv);
                params.put("profile_image", encodedImage);
                params.put("email", email);
                params.put("phone", phone);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

        ImageView backtoProfile = findViewById(R.id.button_back);
        backtoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EditProfileActivity", "Back button clicked");
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void deleteAccount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(EditProfileActivity.this, R.string.delete_account_success, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
//                    finish();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditProfileActivity.this, R.string.delete_account_Failed, Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<>();
            params.put("apiKey", sharedPreferences.getString("apiKey",""));
            return params;
        }
    };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(EditProfileActivity.this)
            .setTitle(R.string.confirm_delete2)
            .setMessage(R.string.msg_delete2)
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAccount();
                }
            })
            .setNegativeButton(R.string.no, null)
            .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        profileImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(this, R.string.error_image, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
    );

    private String encodeImageToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(getApplicationContext(), R.string.select_image, Toast.LENGTH_SHORT).show();
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
