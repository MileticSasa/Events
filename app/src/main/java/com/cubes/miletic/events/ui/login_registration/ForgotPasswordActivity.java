package com.cubes.miletic.events.ui.login_registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    if(binding.etEmail.isEmailValid(binding.etEmail.getText() )){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.buttonRecover.setEnabled(true);
                            }
                        });
                    }
                    else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.buttonRecover.setEnabled(false);
                            }
                        });
                    }
                }
            }
        }).start();

        binding.buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = binding.etEmail.getText();

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ForgotPasswordActivity.this, R.string.message_reset_pass+ email, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });


    }
}