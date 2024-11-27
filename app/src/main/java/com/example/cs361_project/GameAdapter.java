package com.example.cs361_project;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

//import java.util.Base64;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

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

        String currentLanguage = Locale.getDefault().getLanguage();
        if(currentLanguage.equals("th")){
            holder.gameprice.setText(String.format("฿%.2f", price * 34.53));
        }else{
            holder.gameprice.setText(String.format("$%.2f", price));
        }
        //holder.gameprice.setText(String.format("$%.2f", price));
        holder.gameamount.setText("Instock: " + amount);

        holder.gamecover.setImageBitmap(game.getPimage());

        holder.btnbuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyProductActivity.class);

                intent.putExtra("game_name", game.getPname());
                intent.putExtra("game_price", price);
                intent.putExtra("game_amount", amount);

                Bitmap bitmap = game.getPimage();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                intent.putExtra("game_cover", byteArray);

                context.startActivity(intent);
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
