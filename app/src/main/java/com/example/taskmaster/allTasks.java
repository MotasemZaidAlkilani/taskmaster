package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.example.taskmaster.ui.viewAdapter;

import java.util.ArrayList;
import java.util.List;

public class allTasks extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_allTask);

        recyclerView.setAdapter(customRecyclerViewAdapter());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public List<Task> getAllItems() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_allTask);

        List<Task>datalist = new ArrayList();
        Amplify.DataStore.query(Task.class,
                (tasks )-> runOnUiThread(() ->{
                    while(tasks.hasNext()){
                        Task task=tasks.next();
                        Log.e("task",task.getTitle());
                        datalist.add(task);
                    }
                    viewAdapter customRecyclerViewAdapter = new viewAdapter(
                            datalist, position -> {
                        Task task = datalist.get(position);
                        Intent taskDetailActivity=new Intent(this,taskDetail.class);

                        taskDetailActivity.putExtra("lab_title",task.getTitle());
                        taskDetailActivity.putExtra("lab_body",task.getDescription());
                        taskDetailActivity.putExtra("lab_status",task.getStatus());
                        taskDetailActivity.putExtra("lab_location",task.getLocation());
                        startActivity(taskDetailActivity);

                    });
                    recyclerView.setAdapter(customRecyclerViewAdapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                }), error -> {

                }
        );
        return datalist;
    }
    public viewAdapter customRecyclerViewAdapter(){
        viewAdapter customRecyclerViewAdapter = new viewAdapter(
                getAllItems(), position -> {
            Task task = getAllItems().get(position);
            Intent taskDetailActivity=new Intent(this,taskDetail.class);

            taskDetailActivity.putExtra("lab_title",task.getTitle());
            taskDetailActivity.putExtra("lab_body",task.getDescription());
            taskDetailActivity.putExtra("lab_status",task.getStatus());
            taskDetailActivity.putExtra("lab_location",task.getLocation());

            startActivity(taskDetailActivity);

        });
        return customRecyclerViewAdapter;
    }

}