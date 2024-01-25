package com.example.shopapp.ui.Users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopapp.R;


public class HomeActivity extends AppCompatActivity {
    private Button logOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logOutBtn = (Button) findViewById(R.id.button);

        logOutBtn.setOnClickListener(view -> {

            Intent logoutIntent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(logoutIntent);
        });
    }
}