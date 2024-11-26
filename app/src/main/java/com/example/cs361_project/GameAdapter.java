package com.example.cs361_project;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

//import java.util.Base64;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private List<Game> games; // รายการเกม
    private Context context; // context สำหรับเข้าถึง resource

    public GameAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
    }

    // Viewholder สำหรับเก็บ reference ของ views
    public static class GameViewHolder extends RecyclerView.ViewHolder{
        ImageView gamecover; // รูปปก
        TextView gamename; // ชื่อเกม
        TextView gameprice; // ราคา
        TextView gameamount; // จำนวน
        MaterialButton btnbuynow; // ปุ่มชื้อ

        // ผููก views กับแต่ละ id ของ layout
        public GameViewHolder(@NonNull View itemView){
            super(itemView);
            gamecover = itemView.findViewById(R.id.gamecover);
            gamename = itemView.findViewById(R.id.gamename);
            gameprice = itemView.findViewById(R.id.gameprice);
            gameamount = itemView.findViewById(R.id.gameamount);
            btnbuynow = itemView.findViewById(R.id.btnbuynow);
        }
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_product, parent, false);
        return new GameViewHolder(view);
    }

    // กำหนดช้อมูลใน viewholder
    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position){
        Game game = games.get(position);
        double price = game.getPprice();
        int amount = game.getPamount();

        // กำหนดข้อมูล views ที่จะแสดงผล
        holder.gamename.setText(game.getPname());
        holder.gameprice.setText(String.format("$%.2f", price));
        holder.gameamount.setText("Instock: " + amount);

        holder.gamecover.setImageBitmap(game.getPimage());

        holder.btnbuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Buying " + game.getPname(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ส่งกลับจำนวน item ทั้งหมด
    @Override
    public int getItemCount(){
        return games.size();
    }

    // Filter เกมตาม Category
    public void updateGameList(List<Game> newGames){
        this.games = newGames;
        notifyDataSetChanged();
    }
}