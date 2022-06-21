package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthChannelEventName;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.hub.HubChannel;
import com.example.taskmaster.ui.viewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String TAG="MainActivity";
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        authSession("onCreate");
       create3Teams();
        BannerAdView();
        buttonClickListerens();
        clearDatastore();

    }

    public void clearDatastore(){
        final String signedOutEventName = AuthChannelEventName.SIGNED_OUT.toString();

        Amplify.Hub.subscribe(HubChannel.AUTH,
                anyAuthEvent -> signedOutEventName.equals(anyAuthEvent.getName()),
                signedOutEvent -> Amplify.DataStore.clear(
                        () -> Log.i("MyAmplifyApp", "DataStore is cleared."),
                        failure -> Log.e("MyAmplifyApp", "Failed to clear DataStore.")
                )
        );
    }

    public void buttonClickListerens(){
        TextView rewardAmountTextView=findViewById(R.id.rewardAmount);

        Button addTask=findViewById(R.id.addTask);
        Button allTask=findViewById(R.id.allTask);
        Button setting=findViewById(R.id.setting);
        Button rewaredButton=findViewById(R.id.rewardAd);
        Button interstitilAD=findViewById(R.id.interstitialAd);
        rewaredButton.setOnClickListener(view ->{
            AdRequest adRequest = new AdRequest.Builder().build();

            RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            Log.d(TAG, loadAdError.getMessage());
                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            if (mRewardedAd != null) {
                                Activity activityContext = MainActivity.this;
                                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        // Handle the reward.
                                        Log.d(TAG, "The user earned the reward.");
                                        int rewardAmount = rewardItem.getAmount();
                                        String rewardType = rewardItem.getType();
                                        rewardAmountTextView.setText("reward amount:"+rewardAmount);
                                    }
                                });
                            } else {
                                Log.d(TAG, "The rewarded ad wasn't ready yet.");
                            }
                            Log.d(TAG, "Ad was loaded.");
                        }
                    });
        });

        interstitilAD.setOnClickListener(view ->{
            AdRequest adRequestInterstitialAD = new AdRequest.Builder().build();

            InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequestInterstitialAD,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(MainActivity.this);
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                            }
                            Log.i(TAG, "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.i(TAG, loadAdError.getMessage());
                            mInterstitialAd = null;
                        }
                    });
        });


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                logout();
                break;
            case R.id.resest:
                break;
            default:
        }
        return true;
    }

    public List<Task> getAllItems() {
        Log.e(TAG,"first");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        List<Task>datalist = new ArrayList();
        Amplify.DataStore.query(Task.class,
                tasks -> runOnUiThread(() ->{
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    String Team = sharedPref.getString("recycleViewTeam", "");
                    Log.e(TAG,Team);
                    String id="1";
                    if(Team.contains("team 1")){ id="1"; }
                    else if(Team.contains("team 2")){ id="2"; }
                    else if(Team.contains("team 3")){id="3"; }
                    Log.e(TAG,id);
                    Log.e(TAG,"second");

                        while (tasks.hasNext()) {
                            Task task=tasks.next();
                            if (task.getTeamTasksId().contains(id)) {
                                Log.e(TAG, task.getTitle());
                                datalist.add(task);
                            }
                        }

                    viewAdapter customRecyclerViewAdapter = new viewAdapter(
                            datalist, position -> {
                        Task task = datalist.get(position);
                        Intent taskDetailActivity=new Intent(this,taskDetail.class);

                        taskDetailActivity.putExtra("lab_id",task.getId());
                        taskDetailActivity.putExtra("lab_title",task.getTitle());
                        taskDetailActivity.putExtra("lab_body",task.getDescription());
                        taskDetailActivity.putExtra("lab_status",task.getStatus());
                        taskDetailActivity.putExtra("lab_location",task.getLocation());
                        Log.e(TAG,"third");
                        startActivity(taskDetailActivity);

                    });
                    Log.e(TAG,"fourth");

                    recyclerView.setAdapter(customRecyclerViewAdapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    Log.e(TAG,"five");

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
        Log.e("location",task.getLocation());

        taskDetailActivity.putExtra("lab_location",task.getLocation());
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

                    while (teams.hasNext()) {
                        Team team=teams.next();
                        if (team.getId().contains( "1")) {
                            Team1IsExist = false;
                        } else if (team.getId().contains("2") ){
                            Team2IsExist = false;
                        } else if (team.getId() .contains( "3")) {
                            Team3IsExist = false;
                        }
                        Log.e(TAG, team.getName());
                    }
                        Log.e(TAG, "team1"+Team1IsExist);
                        Log.e(TAG, "team2"+Team2IsExist);
                        Log.e(TAG, "team3"+Team3IsExist);

                        Team team1 = Team.builder().name("Team1").id("1").build();

                    if (Team1IsExist) {
                        Amplify.DataStore.save(team1,
                                success -> {
                                    Log.e(TAG, "INSERTED SUCCESFULLY team1"+team1.getName());
                                }, failed -> {
                                    Log.e(TAG, "FAILED TO INSERT team1");

                                }
                        );

                    }
                        Amplify.API.mutate(ModelMutation.create(team1),
                                response -> {
                                    Log.e(TAG, "createTeam1"+team1.getName());

                                },
                                error -> Log.e(TAG, "Create failed", error)
                        );
                        Team team2 = Team.builder().name("Team2").id("2").build();
                        if (Team2IsExist) {
                        Amplify.DataStore.save(team2,
                                success -> {
                                    Log.e(TAG, "INSERTED SUCCESFULLY team2"+team2.getName());
                                }, failed -> {
                                    Log.e(TAG, "FAILED TO INSERT team2");

                                }
                        );

                    }
                        Amplify.API.mutate(ModelMutation.create(team2),
                                response -> {
                                    Log.e(TAG, "create Team2 "+team2.getName());
                                }
                                ,
                                error -> Log.e(TAG, "Create failed team2 ", error)
                        );
                        Team team3 = Team.builder().name("Team3").id("3").build();
                        if (Team3IsExist) {
                        Amplify.DataStore.save(team3,
                                success -> {
                                    Log.e(TAG, "INSERTED SUCCESFULLY team3 "+team3.getName());
                                }, failed -> {
                                    Log.e(TAG, "FAILED TO INSERT team3");

                                }
                        );

                    }
                        Amplify.API.mutate(ModelMutation.create(team3),
                                response -> {
                                    Log.e(TAG, "createTeam3");
                                },
                                error -> Log.e(TAG, "Create failed team3", error)
                        );
                }

                ), error -> {}
    );





}

    private void authSession(String method) {
        Amplify.Auth.fetchAuthSession(
                result -> Log.i(TAG, "Auth Session => " + method + result.toString()),
                error -> Log.e(TAG, error.toString())
        );
    }

    private void logout() {
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    authSession("logout");
                    finish();
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    public void BannerAdView(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    }


