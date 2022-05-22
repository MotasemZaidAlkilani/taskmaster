package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.ui.viewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addTask=findViewById(R.id.addTask);
        Button allTask=findViewById(R.id.allTask);
        Button setting=findViewById(R.id.setting);

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException e) {
            e.printStackTrace();
        }
        create3Teams();
        Amplify.DataStore.observe(Task.class,
                started ->{
                    Log.e(TAG,"observation began");
                },change ->{
                    Log.e(TAG,change.item().toString());

                },failure -> Log.e(TAG,"observation failed",failure),
                () ->Log.i(TAG,"observation complete"));



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
    public List<Task> getAllItems() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        List<Task>datalist = new ArrayList();
        Amplify.DataStore.query(Task.class,
                (tasks )-> runOnUiThread(() ->{
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    String Team = sharedPref.getString("recycleViewTeam", "");
                    Log.e("team sharedPref ",Team);
                    String id="1";
                    if(Team.contains("team 1")){ id="1"; }
                    else if(Team.contains("team 2")){ id="2"; }
                    else if(Team.contains("team 3")){id="3"; }
                    Log.e("id sharedPref ",id);

                    while(tasks.hasNext()){
                        Task task=tasks.next();
                        if(task.getTeamTasksId().contains(id)) {
                            Log.e("task", task.getTitle());
                            datalist.add(task);
                        }
                    }
                    viewAdapter customRecyclerViewAdapter = new viewAdapter(
                            datalist, position -> {
                        Task task = datalist.get(position);
                        Intent taskDetailActivity=new Intent(this,taskDetail.class);

                        taskDetailActivity.putExtra("lab_title",task.getTitle());
                        taskDetailActivity.putExtra("lab_body",task.getDescription());
                        taskDetailActivity.putExtra("lab_status",task.getStatus());
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
    @Override
    public void onResume(){
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(customRecyclerViewAdapter());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        readUsernameAndShowIt();

    }

    public void readUsernameAndShowIt(){
        TextView usernameTextView=findViewById(R.id.usernameTitle);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPref.getString(getString(R.string.username), "");
        usernameTextView.setText(username+"’s tasks");
    }
public viewAdapter customRecyclerViewAdapter(){
    viewAdapter customRecyclerViewAdapter = new viewAdapter(
            getAllItems(), position -> {
        Task task = getAllItems().get(position);
        Intent taskDetailActivity=new Intent(this,taskDetail.class);

        taskDetailActivity.putExtra("lab_title",task.getTitle());
        taskDetailActivity.putExtra("lab_body",task.getDescription());
        taskDetailActivity.putExtra("lab_status",task.getStatus());
        startActivity(taskDetailActivity);

    });
    return customRecyclerViewAdapter;
}
public void create3Teams(){

    Amplify.DataStore.query(Team.class,
            (teams )-> runOnUiThread(() ->{
                boolean Team1IsExist=true,Team2IsExist=true,Team3IsExist=true;
                while(teams.hasNext()){
                    Team team=teams.next();
                    if(team.getId()=="1") {
                        Team1IsExist=false;
                    }
                    else if(team.getId()=="2") {
                        Team2IsExist=false;
                    }
                    else if(team.getId()=="3") {
                        Team3IsExist=false;
                    }
                    Log.e("task",team.getName());
                }
               if(Team1IsExist){
                   Team team1=Team.builder().name("Team1").id("1").build();
                   Amplify.API.mutate(ModelMutation.create(team1),
                           response -> {
                               Log.i("Team1", "createTeam1");

                           }  ,
                           error -> Log.e("Team1", "Create failed", error)
                   );}
                        if(Team2IsExist){
                            Team team2=Team.builder().name("Team2").id("2").build();
                            Amplify.API.mutate(ModelMutation.create(team2),
                                    response -> {Log.i("Team2", "createTeam2");
                                    }
,
                                    error -> Log.e("Team2", "Create failed", error)
                            );}
                        if(Team3IsExist){
                            Team team3=Team.builder().name("Team3").id("3").build();
                            Amplify.API.mutate(ModelMutation.create(team3),
                                    response -> {Log.i("Team3", "createTeam3");
                                    },
                                    error -> Log.e("Team3", "Create failed", error)
                            );}
            }
                ), error -> {}
    );





}

    }


