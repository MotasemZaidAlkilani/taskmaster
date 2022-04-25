package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addTask=findViewById(R.id.addTask);
        Button allTask=findViewById(R.id.allTask);
        Intent toAddTask=new Intent(this,addTaskPage.class);
        Intent allTasks=new Intent(this,allTasks.class);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toAddTask);
            }
        });
        allTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           startActivity(allTasks);
            }
        });
    }
}