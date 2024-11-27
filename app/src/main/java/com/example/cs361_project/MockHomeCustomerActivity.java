package com.example.cs361_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MockHomeCustomerActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mock_home_customer_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);


        final TextView usernamemock = findViewById(R.id.mockusername);
        final TextView apimock = findViewById(R.id.mockapikey);

        usernamemock.setText(sharedPreferences.getString("username", "Guest"));
        apimock.setText(sharedPreferences.getString("apiKey", "eiei"));


        final TextView gotoadditem = findViewById(R.id.gotoadditem);
        gotoadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MockHomeCustomerActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
        final TextView gotoprofile = findViewById(R.id.gotoprofile);
        gotoprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MockHomeCustomerActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });



    } //end OnCreate
}
