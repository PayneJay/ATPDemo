package com.leather.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_jump).setOnClickListener(v -> MyRouter.getInstance()
                .withString("paramKey1", "Hello World!")
                .withInt("paramKey2", 10)
                .withBool("paramKey3", true)
                .navigation("/app/secondActivity"));
    }
}