package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.AuthDevice;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.example.taskmaster.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressBar loadingProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        TextView signUpPrompt=findViewById(R.id.sign_up_prompt);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        signUpPrompt.setOnClickListener(View  ->{
            Intent navigateToSignUpIntent=new Intent(this,SignUpActivity.class);
            startActivity(navigateToSignUpIntent);
        });


        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                loginButton.setEnabled(true);
                return true;
            }
        });

        loginButton.setOnClickListener(view -> {

                loadingProgressBar.setVisibility(View.VISIBLE);
                login(usernameEditText.getText().toString(), passwordEditText.getText().toString());

            });

    }

    private void login(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    Log.i(TAG, result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                },
                error -> Log.e(TAG, error.toString())
        );
    }

}