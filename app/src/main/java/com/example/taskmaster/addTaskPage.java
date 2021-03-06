package com.example.taskmaster;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;


public class addTaskPage extends AppCompatActivity {
    public static final int REQUEST_CODE = 123;
    public static final String TAG = addTaskPage.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int PERMISSION_ID = 44;
    double latitude;
    double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_page);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        spinnerAdapterAndAddTaskButton();
        uploadPictureButtonClickListener();


        }

public void uploadPictureButtonClickListener(){

    Intent intent = getIntent();
    String action = intent.getAction();
    String type = intent.getType();

    Button upload = findViewById(R.id.uploadBtn);
    EditText task_name = findViewById(R.id.task_name);
    upload.setOnClickListener(view -> {
        if(Intent.ACTION_SEND.equals(action) && type != null) {
            if (task_name.getText().toString().trim().length() > 0) {
                Amplify.DataStore.query(Task.class,
                        (tasks) -> runOnUiThread(() -> {
                            boolean condition = true;

                            while (tasks.hasNext()) {
                                Task task = tasks.next();
                                Log.e(TAG, task.getTitle());
                                if (task_name.getText().toString().contains(task.getTitle())) {
                                    condition = false;
                                    break;
                                }
                            }
                            if (condition) {
                                pictureUpload();
                            } else {
                                Toast.makeText(this.getApplicationContext(), "title already exists", Toast.LENGTH_SHORT).show();
                            }
                        }), error -> {

                        });
            } else {
                Toast.makeText(this.getApplicationContext(), "please enter task title first", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this.getApplicationContext(), "already chosen an image", Toast.LENGTH_SHORT).show();
        }
    });
}

public String geocoder() throws IOException {
        try {


            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);


                if (addresses.size() > 0) {
                    return addresses.get(0).getCountryName() + "," + addresses.get(0).getLocality();
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

public void spinnerAdapterAndAddTaskButton() {
        Spinner spinner = findViewById(R.id.spinner);
        Button addTask = findViewById(R.id.addTaskBtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.teams_numbers, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Log.e(TAG,"first");
        addTask.setOnClickListener(view -> {
            EditText task_name = findViewById(R.id.task_name);
            EditText describtion=findViewById(R.id.describtion);
            if(task_name.getText().toString().trim().length()>0&&describtion.getText().toString().trim().length()>0) {

                    Amplify.DataStore.query(Task.class,
                            (tasks)-> runOnUiThread(() ->{
                                boolean condition=true;
                                while(tasks.hasNext()){
                                    Task task=tasks.next();
                                    Log.e(TAG,"second");
                                    if(task_name.getText().toString().equals(task.getTitle())){
                                        condition=false;
                                        break;
                                    }
                                }

                if(condition) {

                     if (spinner.getSelectedItem().toString().matches("team 1")) {

                          saveTaskInTaskTableAndTeamTable("1");
                         Log.e(TAG,"third");
                    } else if (spinner.getSelectedItem().toString().matches("team 2")) {
                          saveTaskInTaskTableAndTeamTable("2");
                         Log.e(TAG,"third");
                    } else if (spinner.getSelectedItem().toString().matches("team 3")) {
                          saveTaskInTaskTableAndTeamTable("3");
                         Log.e(TAG,"third");
                       }
                }else{
                      Toast.makeText(this.getApplicationContext(),"title already exists",Toast.LENGTH_SHORT).show();
                                }
                            }),error ->{

                            });
            }
                                else{
                Toast.makeText(this.getApplicationContext(),"please enter task name and describtion",Toast.LENGTH_SHORT).show();
            }

        });

    }


private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

@SuppressLint("MissingPermission")
private void requestNewLocationData() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, LocationCallBack(), Looper.myLooper());
    }


private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER);
    }


private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }


public void saveTaskInTaskTableAndTeamTable(String id) {

        EditText task_name_input = findViewById(R.id.task_name);
        EditText describtion_input = findViewById(R.id.describtion);

        String taskName = task_name_input.getText().toString();
        String describtion = describtion_input.getText().toString();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        Log.e(TAG,"fourth");

        Amplify.DataStore.query(Team.class, teams -> {
                    Log.e(TAG,"five");
                while (teams.hasNext()) {

                    if (teams.next().getId().contains(id)) {
                        Log.e(TAG,"eight");

                        if (checkPermissions()) {
                            Log.e(TAG,"ten");

                            if (isLocationEnabled()) {
                                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                                        Location location = task.getResult();
                                        if (location == null) {
                                            Log.e(TAG,"elven");

                                            requestNewLocationData();
                                        } else {
                                            Log.e(TAG,"nine");

                                            latitude = location.getLatitude();
                                            longitude = location.getLongitude();
                                            Log.e(TAG, latitude + " ");

                                            String result = null;
                                            try {
                                                result = geocoder();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


                                            Task newTask = Task.builder().
                                                    title(taskName).
                                                    description(describtion).
                                                    teamTasksId(id).
                                                    status("new").
                                                    location(result).
                                                    build();

                                            Amplify.DataStore.save(newTask,
                                                    success -> {
                                                        Log.e(TAG, "INSERTED SUCCESFULLY task");
                                                    }, failed -> {
                                                        Log.e(TAG, "FAILED TO INSERT task");

                                                    }
                                            );


                                            Amplify.API.mutate(ModelMutation.create(newTask),

                                                    response -> {
                                                        Log.i(TAG, "Todo with id mutate ");
                                                        if (Intent.ACTION_SEND.equals(action) && type != null) {
                                                            if (type.startsWith("image/")) {
                                                                try {
                                                                    Log.e(TAG,"intent image first");
                                                                    sendImageMethod(intent, newTask.getTitle());
                                                                    Log.e(TAG,"send Image method");
                                                                } catch (IOException e) {
                                                                    Log.e(TAG,"error send Image method");

                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    }

                                                    ,
                                                    error -> Log.e(TAG, "Create failed mutate", error)
                                            );
                                            Log.e(TAG,"added task with location");
                                            Intent returnHome=new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(returnHome);

                                        }


                                    }

                                });

                            } else {
                                Toast.makeText(this, "please turn on your location", Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent2);
                            }
                        } else {
                            requestPermissions();

                        }


                        break;
                    }
                }
//        }
//            else{
//                Log.e(TAG,"there is no teams in API")  ;
//            }
                    Log.e(TAG,"seven");
            }
            ,error->{
           Log.i(TAG, "error");
                    });


    }

public void fileUpload(File file,String title){
        Log.e(TAG,"intent image eight");

//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//            writer.append("Example file contents");
//            writer.close();
//        } catch (Exception exception) {
//            Log.e(TAG, "Upload failed", exception);
//        }

        Amplify.Storage.uploadFile(
                title+".jpg",
                file,
                result -> Log.e(TAG, "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
        );
        Log.e(TAG,"intent image ten");

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


     public void sendImageMethod(Intent intent,String title) throws IOException {
        Uri imageUri=intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if(imageUri !=null){
            Log.e(TAG,"intent image second");

            File file=getFileFromUri(imageUri,title);
            Log.e(TAG,"intent image third");

            fileUpload(file,title);
            Log.e(TAG,"intent image fourth");


        }
     }


     public LocationCallback LocationCallBack(){
         return new LocationCallback();
     }


     public File getFileFromUri(Uri uri,String title) throws IOException {
         Bitmap bitmap=getBtimapFromUri(uri);

         File file=new File(getApplicationContext().getFilesDir(),title+".jpg");
         OutputStream outputStream=new BufferedOutputStream(new FileOutputStream(file));
         bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
         outputStream.close();
         Log.e(TAG,"intent image six");

         return file;

     }
    }

