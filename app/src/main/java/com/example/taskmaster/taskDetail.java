package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.predictions.models.LanguageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;


public class taskDetail extends AppCompatActivity {
    private final MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);



        setDataInLayout();
        buttonsClickListeners();
        downloadPictureAndAppearIt();







    }
    public void setDataInLayout(){
        TextView labTextView = findViewById(R.id.lab);
        TextView descriptionTextView = findViewById(R.id.describtion);
        TextView status = findViewById(R.id.status);
        TextView lab_location_text_view = findViewById(R.id.location);


        Intent fromHome = getIntent();
        String lab_title = fromHome.getStringExtra("lab_title");
        String lab_body = fromHome.getStringExtra("lab_body");
        String lab_state = fromHome.getStringExtra("lab_status");
        String lab_location = fromHome.getStringExtra("lab_location");


        labTextView.setText(lab_title);
        descriptionTextView.setText(lab_body);
        status.setText(lab_state);
        lab_location_text_view.setText(lab_location);
    }
    public void buttonsClickListeners(){
        TextView descriptionTextView = findViewById(R.id.describtion);
        Button translateBtn = findViewById(R.id.translate);

        translateBtn.setOnClickListener(view ->{
            Pattern p=Pattern.compile("^[a-zA-Z]");
            String describtion=descriptionTextView.getText().toString();
            if (p.matcher(describtion).find()) {
                Toast.makeText(this, "english to arabic", Toast.LENGTH_SHORT).show();
                Amplify.Predictions.translateText(
                        descriptionTextView.getText().toString(),
                        LanguageType.ENGLISH,
                        LanguageType.ARABIC,
                        result ->
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("MyAmplifyApp", result.getTranslatedText());
                                    descriptionTextView.setText(result.getTranslatedText());
                                    translateBtn.setText("Back To Original");
                                }
                            });

                        },
                        error -> Log.e("MyAmplifyApp", "Translation failed", error)
                );
            }
            else {
                Toast.makeText(this, "arabic to english", Toast.LENGTH_SHORT).show();

                Amplify.Predictions.translateText(
                        descriptionTextView.getText().toString(),
                        LanguageType.ARABIC,
                        LanguageType.ENGLISH,
                        result ->
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("MyAmplifyApp", result.getTranslatedText());
                                    descriptionTextView.setText(result.getTranslatedText());
                                    translateBtn.setText("translate");
                                }
                            });

                        },
                        error -> Log.e("MyAmplifyApp", "Translation failed", error)
                );
            }





        });


        Button speech=findViewById(R.id.speech);

        speech.setOnClickListener(view ->{
            Amplify.Predictions.convertTextToSpeech(
                    descriptionTextView.getText().toString(),
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e("MyAmplifyApp", "Conversion failed", error)
            );
        });


        Button completeTask = findViewById(R.id.complete);

              completeTask.setOnClickListener(view -> {
                  Intent fromHome = getIntent();
                  String lab_id=fromHome.getStringExtra("lab_id");


                  Amplify.DataStore.query(Task.class,tasks->{
                      boolean condition=true;
                      while(tasks.hasNext()){
                          Log.e("check if there a tasks ",tasks.hasNext()+" ");
                          Task orginlaTask=tasks.next();
                          if(orginlaTask.getId().contains(lab_id)){
                              condition=false;
                              Task UpdatedTask = orginlaTask.copyOfBuilder()
                                      .status("complete").
                                      build();

                              Amplify.DataStore.save(UpdatedTask,
                                      saved -> Log.i("MyAmplifyApp", "Saved a post."),
                                      failure -> Log.e("MyAmplifyApp", "Save failed.", failure)
                              );


                              Toast.makeText(this.getApplicationContext(), "task is completed", Toast.LENGTH_SHORT).show();
                              Intent backToHome=new Intent(taskDetail.this,MainActivity.class);
                              startActivity(backToHome);
                          }
                      }
                      if(condition){
                          Toast.makeText(this.getApplicationContext(),"there is no task",Toast.LENGTH_SHORT).show();
                      }
                  },error->{
                      Toast.makeText(this.getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                  });




          }
    );
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
    public void downloadPictureAndAppearIt(){
        ImageView imageView = findViewById(R.id.imageView);
        Intent fromHome = getIntent();
        String lab_title = fromHome.getStringExtra("lab_title");
        pictureDownload(lab_title);
        Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir() + "/" + lab_title + ".jpg");
        imageView.setImageBitmap(bitmap);
    }
    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }
}