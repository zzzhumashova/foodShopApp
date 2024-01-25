package com.example.shopapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shopapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private EditText usernameInput, registerPhoneInput, registerPasswordInput;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AppCompatButton registerButton = (AppCompatButton) findViewById(R.id.btnRegister);
        usernameInput = (EditText) findViewById(R.id.register_username_input);
        registerPhoneInput = (EditText) findViewById(R.id.register_phone_input);
        registerPasswordInput = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateAccount();
                }
            });
    }
        private void CreateAccount(){
            String username = usernameInput.getText().toString();
            String phone = registerPhoneInput.getText().toString();
            String password = registerPasswordInput.getText().toString();

            if(TextUtils.isEmpty(username))
            {
                Toast.makeText(this, "Введите ФИО", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
            }
            else{
                loadingBar.setTitle("Создание аккаунта");
                loadingBar.setMessage("Пожалуйста, подождите...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                ValidatePhone(username, phone, password);
            }
        }

        private void ValidatePhone(final String username,final String phone,final String password){
            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                    if(!(dataSnapshot.child("Users").child(phone).exists()))
                    {
                        HashMap<String, Object> userDataMap = new HashMap<>();
                        userDataMap.put("phone", phone);
                        userDataMap.put("name", username);
                        userDataMap.put("password", password);

                        RootRef.child("Users").child(phone).updateChildren(userDataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>(){
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task){
                                        if(task.isSuccessful()){
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this,"Зегистрация прошла успешно!",Toast.LENGTH_SHORT).show();

                                            Intent registorButtonIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(registorButtonIntent);
                                        }
                                        else {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                });
                    }
                    else{
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Номер"+phone+"уже зарегестрирован.", Toast.LENGTH_SHORT).show();

                        Intent registerButtonIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(registerButtonIntent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError){

                }
            });
        }
    }
