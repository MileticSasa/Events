package com.cubes.miletic.events.ui.login_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.databinding.ActivityUpdateEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateEmailActivity extends AppCompatActivity {

    private ActivityUpdateEmailBinding binding;

    private FirebaseAuth auth;
    private CollectionReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        ref = FirebaseFirestore.getInstance().collection(Constants.TABLE_USER);

        binding.tvEmail.setText(auth.getCurrentUser().getEmail());

        binding.btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etPassword.getText().length() > 0) {
                    authenticateUser();
                }
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pb.setVisibility(View.VISIBLE);

                if(Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
                    updateEmail();
                }
                else{
                    Toast.makeText(UpdateEmailActivity.this, "Enter valid email address!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateEmail() {

        auth.getCurrentUser().updateEmail(binding.etEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ref.document(auth.getCurrentUser().getUid())
                                    .update("email", binding.etEmail.getText().toString());

                            Toast.makeText(UpdateEmailActivity.this, "Updated!", Toast.LENGTH_SHORT).show();

                            binding.pb.setVisibility(View.GONE);
                            finish();
                        }
                    }
                });

        binding.pb.setVisibility(View.GONE);
    }

    private void authenticateUser() {
        binding.pb.setVisibility(View.VISIBLE);

        AuthCredential credential = EmailAuthProvider.getCredential(binding.tvEmail.getText().toString(),
                binding.etPassword.getText().toString());

        auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   binding.etEmail.setVisibility(View.VISIBLE);
                   binding.btnUpdate.setVisibility(View.VISIBLE);
                   binding.pb.setVisibility(View.GONE);
               }
               else{
                   Toast.makeText(UpdateEmailActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
               }
            }
        });

        binding.pb.setVisibility(View.GONE);
    }
}