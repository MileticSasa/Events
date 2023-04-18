package com.cubes.miletic.events.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cubes.miletic.events.databinding.ActivityShowImageBinding;
import com.squareup.picasso.Picasso;

public class ShowImageActivity extends AppCompatActivity {

    private ActivityShowImageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String imageUrl = getIntent().getStringExtra("image");

        Picasso.get().load(imageUrl).into(binding.imageView);
    }

}