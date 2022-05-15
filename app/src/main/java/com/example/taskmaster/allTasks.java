package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.taskmaster.models.task;
import com.example.taskmaster.ui.AppDatabase;
import com.example.taskmaster.ui.viewAdapter;

import java.util.List;

public class allTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        displayRecycleViewItemsAndGoToTaskDetailPageWhenClick();
    }
    public void displayRecycleViewItemsAndGoToTaskDetailPageWhenClick(){
        List<task> dataList = AppDatabase.getInstance(this).TaskDao().getAllByState("COMPLETE");
        viewAdapter customRecyclerViewAdapter = new viewAdapter(
                dataList, position -> {
            task task = AppDatabase.getInstance(getApplicationContext()).TaskDao().getTaskById(dataList.get(position).getId());

            Intent taskDetailActivity=new Intent(this,taskDetail.class);

            taskDetailActivity.putExtra("lab_id",task.getId());
            taskDetailActivity.putExtra("lab_title",task.getTitle());
            taskDetailActivity.putExtra("lab_body",task.getBody());
            taskDetailActivity.putExtra("lab_status",task.getState());
            startActivity(taskDetailActivity);
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view_allTask);
        recyclerView.setAdapter(customRecyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}