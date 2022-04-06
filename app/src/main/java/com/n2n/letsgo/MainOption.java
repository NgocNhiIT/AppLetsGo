package com.n2n.letsgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.n2n.letsgo.Dictionary.Dictionary_activity;
import com.n2n.letsgo.Title.Title_activity;
import com.n2n.letsgo.Video.Video_activity;

public class MainOption extends AppCompatActivity {
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_option);


    }
    public void ChooseAction(View view) {
        Intent in;

        switch (view.getId()) {
            case R.id.btnLearing:
                bundle = new Bundle();
                bundle.putString("Type", "learning");
                in = new Intent(view.getContext(), Title_activity.class);
                in.putExtra("Data", bundle);
                startActivity(in);
                break;
            case R.id.btnReview:
                bundle = new Bundle();
                bundle.putString("Type", "review");
                in = new Intent(view.getContext(), Title_activity.class);
                in.putExtra("Data", bundle);
                startActivity(in);;
                break;
            case R.id.btnVideo:
                in = new Intent(view.getContext(), Video_activity.class);
                startActivity(in);
                break;
            case R.id.btnDictionary:
                in = new Intent(view.getContext(), Dictionary_activity.class);
                startActivity(in);
                break;

        }

    }
}