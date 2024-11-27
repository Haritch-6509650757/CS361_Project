package com.example.cs361_project;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BuyMerchantActivity extends AppCompatActivity {
    String Mitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.buy_merchant); //กำหนดหน้า layout home
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.buy_merchant), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Mitem = getIntent().getStringExtra("merchant_item");
        double Mprice = getIntent().getDoubleExtra("merchant_price", 0.0);
        int Mamount = getIntent().getIntExtra("merchant_amount", 0);
        String Mcover = getIntent().getStringExtra("merchant_cover");

        TextView name = findViewById(R.id.name_merchant);
        TextView price = findViewById(R.id.price_merchant);
        TextView amount = findViewById(R.id.amount_merchant);
        TextView title = findViewById(R.id.gametitle_merchant);
        ImageView cover = findViewById(R.id.cover_merchant);

        name.setText(Mitem);
        title.setText(Mitem);

        String currentLanguage = Locale.getDefault().getLanguage();
        if(currentLanguage.equals("th")){
            price.setText(String.format("฿%.2f", Mprice * 34.53));
        }else{
            price.setText(String.format("$%.2f", Mprice));
        }

        amount.setText("In stock: " + Mamount);

        Glide.with(this)
                .load(Mcover)
                .override(600, 900)
                .into(cover);

        //ImageView merchantcover = findViewById(R.id.cover_merchant);
        //merchantcover.setImageResource(Mcover);

        final MaterialButton BUY = findViewById(R.id.btnbuynow_merchant);
        BUY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyProduct();
            }
        });

        final ImageView BACKBTN = findViewById(R.id.back_btn_merchant);
        BACKBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BuyMerchantActivity.this)
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
    }
    private void BuyProduct() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_BUYMERCHANT,
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
                paramV.put("Mitem", Mitem);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}
