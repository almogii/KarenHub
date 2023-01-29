package com.example.karenhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
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
        editTextaccLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
                String keyRegex = "[a-zA-Z0-9]";
                if(s.toString().contains(" ")){
                    s = s.toString().replace(" ","");
                    editTextaccLabel.setText(s);
                    editTextaccLabel.setSelection(s.length());
                }
                if(s.length() !=0 && !String.valueOf(s.charAt(s.length()-1)).matches(keyRegex) && before !=1){
                    return;
                }
                if(s.length() !=0 && !s.toString().startsWith("@")){
                    if(s.toString().split("@").length > 1 ){
                        int atIndex = s.toString().lastIndexOf("@");
                        String PrevS = String.valueOf(s.subSequence(atIndex, s.length()));
                        editTextaccLabel.setText(PrevS);
                        editTextaccLabel.setSelection(1);
                    } else {
                        s = "@" + s;
                        editTextaccLabel.setText(s);
                        editTextaccLabel.setSelection(s.length());
                    }
                }
                if(s.toString().equals("@")){
                    s = "";
                    editTextaccLabel.setText(s);
                    editTextaccLabel.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextpassword.setTransformationMethod(new PasswordTransformationMethod());
        SignUpBtn =findViewById(R.id.signUpBtn);
        sp = getSharedPreferences("SignUp",MODE_PRIVATE);


        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password,email,label;
                email=String.valueOf(editTextemail.getText());
                password=String.valueOf(editTextpassword.getText());
                label = String.valueOf(editTextaccLabel.getText());

                if (email.isEmpty() || password.isEmpty() || label.isEmpty()) {
                    Toast.makeText(getBaseContext(),"missing either an email, label, or password.",Toast.LENGTH_LONG).show();
                    
                    return;
                }
                else{
                    Model.instance().signUp(email,label,password,isok->{
                        if(isok){
                            // Sign up success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "user has been authenticated", Toast.LENGTH_SHORT).show();
                            i = new Intent(getApplicationContext(), MainActivity.class);
                            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                                            getApplicationContext(),android.R.anim.fade_in, android.R.anim.fade_out)
                                    .toBundle();
                            startActivity(i, bundle);
                            finish();
                            return;
                        }
                        else{ Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();}
                    });

                }

            }
        });

    }

}