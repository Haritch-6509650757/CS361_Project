package com.example.cs361_project;

import static com.example.cs361_project.Api.URL_DELETE;
import static com.example.cs361_project.Api.URL_EDIT_PROFILE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    private static final int PICK_IMAGE_REQUEST = 1;  // Code for opening gallery
    private ImageView profileImageView;
    //private Button uploadImage;
    //private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
                // หลังจากบันทึกสำเร็จ ส่งชื่อรูปภาพไปยัง ProfileActivity
                //Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                //intent.putExtra("profile_image", "6744540d71d94.jpg"); // ส่งชื่อไฟล์รูปภาพที่อัปโหลด
                //startActivity(intent);
                //finish();
            }
        });

        Button buttonDelete = findViewById(R.id.button_delete);
        EditText editTextConfirm = findViewById(R.id.edit_text);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirmationText = editTextConfirm.getText().toString().trim();
                if ("DELETE".equals(confirmationText)) {
                    confirmDeleteAccount();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please type 'DELETE' to confirm", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // สร้าง AlertDialog เพื่อถามผู้ใช้ก่อนย้อนกลับ
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Confirm Back");
                builder.setMessage("Are you sure you want to go back to the profile page?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                // สร้าง AlertDialog เพื่อถามผู้ใช้ก่อนย้อนกลับ
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Confirm cancle");
                builder.setMessage("Are you sure you want to go back to the profile page?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
        // เมื่อกดที่ ImageView จะเปิด Gallery เพื่อเลือกภาพ
        profileImageView.setOnClickListener(v -> openGallery());

    } //endOnCreate

    // ฟังก์ชันสำหรับบันทึกข้อมูลในฐานข้อมูล
    private void saveProfile() {
        EditText editName = findViewById(R.id.edit_name);
        EditText editVisaCard = findViewById(R.id.edit_visa);
        EditText editCvv = findViewById(R.id.edit_cvv);

        String username = editName.getText().toString();
        String visa = editVisaCard.getText().toString();
        String cvv = editCvv.getText().toString();

//        Log.d("EditProfile", "Username: " + username);
//        Log.d("EditProfile", "Visa: " + visa);
//        Log.d("EditProfile", "CVV: " + cvv);

        if (username.isEmpty() || visa.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // แปลงรูปภาพเป็น Base64
        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();
        Bitmap bitmap = profileImageView.getDrawingCache();
        String encodedImage = encodeImageToBase64(bitmap);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // แปลง Response เป็น JSON Object
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.getBoolean("success")) {
                                // รับชื่อไฟล์รูปภาพจาก Response
                                String imageName = jsonResponse.optString("image_name", "");

                                // สร้าง AlertDialog แสดงความสำเร็จ
                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setTitle("Success")
                                        .setMessage("Profile updated successfully")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                                // ส่งข้อมูลไปยังหน้า ProfileActivity
                                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                intent.putExtra("profile_image", imageName); // ส่งชื่อไฟล์รูปภาพ
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            } else {
                                // แสดงข้อความผิดพลาดจาก Response
                                Toast.makeText(EditProfileActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProfileActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", "1"); // ระบุ ID ผู้ใช้
                params.put("username", username); // ชื่อผู้ใช้
                params.put("visa", visa); // หมายเลข Visa
                params.put("cvv", cvv); // CVV
                params.put("profile_image", encodedImage); // รูปภาพในรูปแบบ Base64
                return params;
            }
        };


        // สร้าง RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // เพิ่มคำขอไปยัง Queue
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

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this account? This action cannot be undone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteAccount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditProfileActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class); //เดี๋ยวค่อยแก้เป็นหน้า Login
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, "Failed to delete account", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", "1"); // ใส่ ID ของผู้ใช้
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    // ฟังก์ชันสำหรับการออกจากระบบ (Logout)
    private void handleLogout() {
        // ตัวอย่างการแสดง Toast แจ้งเตือนเมื่อกด Logout
        Toast.makeText(EditProfileActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();

        // ในกรณีนี้สามารถลบข้อมูลการล็อกอิน (เช่น SharedPreferences) และทำการเปลี่ยนหน้า
        // ตัวอย่างการย้ายไปหน้าหลักหรือหน้า Login
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class); //เดี๋ยวค่อยเปลี่ยนเป็นหน้า Login
        startActivity(intent);
        finish();
    }

    private void openGallery() {
        // เรียกใช้ Intent:
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
                        e.printStackTrace();
                        Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private String encodeImageToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(getApplicationContext(), "Select the image first", Toast.LENGTH_SHORT).show();
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
