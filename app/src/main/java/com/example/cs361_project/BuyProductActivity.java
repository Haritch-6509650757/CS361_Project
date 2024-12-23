package com.example.cs361_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class BuyProductActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    String Pname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.buy_product); //กำหนดหน้า layout home
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.buy_product), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if(!sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(BuyProductActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        Pname = getIntent().getStringExtra("game_name");
        double Pprice = getIntent().getDoubleExtra("game_price", 0.0);
        int Pamount = getIntent().getIntExtra("game_amount", 0);

        byte[] byteArray = getIntent().getByteArrayExtra("game_cover");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        int newWidth = bitmap.getWidth() * 3;
        int newHeight = bitmap.getHeight() * 3;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        TextView name = findViewById(R.id.name_buy);
        TextView price = findViewById(R.id.price_buy);
        TextView amount = findViewById(R.id.amount_buy);
        TextView title = findViewById(R.id.gametitle_buy);

        name.setText(Pname);
        title.setText(Pname);

        String currentLanguage = Locale.getDefault().getLanguage();
        if(currentLanguage.equals("th")){
            price.setText(String.format("฿%.2f", Pprice * 34.53));
        }else{
            price.setText(String.format("$%.2f", Pprice));
        }

        amount.setText("In stock: " + Pamount);

        ImageView gamecover = findViewById(R.id.cover_buy);
        gamecover.setImageBitmap(scaledBitmap);

        final MaterialButton BUY = findViewById(R.id.btnbuynow_buy);

        BUY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BuyProductActivity.this)
                        .setTitle(R.string.noti_title_but)
                        .setMessage(R.string.noti_buy_product)
                        .setPositiveButton(R.string.noti_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BuyProduct();
                                showSuccessDialog();
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


        /*BUY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyProduct();
            }
        });*/


        final ImageView BACKBTN = findViewById(R.id.back_btn_buy);
        BACKBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BuyProductActivity.this)
                        .setTitle(R.string.noti_merchant_exit)
                        .setMessage(R.string.noti_merchant_exit_page)
                        .setPositiveButton(R.string.noti_merchant_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.noti_merchant_close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        ImageView menu_btn = findViewById(R.id.menu_btn_buy);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(BuyProductActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_profile){
                            Intent intent = new Intent(BuyProductActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            return true;
                        } else if (id == R.id.menu_logout){
                            LogoutUtils.logout(BuyProductActivity.this, sharedPreferences);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


    }

    private void BuyProduct() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_BUYITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("Pname", Pname);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    private void showSuccessDialog() {
        String randomHexCode = generateRandomHex(20);
        new AlertDialog.Builder(BuyProductActivity.this)
                .setTitle(R.string.success)
                .setMessage(getString(R.string.yourcodegane) + randomHexCode)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }


    private String generateRandomHex(int length) {
        Random random = new Random();
        StringBuilder hexString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int hexValue = random.nextInt(16);
            char hexChar = Integer.toHexString(hexValue).charAt(0);
            if (random.nextBoolean()) {
                hexChar = Character.toUpperCase(hexChar);
            }
            hexString.append(hexChar);
        }
        return hexString.toString();
    }
}