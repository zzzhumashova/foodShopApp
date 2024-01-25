package com.example.shopapp.ui.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopapp.R;

public class AdminPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        Toast.makeText(this, "Добро пожаловать, Админ!", Toast.LENGTH_SHORT).show();
    }
}