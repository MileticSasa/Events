package com.cubes.miletic.events.ui.login_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.ActivityUpdateEmailBinding;
import com.cubes.miletic.events.databinding.ActivityUpdatePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdatePasswordActivity extends AppCompatActivity {

    private ActivityUpdatePasswordBinding binding;

    private FirebaseUser user;
    private CollectionReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseFirestore.getInstance().collection(Constants.TABLE_USER);

        binding.tvEmail.setText(user.getEmail());

        binding.btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etPassword.getText().length() > 0){
                    authenticateUser();
                }
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etNewPassword.getText().length() > 0){
                    updatePassword();
                }
            }
        });
    }

    private void updatePassword() {
        binding.pb.setVisibility(View.VISIBLE);

        user.updatePassword(binding.etNewPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(UpdatePasswordActivity.this, "Updated!", Toast.LENGTH_SHORT).show();

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

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    binding.etNewPassword.setVisibility(View.VISIBLE);
                    binding.btnUpdate.setVisibility(View.VISIBLE);
                    binding.pb.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(UpdatePasswordActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.pb.setVisibility(View.GONE);
    }
}