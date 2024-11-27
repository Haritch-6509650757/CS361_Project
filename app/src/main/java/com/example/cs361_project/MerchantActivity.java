package com.example.cs361_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MerchantActivity extends AppCompatActivity {
    private RecyclerView rvMerchant; // แสดงรายการเกม
    private MerchantAdapter merchantAdapter; // จีดการข้อมูลใน Recycler Viewer
    private List<Merchant> merchantList; // แสดงรายการเกมทั้งหมด

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.merchant_page); //กำหนดหน้า layout home
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.merchantmain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            initializeViews();
            setupRecycleView();
            loadMerchantData();
        } catch (Exception e){
            e.printStackTrace();
            Log.e("MainActivity", "Error initializing: " + e.getMessage());
        }

        final ImageView BACKBTN = findViewById(R.id.back_btn);
        BACKBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // method สำหรับโหลดข้อมูลเกม
    private void loadMerchantData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.URL_GET_MERCHANT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // ลบ comment และ character พิเศษออก
                    String cleanResponse = response;
                    if (response.contains("/*")) {
                        cleanResponse = response.substring(response.indexOf("["), response.lastIndexOf("]") + 1);
                    }

                    // Log response
                    Log.d("API_DEBUG", "Original response: " + response);
                    Log.d("API_DEBUG", "Cleaned response: " + cleanResponse);

                    JSONArray jsonArray = new JSONArray(cleanResponse);
                    Log.d("API_DEBUG", "Successfully parsed JSON Array with " + jsonArray.length() + " items");

                    merchantList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject merchantObject = jsonArray.getJSONObject(i);

                        // Log ข้อมูลของแต่ละเกม
                        Log.d("API_DEBUG", "Processing game " + (i + 1));

                        int id = Integer.parseInt(merchantObject.optString("Mid", ""));
                        String item = merchantObject.optString("Mitem", "");

                        // แปลง String เป็น double และ int
                        double price = Double.parseDouble(merchantObject.optString("Mprice", "0"));
                        int amount = Integer.parseInt(merchantObject.optString("Mamount", "0"));

                        // จัดการกับรูปภาพ
                        String imageUrl = merchantObject.optString("Mimage", "");

                        // สร้าง Game object และเพิ่มเข้า list
                        Merchant merchant = new Merchant(id, item, price, amount, imageUrl);
                        merchantList.add(merchant);
                        Log.d("API_DEBUG", "Successfully added game: " + item);
                    }

                    if (!merchantList.isEmpty()) {
                        merchantAdapter = new MerchantAdapter(MerchantActivity.this, merchantList);
                        rvMerchant.setAdapter(merchantAdapter);
                        Log.d("API_DEBUG", "Successfully set adapter with " + merchantList.size() + " games");
                    } else {
                        Toast.makeText(MerchantActivity.this, "No games found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("API_DEBUG", "Error parsing data: " + e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(MerchantActivity.this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API_DEBUG", "Network Error: " + error.toString());
                if (error.networkResponse != null) {
                    String errorMessage = new String(error.networkResponse.data);
                    Log.e("API_DEBUG", "Error Data: " + errorMessage);
                }
                Toast.makeText(MerchantActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        // เพิ่ม timeout และ retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000, // 15 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(stringRequest);
    }

    // ตั้งค่า recycle viewer
    private void setupRecycleView() {
        // กำหนดเป็นตา่ราง 3 คอลลัม
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvMerchant.setLayoutManager(layoutManager);

        // กำหนดระยะห่างระหว่างเกมที่โชว์
        int spacinginPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        rvMerchant.addItemDecoration(new GridSpacingItemDecoration(3, spacinginPixels, true));

        rvMerchant.setHasFixedSize(true); // ถ้าขนาดไม่เปลี่ยนกำหนดเปลี่ยนอัตโนมัติ
    }

    // เชื่อมกับ product ใน layout
    private void initializeViews() {
        try {
            rvMerchant = findViewById(R.id.Mproducts);
            if(rvMerchant == null){
                Log.e("MerchantActivity", "RecyclerView not found");
            }else {
                Log.d("MerchantActivity", "RecyclerViewer initialized successfully");
            }
        } catch (Exception e){
            Log.e("MerchantActivity", "Error initializing views: " + e.getMessage());
        }

    }

    // จัดการะยะห่างระหว่าง item ใน grid
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{
        private int spanCount; // จำนวนคอลลัมน์
        private int spacing; // ระยะห่าง
        private boolean includeEdge; // รวมขอบ

        public GridSpacingItemDecoration(int spancount, int spacing, boolean includeEdge){
            this.spanCount = spancount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        // คำนวณระยะห่างแต่ละ item
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state){
            // คำนวณรตำแหน่งแต่ละคอลลัมน์
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            // กำหนดระยะห่าง
            if(includeEdge){ // รวมขอบ
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if(position < spanCount){
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            }else { // ไม่รวมขอบ
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if(position >= spanCount){
                    outRect.top = spacing;
                }
            }
        }
    }
}
