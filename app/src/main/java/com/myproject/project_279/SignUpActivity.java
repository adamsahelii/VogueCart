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

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.email);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        TextView signInText = findViewById(R.id.signInText);

        btnCreateAccount.setOnClickListener(v -> {
            String emailText = emailEditText.getText().toString().trim();
            String phoneText = phoneNumberEditText.getText().toString().trim();
            String passwordText = passwordEditText.getText().toString().trim();
            String confirmPasswordText = confirmPasswordEditText.getText().toString().trim();

            if (emailText.isEmpty() || phoneText.isEmpty() ||
                    passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use phone as username (or you can change this)
            String username = phoneText;
            performRegister(emailText, passwordText, username);
        });

        String fullText = "Already have an Account? Sign in";
        SpannableString spannableString = new SpannableString(fullText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        };

        ForegroundColorSpan signInColor = new ForegroundColorSpan(Color.rgb(59, 150, 220));

        int startIndex = fullText.indexOf("Sign in");
        int endIndex = startIndex + "Sign in".length();

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(signInColor, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signInText.setText(spannableString);
        signInText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void performRegister(String email, String password, String username) {
        Call<LoginResponse> call = ApiClient.retrofitService.register(email, password, username);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse lr = response.body();
                    if (lr.isSuccess()) {
                        Toast.makeText(SignUpActivity.this,
                                "Account created! Please sign in.",
                                Toast.LENGTH_SHORT).show();

                        // Optionally auto-save user_id if backend returned it
                        if (lr.getUser_id() != 0) {
                            getSharedPreferences("user_prefs", MODE_PRIVATE)
                                    .edit()
                                    .putInt("user_id", lr.getUser_id())
                                    .apply();
                        }

                        // Go to sign in page
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this,
                                lr.getMessage() != null ? lr.getMessage() : "Registration failed",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "Error: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,
                        "Failed: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
