package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;

import java.io.File;

//import com.example.taskmaster.ui.AppDatabase;

public class taskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        TextView labTextView=findViewById(R.id.lab);
        TextView descriptionTextView=findViewById(R.id.describtion);
        TextView status=findViewById(R.id.status);
        Button completeTask=findViewById(R.id.complete);
        ImageView imageView=findViewById(R.id.imageView);

        Intent fromHome=getIntent();
        String lab_title=fromHome.getStringExtra("lab_title");
        String lab_body=fromHome.getStringExtra("lab_body");
        String lab_state=fromHome.getStringExtra("lab_status");

        pictureDownload(lab_title);
        Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir()+"/"+lab_title+".jpg");
        imageView.setImageBitmap(bitmap);


//        String lab_id=fromHome.getStringExtra("lab_id"," ");
        labTextView.setText(lab_title);
        descriptionTextView.setText(lab_body);
        status.setText(lab_state);
//      completeTask.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
//              AppDatabase.getInstance(getApplicationContext()).TaskDao().changeState("COMPLETE",lab_id);
//              Intent backToHome=new Intent(taskDetail.this,MainActivity.class);
//              startActivity(backToHome);
//          }
//      });
    }
    private void pictureDownload(String lab_name) {

        Amplify.Storage.downloadFile(
                lab_name+".jpg",
                new File(getApplicationContext().getFilesDir() + lab_name+".jpg"),
                result -> {
                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                    Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());
                },
                error -> Log.e(TAG,  "Download Failure", error)
        );
    }

}