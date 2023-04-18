package com.cubes.miletic.events.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebViewClient;

import com.cubes.miletic.events.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {

    private ActivityWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String address = getIntent().getStringExtra("addr");

        binding.videoView.setWebViewClient(new WebViewClient());
        binding.videoView.loadUrl(address);
        binding.videoView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if(binding.videoView.canGoBack()){
            binding.videoView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}