package com.cubes.miletic.events.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cubes.miletic.events.EventsApplication;
import com.cubes.miletic.events.NotificationPack.MyFirebaseMessagingService;
import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.ActivityMainBinding;
import com.cubes.miletic.events.fragments.FavouritesFragment;
import com.cubes.miletic.events.fragments.HomeFragment;
import com.cubes.miletic.events.fragments.MapFragment;
import com.cubes.miletic.events.fragments.ProfileFragment;
import com.cubes.miletic.events.fragments.SearchFragment;
import com.cubes.miletic.events.model.ModelToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private CollectionReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //check for notifications
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MY_SP", Context.MODE_PRIVATE);
        if(sp.getBoolean("checked", true))
            MyFirebaseMessagingService.notificationsTurnedOn = true;
        else
            MyFirebaseMessagingService.notificationsTurnedOn = false;

        //get information from notification
        if(getIntent().getExtras() != null){
            String url = getIntent().getExtras().getString("addr");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }

        auth = FirebaseAuth.getInstance();

        reference = FirebaseFirestore.getInstance().collection("Tokens");

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(binding.fragmentBox.getId(),
                new HomeFragment()).commit();

        binding.bottomNavigation.setItemBackgroundResource(R.color.purple);

        binding.btn.setVisibility(View.GONE);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Log.d("MainActivity", "Getting token error: "+task.getException());
                            return;
                        }

                        String token = task.getResult();
                        ModelToken modelToken = new ModelToken(token);

                        reference.document(auth.getCurrentUser().getUid()).set(modelToken)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this,
                                                ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

//        Button crashButton = new Button(this);
//        crashButton.setText("Test Crash");
//        crashButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                throw new RuntimeException("Test Crash"); // Force a crash
//            }
//        });
//
//        addContentView(crashButton, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));

        EventsApplication.sendAnalythicsEvent("MainActivity", "screen");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.navigation_map:
                            selectedFragment = new MapFragment();
                            break;
                        case R.id.navigation_favourites:
                            selectedFragment = new FavouritesFragment();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(binding.fragmentBox.getId(),
                            selectedFragment).commit();

                    return true;
                }
            };
}