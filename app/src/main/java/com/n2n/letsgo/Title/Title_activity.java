package com.n2n.letsgo.Title;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.n2n.letsgo.DataBaseHelper;
import com.n2n.letsgo.R;

import java.util.ArrayList;

public class Title_activity extends AppCompatActivity {
    DataBaseHelper db = new DataBaseHelper(this);
    ListView lvTitle;
    ArrayList<String> arrID, arrTitle, arrMeaning;
    ArrayList<byte[]> arrPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_layout);

        fetchdata();

        lvTitle = findViewById(R.id.lvTitle);

        HienThi(lvTitle, "SELECT * FROM Title");
    }
    private void fetchdata() {
        try {
            db.copyDB2SDCard();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
    private void HienThi(ListView lv, String strSelect) {

        arrID = new ArrayList<>();
        arrTitle = new ArrayList<>();
        arrMeaning = new ArrayList<>();
        arrPicture = new ArrayList<>();

        Cursor cur = db.getCursor(strSelect);
        if (cur.moveToFirst()) {
            do {
                arrID.add(cur.getString(0));
                arrTitle.add(cur.getString(1));
                arrPicture.add(cur.getBlob(2));
                arrMeaning.add(cur.getString(3));
            } while (cur.moveToNext());
        }
        lv.setAdapter(new TitleAdapter(this, arrID, arrTitle, arrMeaning, arrPicture));
    }
}