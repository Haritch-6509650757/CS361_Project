package com.example.cs361_project;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

public class MerchantAdapter extends RecyclerView.Adapter<MerchantAdapter.MerchantViewHolder> {
    private List<Merchant> merchant; // รายการเกม
    private Context context; // context สำหรับเข้าถึง resource

    public MerchantAdapter(Context context, List<Merchant> merchant) {
        this.context = context;
        this.merchant = merchant;
    }

    // Viewholder สำหรับเก็บ reference ของ views
    public static class MerchantViewHolder extends RecyclerView.ViewHolder{
        ImageView Mcover; // รูปปก
        TextView Mitem; // ชื่อเกม
        TextView Mprice; // ราคา
        TextView Mamount; // จำนวน
        MaterialButton Mbtnbuynow; // ปุ่มชื้อ

        // ผููก views กับแต่ละ id ของ layout
        public MerchantViewHolder(@NonNull View itemView){
            super(itemView);
            Mcover = itemView.findViewById(R.id.Mcover);
            Mitem = itemView.findViewById(R.id.Mname);
            Mprice = itemView.findViewById(R.id.Mprice);
            Mamount = itemView.findViewById(R.id.Mamount);
            Mbtnbuynow = itemView.findViewById(R.id.Mbtnbuynow);
        }
    }

    @NonNull
    @Override
    public MerchantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_product, parent, false);
        return new MerchantViewHolder(view);
    }

    // กำหนดช้อมูลใน viewholder
    @Override
    public void onBindViewHolder(@NonNull MerchantViewHolder holder, int position){
        Merchant merchant1 = merchant.get(position);
        double price = merchant1.getMprice();
        int amount = merchant1.getMamount();

        // กำหนดข้อมูล views ที่จะแสดงผล
        holder.Mitem.setText(merchant1.getMitem());
        holder.Mprice.setText(String.format("$%.2f", price));
        holder.Mamount.setText("Instock: " + amount);

        String cover = "http://" + Api.IPV4 + ":8080/api/" + merchant1.getMimage();
        Glide.with(holder.Mcover.getContext())
                .load(cover)  // หรือ Path ที่ถูกต้อง
                .into(holder.Mcover);
        Log.d("MerchantAdapter", "Image Path: " + merchant1.getMimage());


        holder.Mbtnbuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Buying " + merchant1.getMitem(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ส่งกลับจำนวน item ทั้งหมด
    @Override
    public int getItemCount(){
        return merchant.size();
    }
}
