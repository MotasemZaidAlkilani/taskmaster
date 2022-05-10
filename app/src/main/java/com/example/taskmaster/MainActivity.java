package com.example.taskmaster;

import static com.example.taskmaster.ui.viewAdapter.dataList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmaster.models.task;
import com.example.taskmaster.models.taskState;
import com.example.taskmaster.ui.AppDatabase;
import com.example.taskmaster.ui.viewAdapter;

import java.sql.SQLOutput;
import java.util.List;

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
        displayRecycleViewItemsAndGoToTaskDetailPageWhenClick();
        readUsernameAndShowIt();
    }


    public void readUsernameAndShowIt(){
        TextView usernameTextView=findViewById(R.id.usernameTitle);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPref.getString(getString(R.string.username), "");
        usernameTextView.setText(username+"’s tasks");
    }
    public void displayRecycleViewItemsAndGoToTaskDetailPageWhenClick(){
        List<task> dataList = AppDatabase.getInstance(this).TaskDao().getAll();
        viewAdapter customRecyclerViewAdapter = new viewAdapter(
                dataList, position -> {
            task task = AppDatabase.getInstance(getApplicationContext()).TaskDao().getTaskById(dataList.get(position).getId());
            Intent taskDetailActivity=new Intent(this,taskDetail.class);
            taskDetailActivity.putExtra("lab_title",task.getTitle());
            taskDetailActivity.putExtra("lab_body",task.getBody());
            taskDetailActivity.putExtra("lab_status",task.getState());
            startActivity(taskDetailActivity);
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(customRecyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}