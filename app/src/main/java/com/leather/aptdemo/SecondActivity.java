package com.leather.aptdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leather.apt_annotation.Router;

@Router(path = "/app/secondActivity")
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String stringExtra = getIntent().getStringExtra("paramKey1");
        int intExtra = getIntent().getIntExtra("paramKey2", 0);
        boolean boolExtra = getIntent().getBooleanExtra("paramKey3", false);
        Toast.makeText(this, stringExtra + "\n" + intExtra + "\n" + boolExtra, Toast.LENGTH_SHORT).show();

        findViewById(R.id.btn_jump).setOnClickListener(v -> {
            MyRouter.getInstance().navigation("/app/thirdActivity");
        });
    }
}