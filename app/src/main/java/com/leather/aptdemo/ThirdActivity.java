package com.leather.aptdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.leather.apt_annotation.Router;

@Router(path = "/app/thirdActivity")
public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

    }
}