package com.myproject.project_279;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // 🔹 Match XML IDs
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        btnLogIn = findViewById(R.id.btnLogIn);

        TextView signUpText = findViewById(R.id.signUpText);

        String fullText = "Don't have an Account? Sign up";
        SpannableString spannableString = new SpannableString(fullText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        };

        ForegroundColorSpan signUpColor = new ForegroundColorSpan(Color.RED);

        int startIndex = fullText.indexOf("Sign up");
        int endIndex = startIndex + "Sign up".length();

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(signUpColor, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signUpText.setText(spannableString);
        signUpText.setMovementMethod(LinkMovementMethod.getInstance());

        // 🔹 Login button now actually calls backend
        btnLogIn.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                attemptLogin(email, password);
            }
        });
    }

    private void attemptLogin(String email, String password) {
        // This calls login.php through Retrofit
        Call<LoginResponse> call = ApiClient.retrofitService.login(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse lr = response.body();
                    if (lr.isSuccess()) {
                        // Save user info for later (favorites/cart)
                        getSharedPreferences("user_prefs", MODE_PRIVATE)
                                .edit()
                                .putInt("user_id", lr.getUser_id())
                                .putString("user_email", lr.getEmail())
                                .putString("username", lr.getUsername())
                                .apply();

                        Toast.makeText(SignInActivity.this,
                                "Welcome " + lr.getUsername(),
                                Toast.LENGTH_SHORT).show();

                        // Go to main page
                        Intent intent = new Intent(SignInActivity.this, MainPageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this,
                                lr.getMessage() != null ? lr.getMessage() : "Login failed",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignInActivity.this,
                            "Error: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(SignInActivity.this,
                        "Failed: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
