package com.example.shopapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shopapp.Model.Users;
import com.example.shopapp.R;
import com.example.shopapp.ui.Admin.AdminCategoryActivity;
import com.example.shopapp.ui.Users.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;


public class LoginActivity extends AppCompatActivity {
    private EditText loginPhoneInput, loginPasswordInput;
    private ProgressDialog loadingBar;

    private String parentDbName="Users";
    private TextView AdminLink, NotAdminLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatButton loginButton = findViewById(R.id.btnLogin);
        loginPhoneInput = findViewById(R.id.login_phone_input);
        loginPasswordInput = findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);

        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);

        AdminLink.setOnClickListener(view -> {
            AdminLink.setVisibility(View.INVISIBLE);
            NotAdminLink.setVisibility(View.VISIBLE);
            loginButton.setText("Вход для Админа");
            parentDbName="Admins";
        });

        NotAdminLink.setOnClickListener(view -> {
            AdminLink.setVisibility(View.VISIBLE);
            NotAdminLink.setVisibility(View.INVISIBLE);
            loginButton.setText("Войти");
            parentDbName="Users";

        });

        loginButton.setOnClickListener(view -> loginUser());
    }

    private void loginUser(){
        String phone = loginPhoneInput.getText().toString();
        String password = loginPasswordInput.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Вход в аккаунт");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateUser(phone, password);
        }
    }

    private void ValidateUser(final String phone, String password){

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    assert usersData != null;
                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Users")){
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                            }
                            else if(parentDbName.equals("Admins")){
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                                Intent homeIntent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(homeIntent);
                            }
                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Неверный пароль...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Аккаунт с номером "+phone+" не существует", Toast.LENGTH_SHORT).show();

                    Intent loginButtonIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(loginButtonIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });

    }
}