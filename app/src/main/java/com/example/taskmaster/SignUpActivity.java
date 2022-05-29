package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUpActivity extends AppCompatActivity {
    public static final String EMAIL = "email";
    private static final String TAG=SignUpActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        final EditText usernameEditText = findViewById(R.id.username_sign_up);
        final EditText passwordEditText = findViewById(R.id.password_sign_Up);
        final Button signupButton = findViewById(R.id.sign_up);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

//        authSession();


        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    signupButton.setEnabled(true);

                return true;
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                signUp(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

            }
        });
    }



    private void signUp(String email,String password){
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();
        Amplify.Auth.signUp(email, password, options,
                result -> {
                Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
                    Log.i(TAG, "Result: " + result.toString());
                   finish();
        }
                ,
                error -> Log.e(TAG, "Sign up failed", error)
        );
    }
    private void authSession(){
        Amplify.Auth.fetchAuthSession(
                result-> Log.i(TAG,result.toString()),
                error -> Log.e(TAG,error.toString())
        );
    }
}