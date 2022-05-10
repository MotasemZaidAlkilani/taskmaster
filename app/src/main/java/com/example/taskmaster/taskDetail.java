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
        TextView status=findViewById(R.id.status);

        Intent fromHome=getIntent();
        String lab_title=fromHome.getStringExtra("lab_title");
        String lab_body=fromHome.getStringExtra("lab_body");
        String lab_state=fromHome.getStringExtra("lab_status");

        labTextView.setText(lab_title);
        descriptionTextView.setText(lab_body);
        status.setText(lab_state);

    }
}