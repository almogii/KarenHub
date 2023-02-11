package com.example.karenhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karenhub.model.Model;
import com.example.karenhub.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class LogInActivity extends AppCompatActivity {
    TextInputEditText LogIn_email, LogIn_password;
    TextView toSignUp;
    Button LogIn_btn;
    Intent i;
    ImageView loaderIV;
    TextView errorTV;
    FirebaseUser user;
    String label;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        LogIn_email = findViewById(R.id.logInEmail);
        LogIn_password = findViewById(R.id.logInPassword);
        LogIn_btn = findViewById(R.id.login_btn1);
        toSignUp = findViewById(R.id.login_to_signup_tv);
        user = Model.instance().getAuth().getCurrentUser();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        loaderIV=findViewById(R.id.loading_spinner);
        loaderIV.setVisibility(View.GONE);
        errorTV=findViewById(R.id.login_error);
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i = new Intent(getApplicationContext(), SignUpActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                                getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out)
                        .toBundle();
                startActivity(i, bundle);
                finish();
            }
        });
        LogIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorTV.setText("");
                String email, password;
                email = String.valueOf(LogIn_email.getText());
                password = String.valueOf(LogIn_password.getText());
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getBaseContext(), "the password or email you insert is not valid", Toast.LENGTH_LONG).show();
                } else {
                    LogIn_btn.setClickable(false);
                    loaderIV.post(() -> {
                        loaderIV.setVisibility(View.VISIBLE);
                        RotateAnimation animation = new RotateAnimation(360.0f, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setInterpolator(new LinearInterpolator());
                        animation.setDuration(1000);
                        animation.setRepeatCount(Animation.INFINITE);
                        loaderIV.startAnimation(animation);
                    });
                    Model.instance().login(email, password, result -> {

                        if (result.first) {
                            Model.instance().getDb().collection("users")
                                    .whereEqualTo("email",email).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                //Toast.makeText(LogInActivity.this, result.second, Toast.LENGTH_SHORT).show();
                                                errorTV.setText(result.second);
                                                QuerySnapshot querySnapshot = task.getResult();
                                                if(!querySnapshot.isEmpty()){
                                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                                    label = document.getString("label");
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
                                                }
                                            } else {
                                                errorTV.setText("An Error has occurred");
                                            }
                                        }
                                    });
                        }
                        else {
                            errorTV.setText(result.second);
                        }
                        loaderIV.post(() -> {
                            loaderIV.clearAnimation();
                            loaderIV.setVisibility(View.GONE);
                        });
                        LogIn_btn.setClickable(true);
                    });
                }
            }
        });
    }


}