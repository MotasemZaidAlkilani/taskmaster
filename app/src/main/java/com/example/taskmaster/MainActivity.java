package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addTask=findViewById(R.id.addTask);
        Button allTask=findViewById(R.id.allTask);
        Button setting=findViewById(R.id.setting);





        addTask.setOnClickListener(View -> {
            Intent toAddTaskActivity=new Intent(this,addTaskPage.class);
            startActivity(toAddTaskActivity);
            });

        allTask.setOnClickListener(View -> {
            Intent allTasksActivity=new Intent(this,allTasks.class);

           startActivity(allTasksActivity);

        });
        setting.setOnClickListener(View ->{
            Intent settingActivity=new Intent(this,setting.class);
            startActivity(settingActivity);
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        readUsernameAndShowIt();
    }
    public void taskDetail(View v){
        Intent taskDetailActivity=new Intent(this,taskDetail.class);
        Button button=findViewById(v.getId());
        taskDetailActivity.putExtra("lab",button.getText());
        startActivity(taskDetailActivity);
    }

    public void readUsernameAndShowIt(){
        TextView usernameTextView=findViewById(R.id.usernameTitle);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPref.getString(getString(R.string.username), "");
        usernameTextView.setText(username+"’s tasks");


    }
}