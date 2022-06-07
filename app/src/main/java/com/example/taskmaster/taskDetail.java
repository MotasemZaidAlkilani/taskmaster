package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.predictions.models.LanguageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

//import com.example.taskmaster.ui.AppDatabase;

public class taskDetail extends AppCompatActivity {
    private final MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        TextView labTextView = findViewById(R.id.lab);
        TextView descriptionTextView = findViewById(R.id.describtion);
        TextView status = findViewById(R.id.status);
        Button completeTask = findViewById(R.id.complete);
        Button translateBtn = findViewById(R.id.translate);
        Button speech=findViewById(R.id.speech);
        ImageView imageView = findViewById(R.id.imageView);
        TextView lab_location_text_view = findViewById(R.id.location);


        Intent fromHome = getIntent();
        String lab_title = fromHome.getStringExtra("lab_title");
        String lab_body = fromHome.getStringExtra("lab_body");
        String lab_state = fromHome.getStringExtra("lab_status");
        String lab_location = fromHome.getStringExtra("lab_location");

        pictureDownload(lab_title);
        Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir() + "/" + lab_title + ".jpg");
        imageView.setImageBitmap(bitmap);


        labTextView.setText(lab_title);
        descriptionTextView.setText(lab_body);
        status.setText(lab_state);
        lab_location_text_view.setText(lab_location);
        speech.setOnClickListener(view ->{
            Amplify.Predictions.convertTextToSpeech(
                    descriptionTextView.getText().toString(),
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e("MyAmplifyApp", "Conversion failed", error)
            );
        });

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




//      completeTask.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
//              AppDatabase.getInstance(getApplicationContext()).TaskDao().changeState("COMPLETE",lab_id);
//              Intent backToHome=new Intent(taskDetail.this,MainActivity.class);
//              startActivity(backToHome);
//          }
//      });
        });
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