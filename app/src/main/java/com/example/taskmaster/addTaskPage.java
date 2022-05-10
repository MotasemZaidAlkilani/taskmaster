package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmaster.models.task;
import com.example.taskmaster.ui.AppDatabase;

public class addTaskPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_page);
        Button addTask=findViewById(R.id.addTaskBtn);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText task_name_input=findViewById(R.id.task_name);
                EditText describtion_input=findViewById(R.id.describtion);

                String taskName=task_name_input.getText().toString();
                String describtion=describtion_input.getText().toString();
                task task=new task(taskName,describtion);
                Long newTaskId = AppDatabase.getInstance(getApplicationContext()).TaskDao().insertTask(task);

                Toast.makeText(addTaskPage.this,"INSERTED!",Toast.LENGTH_LONG).show();
            }
        });
    }
}