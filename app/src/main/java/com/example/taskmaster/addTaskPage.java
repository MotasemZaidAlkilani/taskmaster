package com.example.taskmaster;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;


public class addTaskPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_page);
        Button addTask = findViewById(R.id.addTaskBtn);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.teams_numbers, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("spinner",spinner.getSelectedItem().toString());
                if (spinner.getSelectedItem().toString().matches("team 1") ) {
                    saveTaskInTaskTableAndTeamTable("1");
                }
                 else if (spinner.getSelectedItem().toString().matches("team 2")) {
                    saveTaskInTaskTableAndTeamTable("2");
                } else if (spinner.getSelectedItem().toString().matches("team 3")) {
                    saveTaskInTaskTableAndTeamTable("3");
                }


            }
        });
    }
    public void saveTaskInTaskTableAndTeamTable(String id){

        EditText task_name_input = findViewById(R.id.task_name);
        EditText describtion_input = findViewById(R.id.describtion);

        String taskName = task_name_input.getText().toString();
        String describtion = describtion_input.getText().toString();






        Amplify.API.query(ModelQuery.list(Team.class,Team.ID.contains(id)), teams->{
            for(Team team:teams.getData()){
              
                if(team.getId().contains(id)){

                    Task newTask = Task.builder().
                            title(taskName).
                            description(describtion).
                            teamTasksId(id).
                            status("new").
                            build();

                    Amplify.DataStore.save(newTask,
                            success -> {
                                Log.e("success", "INSERTED SUCCESFULLY");
                            }, failed -> {
                                Log.e("faild", "FAILED TO INSERT");

                            }
                    );

                    Amplify.API.mutate(ModelMutation.create(newTask),

                            response -> Log.i("MyAmplifyApp", "Todo with id: "),
                            error -> Log.e("MyAmplifyApp", "Create failed", error)
                    );

                    break;
                }

            }
            },error->{
           Log.i("mutates team", "error");
                    });

    }

    }

