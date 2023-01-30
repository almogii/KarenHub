package com.example.karenhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.karenhub.model.Model;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class LogInActivity extends AppCompatActivity {
    TextInputEditText LogIn_email,LogIn_password;
    Button LogIn_btn;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        LogIn_email=findViewById(R.id.logInEmail);
        LogIn_password=findViewById(R.id.logInPassword);
        LogIn_btn=findViewById(R.id.login_btn1);
        LogIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String email,password;
                email = String.valueOf(LogIn_email.getText());
                password= String.valueOf(LogIn_password.getText());
                if(email.isEmpty()||password.isEmpty()){
                    Toast.makeText(getBaseContext(),"the password or email you insert is not valid",Toast.LENGTH_LONG).show();
                }
                else{
                    Model.instance().login(email,password,isValid->{
                        if (!isValid) {
                            Toast.makeText(LogInActivity.this, "user not exist", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(isValid){
                            Toast.makeText(LogInActivity.this, "user has been logged in", Toast.LENGTH_SHORT).show();
                            i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            return;
                        }
                    });
                }
            }
        });
    }


}