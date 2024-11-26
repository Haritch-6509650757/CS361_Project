package com.example.cs361_project;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_item_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        if(!sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(AddItemActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        ImageView image_item = findViewById(R.id.imgitem);
        Button summit_btn = findViewById(R.id.submit_btn);
        ImageView arrowback_btn = findViewById(R.id.arrowback);
        ImageView menu_btn = findViewById(R.id.menu);

        /*Test*/
        /*Button addpic_btn = findViewById(R.id.addpic_btn);
        addpic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItemActivity.this, ShowItemActivity.class);
                startActivity(intent);
            }
        });*/
        /*Test*/

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        image_item.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        image_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        summit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additem();
            }
        });

        arrowback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddItemActivity.this)
                        .setTitle(R.string.noti_title_exit)
                        .setMessage(R.string.noti_exit_page)
                        .setPositiveButton(R.string.noti_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.noti_close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AddItemActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_profile){
                            Toast.makeText(AddItemActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (id == R.id.menu_logout){
                            LogoutUtils.logout(AddItemActivity.this, sharedPreferences);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    } //end OnCreate

    public void additem(){
        final AlertDialog.Builder dialogerr = new AlertDialog.Builder(this);
        final AlertDialog.Builder dialogssucess = new AlertDialog.Builder(this);
        dialogerr.setTitle(R.string.err_title);
        dialogerr.setPositiveButton(R.string.noti_close, null);
        ByteArrayOutputStream byteArrayOutputStream;
        byteArrayOutputStream = new ByteArrayOutputStream();
        ImageView image_item = findViewById(R.id.imgitem);


        if(bitmap != null){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
            /*Log.e("Base64",base64Image);*/
            final EditText input_item = findViewById(R.id.item_input);
            final EditText price_item = findViewById(R.id.price_input);
            final EditText amount_item = findViewById(R.id.amount_input);



            if(input_item.getText().length() == 0){
                dialogerr.setMessage(R.string.noti_input_item);
                dialogerr.show();
                input_item.requestFocus();
                return;
            }

            if(price_item.getText().length() == 0){
                dialogerr.setMessage(R.string.noti_input_price);
                dialogerr.show();
                price_item.requestFocus();
                return;
            }

            if(amount_item.getText().length() == 0){
                dialogerr.setMessage(R.string.noti_input_amount);
                dialogerr.show();
                amount_item.requestFocus();
                return;
            }

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_ADDITEM,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("success")){
                                dialogssucess.setTitle(R.string.noti_additem_success);
                                dialogssucess.setMessage(R.string.noti_register_save);
                                dialogssucess.setPositiveButton(R.string.noti_ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                image_item.setImageResource(R.drawable.cloudupload);;
                                                input_item.setText("");
                                                price_item.setText("");
                                                amount_item.setText("");
                                            }
                                        });
                                dialogssucess.show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
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
                    paramV.put("item", input_item.getText().toString());
                    paramV.put("price", price_item.getText().toString());
                    paramV.put("amount", amount_item.getText().toString());
                    paramV.put("image", base64Image);
                    return paramV;
                }
            };
            queue.add(stringRequest);
        } else {
            dialogerr.setMessage(R.string.noti_img_item);
            dialogerr.show();
            image_item.requestFocus();
        }
    }

    /*@Override
    public void onBackPressed() {
        // สร้าง AlertDialog เพื่อยืนยันการออกจากหน้า
        new AlertDialog.Builder(this)
                .setTitle(R.string.noti_title_exit)
                .setMessage(R.string.noti_exit_page)
                .setPositiveButton(R.string.noti_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddItemActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.noti_close, null)
                .show();
    }*/
}
