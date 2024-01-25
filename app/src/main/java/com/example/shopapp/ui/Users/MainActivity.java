package com.example.shopapp.ui.Users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shopapp.ui.LoginActivity;
import com.example.shopapp.Model.Users;
import com.example.shopapp.R;
import com.example.shopapp.ui.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;


public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton btnStart = (AppCompatButton) findViewById(R.id.btnStart);
        AppCompatButton btnRegister = (AppCompatButton) findViewById(R.id.btnRegister);


        loadingBar = new ProgressDialog(this);
        progressDialog = new ProgressDialog(this);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent btnStartIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(btnStartIntent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent btnRegisterIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(btnRegisterIntent);
            }
        });


    }

    private void ValidateUser(String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                        }
                        else{
                            loadingBar.dismiss();
                        }
                    }
                }
                else{
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Аккаунт с номером "+phone+" не существует", Toast.LENGTH_SHORT).show();

                    Intent loginButtonIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(loginButtonIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });
    }
}