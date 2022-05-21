package com.example.taskmaster;


import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;


public class addTaskPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_page);
        Button addTask = findViewById(R.id.addTaskBtn);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText task_name_input = findViewById(R.id.task_name);
                EditText describtion_input = findViewById(R.id.describtion);

                String taskName = task_name_input.getText().toString();
                String describtion = describtion_input.getText().toString();

                Task newTask = Task.builder().
                        title(taskName).
                        description(describtion).
                        status("new").
                        build();


                Amplify.DataStore.save(newTask,
                        success -> {
                            Log.e("success", "INSERTED SUCCESfullY");
                        }, failed -> {
                            Log.e("faild", "FAILED TO INSERT");

                        }
                );
          Amplify.API.mutate(ModelMutation.create(newTask),
    response -> Log.i("MyAmplifyApp", "Todo with id: "),
    error -> Log.e("MyAmplifyApp", "Create failed", error)
);
                Amplify.DataStore.observe(Task.class,
                        started ->{
                            Log.e(TAG,"observation began");
                        },change ->{
                            Log.e(TAG,change.item().toString());

                        },failure -> Log.e(TAG,"observation failed",failure),
                        () ->Log.i(TAG,"observation complete"));



                //                task task=new task(taskName,describtion);
//                Long newTaskId = AppDatabase.getInstance(getApplicationContext()).TaskDao().insertTask(task);
//                Toast.makeText(addTaskPage.this,"INSERTED!",Toast.LENGTH_LONG).show();


            }
        });
    }
}
