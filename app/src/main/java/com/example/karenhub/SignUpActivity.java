package com.example.karenhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.karenhub.model.Model;
import com.google.android.material.textfield.TextInputEditText;


public class SignUpActivity extends AppCompatActivity {
    TextInputEditText editTextemail,editTextpassword, editTextaccLabel;
    Button SignUpBtn;
    Intent i;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editTextemail=findViewById(R.id.email);
        editTextpassword=findViewById(R.id.password);
        editTextaccLabel=findViewById(R.id.AccLabel);

        editTextpassword.setTransformationMethod(new PasswordTransformationMethod());
        SignUpBtn =findViewById(R.id.signUpBtn);
        sp = getSharedPreferences("SignUp",MODE_PRIVATE);


        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password,email;
                email=String.valueOf(editTextemail.getText());
                password=String.valueOf(editTextpassword.getText());

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getBaseContext(),"missing email or password",Toast.LENGTH_LONG).show();
                    
                    return;
                }
                else{
                    Model.instance().signUp(email,password,isok->{
                        if(isok){
                            // Sign up success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "user has been authenticated", Toast.LENGTH_SHORT).show();
                            i = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(i);
                            return;
                        }
                        else{ Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();}
                    });

                }

            }
        });

    }
}