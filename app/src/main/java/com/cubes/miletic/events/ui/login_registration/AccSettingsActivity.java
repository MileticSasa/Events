package com.cubes.miletic.events.ui.login_registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.databinding.ActivityAccSettingsBinding;
import com.cubes.miletic.events.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccSettingsActivity extends AppCompatActivity {

    private ActivityAccSettingsBinding binding;

    private FirebaseAuth auth;
    private CollectionReference ref;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = getIntent().getParcelableExtra("user");

        auth = FirebaseAuth.getInstance();
        ref = FirebaseFirestore.getInstance().collection(Constants.TABLE_USER);

        setDataIntoViews();

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNameAndSurname();
            }
        });

        binding.ivEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccSettingsActivity.this, UpdateEmailActivity.class));
            }
        });

        binding.ivEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccSettingsActivity.this, UpdatePasswordActivity.class));
            }
        });
    }

    private void updateNameAndSurname() {

        if(binding.etName.getText().toString().length() > 0
                && binding.etSurname.getText().toString().length() > 0){

            currentUser.name = binding.etName.getText().toString();
            currentUser.surname = binding.etSurname.getText().toString();

            ref.document(auth.getCurrentUser().getUid()).set(currentUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(AccSettingsActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
        else{
            Toast.makeText(this, "Enter valid data!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setDataIntoViews() {
        binding.etName.setText(currentUser.name);
        binding.etSurname.setText(currentUser.surname);
    }
}