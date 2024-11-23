package com.example.cs361_project;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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

        // เช็คถ้าเกิด error
        try {
            initializeViews();
            setupRecycleView();
            loadGameData();
        } catch (Exception e){
            e.printStackTrace();
            Log.e("MainActivity", "Error initializing: " + e.getMessage());
        }
    }

    // method สำหรับโหลดข้อมูลเกม
    private void loadGameData() {
        gameList = new ArrayList<>();

        gameList.add(new Game("1", "test game","",20,20));
        gameList.add(new Game("2", "Elden ring", "", 30, 100));

        gameAdapter = new GameAdapter(this, gameList);
        rvGames.setAdapter(gameAdapter);
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
}