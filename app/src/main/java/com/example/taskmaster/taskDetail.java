package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmaster.ui.AppDatabase;

public class taskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        TextView labTextView=findViewById(R.id.lab);
        TextView descriptionTextView=findViewById(R.id.describtion);
        TextView status=findViewById(R.id.status);
        Button completeTask=findViewById(R.id.complete);
        Intent fromHome=getIntent();
        String lab_title=fromHome.getStringExtra("lab_title");
        String lab_body=fromHome.getStringExtra("lab_body");
        String lab_state=fromHome.getStringExtra("lab_status");
        int lab_id=fromHome.getIntExtra("lab_id",-1);
        labTextView.setText(lab_title);
        descriptionTextView.setText(lab_body);
        status.setText(lab_state);
      completeTask.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              AppDatabase.getInstance(getApplicationContext()).TaskDao().changeState("COMPLETE",lab_id);
              Intent backToHome=new Intent(taskDetail.this,MainActivity.class);
              startActivity(backToHome);
          }
      });
    }
}