package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class taskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        TextView labTextView=findViewById(R.id.lab);
        TextView descriptionTextView=findViewById(R.id.describtion);
        Intent fromHome=getIntent();
        String labText=fromHome.getStringExtra("lab");
        labTextView.setText(labText);
        String loremText = getResources().getString(R.string.lorem);
        descriptionTextView.setText(loremText);

    }
}