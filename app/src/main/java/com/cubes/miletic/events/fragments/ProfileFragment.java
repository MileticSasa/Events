package com.cubes.miletic.events.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.NotificationPack.MyFirebaseMessagingService;
import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.FragmentProfileBinding;
import com.cubes.miletic.events.model.User;
import com.cubes.miletic.events.ui.ManageAcountActivity;
import com.cubes.miletic.events.ui.SplashscreenActivity;
import com.cubes.miletic.events.ui.login_registration.AccSettingsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private User currentUser = null;

    private boolean switchState;


    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        handleNotificationsWithSwitch();

        binding.circleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ManageAcountActivity.class));
            }
        });

        binding.accountSettingsLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AccSettingsActivity.class);
                intent.putExtra("user", currentUser);
                getContext().startActivity(intent);
            }
        });

        binding.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocation();
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getContext(), SplashscreenActivity.class));
            }
        });
    }

    private void handleNotificationsWithSwitch(){
        SharedPreferences sp = getActivity().getSharedPreferences("MY_SP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        switchState = sp.getBoolean("checked", true);
        binding.sw.setChecked(switchState);

        binding.sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.sw.isChecked()){
                    MyFirebaseMessagingService.notificationsTurnedOn = true;
                    editor.putBoolean("checked", true);
                    editor.apply();
                }
                else{
                    MyFirebaseMessagingService.notificationsTurnedOn = false;
                    editor.putBoolean("checked", false);
                    editor.apply();
                }
            }
        });
    }

    private void updateLocation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change location?");
        builder.setCancelable(false);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);

        EditText et = new EditText(getContext());
        et.setText(currentUser.location);

        layout.addView(et);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                firestore.collection(Constants.TABLE_USER).document(auth.getCurrentUser().getUid())
                        .update("location", et.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialogInterface.dismiss();
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();

        String uid = auth.getCurrentUser().getUid();

        firestore.collection(Constants.TABLE_USER).document(uid)
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error != null){
                                    return;
                                }

                                currentUser = value.toObject(User.class);

                                if(currentUser != null) {
                                    binding.tvName.setText(new StringBuilder().append(currentUser.name).append(" ").append(currentUser.surname).toString());
                                    binding.tvEmail.setText(currentUser.email);
                                    if(currentUser.location != null) {
                                        binding.tvLocation.setText(currentUser.location);
                                    }
                                    else{
                                        binding.tvLocation.setText(R.string.location);
                                    }

                                    try {
                                        Picasso.get().load(currentUser.image).into(binding.circleIv);
                                    }
                                    catch (Exception e){
                                        Picasso.get().load(R.drawable.ic_person).into(binding.circleIv);
                                    }
                                }
                            }
                        });
    }
}
