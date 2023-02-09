package com.example.karenhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karenhub.model.Model;
import com.google.android.material.textfield.TextInputEditText;


public class SignUpActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword, editTextAccLabel;
    TextView errorTV;
    TextView toLogIn;
    ImageView loaderIV;
    Button signUpBtn;
    Intent i;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        toLogIn = findViewById(R.id.signup_to_login_tv);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextPassword.setTransformationMethod(new PasswordTransformationMethod());

        editTextAccLabel = findViewById(R.id.AccLabel);
        LabelChecker();

        errorTV = findViewById(R.id.signup_error);
        loaderIV = findViewById(R.id.loading_spinner);
        loaderIV.setVisibility(View.GONE);

        signUpBtn = findViewById(R.id.signUpBtn);
        SignUpListener();

        toLogIn.setOnClickListener((toLogIn)->{backToLogIn();});
    }

    private void SignUpListener() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorTV.setText("");

                String password, email, label;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                label = String.valueOf(editTextAccLabel.getText());

                if (email.isEmpty() || password.isEmpty() || label.isEmpty()) {
                    Toast.makeText(getBaseContext(), "missing either an email, label, or password.", Toast.LENGTH_LONG).show();
                } else {
                    signUpBtn.setClickable(false);
                    // Load in UI Thread
                    loaderIV.post(() -> {
                        loaderIV.setVisibility(View.VISIBLE);
                        RotateAnimation animation = new RotateAnimation(360.0f, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setInterpolator(new LinearInterpolator());
                        animation.setDuration(1000);
                        animation.setRepeatCount(Animation.INFINITE);
                        loaderIV.startAnimation(animation);
                    });
                    Model.instance().signUp(email, label, password, (result) -> {
                        if (result.first) {
                            // Sign up success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, result.second, Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("email", email);
                            editor.putString("label", label);
                            editor.putString("password", password);
                            editor.apply();

                            i = new Intent(getApplicationContext(), MainActivity.class);
                            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                                            getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out)
                                    .toBundle();
                            startActivity(i, bundle);
                            finish();
                        } else {
                            errorTV.setText(result.second);


                        }
                        loaderIV.post(() -> {
                            loaderIV.clearAnimation();
                            loaderIV.setVisibility(View.GONE);
                        });
                        signUpBtn.setClickable(true);
                    });

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToLogIn();
    }

    public void backToLogIn(){
        i = new Intent(getApplicationContext(), LogInActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();
        startActivity(i, bundle);
        finish();
    }

    private void LabelChecker() {
        editTextAccLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
                String keyRegex = "[a-zA-Z0-9]";
                if (s.toString().contains(" ")) {
                    s = s.toString().replace(" ", "");
                    editTextAccLabel.setText(s);
                    editTextAccLabel.setSelection(s.length());
                }
                if (s.length() != 0 && !String.valueOf(s.charAt(s.length() - 1)).matches(keyRegex) && before != 1) {
                    return;
                }
                if (s.length() != 0 && !s.toString().startsWith("@")) {
                    if (s.toString().split("@").length > 1) {
                        int atIndex = s.toString().lastIndexOf("@");
                        String PrevS = String.valueOf(s.subSequence(atIndex, s.length()));
                        editTextAccLabel.setText(PrevS);
                        editTextAccLabel.setSelection(1);
                    } else {
                        s = "@" + s;
                        editTextAccLabel.setText(s);
                        editTextAccLabel.setSelection(s.length());
                    }
                }
                if (s.toString().equals("@")) {
                    s = "";
                    editTextAccLabel.setText(s);
                    editTextAccLabel.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}