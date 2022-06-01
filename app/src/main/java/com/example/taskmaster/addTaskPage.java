package com.example.taskmaster;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;


public class addTaskPage extends AppCompatActivity {
    public static final int REQUEST_CODE = 123;
    public static final String TAG = addTaskPage.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_page);
        Button addTask = findViewById(R.id.addTaskBtn);
        Button upload = findViewById(R.id.uploadBtn);

        spinnerAdapterAndAddTaskButton();


        upload.setOnClickListener(view ->{
            pictureUpload();
        });

    }
    public void spinnerAdapterAndAddTaskButton(){
        Spinner spinner = findViewById(R.id.spinner);
        Button addTask = findViewById(R.id.addTaskBtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.teams_numbers, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        addTask.setOnClickListener(view -> {

            Log.e("spinner",spinner.getSelectedItem().toString());
            if (spinner.getSelectedItem().toString().matches("team 1") ) {
                saveTaskInTaskTableAndTeamTable("1");
            }
            else if (spinner.getSelectedItem().toString().matches("team 2")) {
                saveTaskInTaskTableAndTeamTable("2");
            } else if (spinner.getSelectedItem().toString().matches("team 3")) {
                saveTaskInTaskTableAndTeamTable("3");
            }


        });

    }
    public void saveTaskInTaskTableAndTeamTable(String id){

        EditText task_name_input = findViewById(R.id.task_name);
        EditText describtion_input = findViewById(R.id.describtion);

        String taskName = task_name_input.getText().toString();
        String describtion = describtion_input.getText().toString();






        Amplify.DataStore.query(Team.class, teams->{

                while(teams.hasNext()){

                if (teams.next().getId().contains(id)) {

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
            }
            ,error->{
           Log.i("mutates team", "error");
                    });


    }
    public void fileUpload(){
        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
            writer.append("Example file contents");
            writer.close();
        } catch (Exception exception) {
            Log.e("MyAmplifyApp", "Upload failed", exception);
        }

        Amplify.Storage.uploadFile(
                "ExampleKey",
                exampleFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }
    public void pictureUpload(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK){
           Log.e(TAG,"onActivityResult: Error getting image from device");
           return ;

        }

        switch(requestCode){
            case REQUEST_CODE:

                Uri currentUri=data.getData();
                EditText task_name_input = findViewById(R.id.task_name);

                String taskName = task_name_input.getText().toString();
                Log.i(TAG,"onActivityResult : the uri is =>"+currentUri);

                try{
                    Bitmap bitmap=getBtimapFromUri(currentUri);

                    File file=new File(getApplicationContext().getFilesDir(),taskName+".jpg");

                    OutputStream os=new BufferedOutputStream(new FileOutputStream(file));

                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);

                    os.close();
                    Amplify.Storage.uploadFile(
                            taskName+".jpg",
                            file,
                            result -> Log.i(TAG, "Successfully uploaded: " + result.getKey()),
                            storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
                }

        }

        private Bitmap getBtimapFromUri(Uri uri) throws IOException{
             ParcelFileDescriptor parcelFileDescriptor=
                     getContentResolver().openFileDescriptor(uri,"r");

            FileDescriptor fileDescriptor=parcelFileDescriptor.getFileDescriptor();
            Bitmap image= BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();

            return image;

        }
    }

