package com.n2n.letsgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton igbtnplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        igbtnplay = findViewById(R.id.imageButtonplay);

        igbtnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manhinhoption = new Intent(MainActivity.this,MainOption.class);
                startActivity(manhinhoption);
            }
        });
    }
}