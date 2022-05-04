package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmaster.models.task;
import com.example.taskmaster.ui.viewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<task> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addTask=findViewById(R.id.addTask);
        Button allTask=findViewById(R.id.allTask);
        Button setting=findViewById(R.id.setting);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        initialiseData();

        viewAdapter customRecyclerViewAdapter = new viewAdapter(
                dataList, position -> {
            Intent taskDetailActivity=new Intent(this,taskDetail.class);
            taskDetailActivity.putExtra("lab_title",dataList.get(position).getTitle());
            taskDetailActivity.putExtra("lab_body",dataList.get(position).getBody());

            startActivity(taskDetailActivity);

        });

        recyclerView.setAdapter(customRecyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
    private void initialiseData() {
        dataList.add(new task("task 26","start create taskmaster"));
        dataList.add(new task("task 27","add some funtionality "));
        dataList.add(new task("task 28", "this task for recycle view"));

    }
}