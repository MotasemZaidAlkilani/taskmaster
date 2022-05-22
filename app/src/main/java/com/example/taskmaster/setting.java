package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
Button submit=findViewById(R.id.submitBtnId);
EditText usernameEditText=findViewById(R.id.username);
        Button backBtn=findViewById(R.id.backBtn);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_setting);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.teams_numbers, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        backBtn.setOnClickListener(view ->{
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("recycleViewTeam", spinner.getSelectedItem().toString());
            editor.apply();
            Toast.makeText(this,"team changed",Toast.LENGTH_LONG).show();
            Intent backToHome=new Intent(this,MainActivity.class);
            startActivity(backToHome);
        });
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