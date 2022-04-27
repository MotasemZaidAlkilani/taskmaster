package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
Button submit=findViewById(R.id.submitBtnId);
EditText usernameEditText=findViewById(R.id.username);
        submit.setOnClickListener(View ->{
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(getString(R.string.username), usernameEditText.getText().toString());
    editor.apply();
    Toast.makeText(this,"username saved",Toast.LENGTH_LONG).show();
            Intent backToHome=new Intent(this,MainActivity.class);
            startActivity(backToHome);
});
    }
}