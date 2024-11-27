package com.example.cs361_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64; // ใข้แปลงรูป

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

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private RecyclerView rvGames; // แสดงรายการเกม
    private GameAdapter gameAdapter; // จีดการข้อมูลใน Recycler Viewer
    private List<Game> gameList; // แสดงรายการเกมทั้งหมด

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home); //กำหนดหน้า layout home
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if(!sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(GameActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // เช็คถ้าเกิด error
        try {
            initializeViews();
            setupRecycleView();
            loadGameData();
        } catch (Exception e){
            e.printStackTrace();
            Log.e("MainActivity", "Error initializing: " + e.getMessage());
        }

        final TextView PC = findViewById(R.id.pc);
        PC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterGamesByCategory("PC");
            }
        });

        final TextView PSN = findViewById(R.id.psn);
        PSN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterGamesByCategory("PSN");
            }
        });

        final TextView NINTEDNO = findViewById(R.id.nintendo);
        NINTEDNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterGamesByCategory("NINTENDO");
            }
        });

        final TextView XBOX = findViewById(R.id.xbox);
        XBOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterGamesByCategory("XBOX");
            }
        });

        final TextView MERCHANT = findViewById(R.id.merchant);
        MERCHANT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sharedPreferences.getString("job","").equals("แม่ค้า") || sharedPreferences.getString("job","").equals("Seller")){
                    Intent intent = new Intent(GameActivity.this, MerchantSellerActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GameActivity.this, MerchantActivity.class);
                    startActivity(intent);
                }
            }
        });

        //setContentView(R.layout.home);
        final ImageView HOME = findViewById(R.id.home);
        HOME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGameData();
            }
        });
    }

    // method สำหรับโหลดข้อมูลเกม
    private void loadGameData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.URL_GET_PRODUCTS, new Response.Listener<String>() {
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

                    gameList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject gameObject = jsonArray.getJSONObject(i);

                        // Log ข้อมูลของแต่ละเกม
                        Log.d("API_DEBUG", "Processing game " + (i + 1));

                        String id = gameObject.optString("Pid", "");
                        String name = gameObject.optString("Pname", "");
                        String category = gameObject.optString("Pcategory", "");

                        // แปลง String เป็น double และ int
                        double price = Double.parseDouble(gameObject.optString("Pprice", "0"));
                        int amount = Integer.parseInt(gameObject.optString("Pamount", "0"));

                        // จัดการกับรูปภาพ
                        Bitmap imageBitmap;
                        String imageBase64 = gameObject.optString("Pimage", "");

                        if (!imageBase64.isEmpty()) {
                            try {
                                // ลบ whitespace หรือ newline ที่อาจมีใน Base64 string
                                imageBase64 = imageBase64.trim().replaceAll("\\s+", "");
                                                                  //รูปที่ต้องการแปลง decode แบบ default
                                byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                                              // แปลงข้อมูลรููปแบบ byte เพื่อให้สามารถนำไปใช้ใน layout ได้ เริ่มตำแหน้ง 0 และขนาดที่ต้องการ decode
                                imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                if (imageBitmap == null) {
                                    Log.e("API_DEBUG", "Failed to decode bitmap for game: " + name);
                                    imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loading_error);
                                }
                            } catch (Exception e) {
                                Log.e("API_DEBUG", "Error processing image for game " + name + ": " + e.getMessage());
                                imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loading_error);
                            }
                        } else {
                            Log.d("API_DEBUG", "No image data for game: " + name);
                            imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loading_error);
                        }

                        // สร้าง Game object และเพิ่มเข้า list
                        Game game = new Game(id, name, imageBitmap, price, amount, category);
                        gameList.add(game);
                        Log.d("API_DEBUG", "Successfully added game: " + name);
                    }

                    if (!gameList.isEmpty()) {
                        gameAdapter = new GameAdapter(GameActivity.this, gameList);
                        rvGames.setAdapter(gameAdapter);
                        Log.d("API_DEBUG", "Successfully set adapter with " + gameList.size() + " games");
                    } else {
                        Toast.makeText(GameActivity.this, "No games found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("API_DEBUG", "Error parsing data: " + e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(GameActivity.this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GameActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
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
        rvGames.setLayoutManager(layoutManager);

        // กำหนดระยะห่างระหว่างเกมที่โชว์
        int spacinginPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        rvGames.addItemDecoration(new GridSpacingItemDecoration(3, spacinginPixels, true));

        rvGames.setHasFixedSize(true); // ถ้าขนาดไม่เปลี่ยนกำหนดเปลี่ยนอัตโนมัติ
    }

    // เชื่อมกับ product ใน layout
    private void initializeViews() {
        try {
            rvGames = findViewById(R.id.products);
            if(rvGames == null){
                Log.e("MainActivity", "RecyclerView not found");
            }else {
                Log.d("MainActivity", "RecyclerViewer initialized successfully");
            }
        } catch (Exception e){
            Log.e("MainActivity", "Error initializing views: " + e.getMessage());
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

    private void filterGamesByCategory(String category){
        List<Game> filteredGames = new ArrayList<>();
        for(Game game : gameList){
            if(game.getPcategory().equalsIgnoreCase(category)){
                filteredGames.add(game);
            }
        }
        gameAdapter.updateGameList(filteredGames);
    }

}