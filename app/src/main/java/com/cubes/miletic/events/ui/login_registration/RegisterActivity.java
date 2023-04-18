package com.cubes.miletic.events.ui.login_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.databinding.ActivityRegisterBinding;
import com.cubes.miletic.events.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.etName.getText();
                String email = binding.etEmail.getText();

                if(binding.etEmail.isEmailValid(email)){
                    if(binding.etPassword.getText().length() > 3){
                        if(binding.etPassword.getText().equalsIgnoreCase(binding.etForgotPassword.getText())){
                            Toast.makeText(RegisterActivity.this, "Verification code for "+name+" sent to "+email,
                                    Toast.LENGTH_LONG).show();

                            auth.createUserWithEmailAndPassword(binding.etEmail.getText(), binding.etPassword.getText())
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            createUserFirestore();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Yout must enter password.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Invalid email address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void createUserFirestore() {
//        Map<String, Object> user = new HashMap<>();
//        user.put("name", binding.etName.getText());
//        user.put("surname", binding.etSurname.getText());
//        user.put("email", binding.etEmail.getText());

        User userModel = new User(binding.etEmail.getText().toString(), binding.etName.getText().toString(),
                binding.etSurname.getText().toString());

        FirebaseFirestore.getInstance().collection(Constants.TABLE_USER).document(auth.getUid())
                .set(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}