package com.n2n.letsgo.Dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.SearchView;

import com.n2n.letsgo.DataBaseHelper;
import com.n2n.letsgo.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Dictionary_activity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static ArrayList<DictionaryModel> data;
    DataBaseHelper  db= new DataBaseHelper(this);;
    ArrayList<String> wordlist;
    ArrayList<String> meanlist;
    LinkedHashMap<String,String> namelist;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_layout);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        searchView = findViewById(R.id.searchView);
        searchView.setQueryRefinementEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        fetchdata();
        HienThi(recyclerView, "SELECT * FROM Dictionary " );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();

                final ArrayList<DictionaryModel> filterList = new ArrayList<>();

                for (int i=0;i<wordlist.size();i++) {
                    final String text = wordlist.get(i).toLowerCase();
                    if (text.contains(newText)) {

                        filterList.add(new DictionaryModel(wordlist.get(i),meanlist.get(i)));
                    }
                }
                adapter = new DictionaryAdapter(filterList);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });
    }

    private void fetchdata() {
        try {
            db.copyDB2SDCard();
            db.OpenDB();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void HienThi(RecyclerView recyclerView, String strSelect) {
        namelist=new LinkedHashMap<>();
        wordlist = new ArrayList<>();
        meanlist = new ArrayList<>();
        Cursor cur = db.getCursor(strSelect);
        if (cur.moveToFirst()) {
            do {
                wordlist.add(cur.getString(0));
                meanlist.add(cur.getString(2));
            } while (cur.moveToNext());

            Iterator entries = namelist.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) entries.next();
                wordlist.add(String.valueOf(thisEntry.getKey()));
                meanlist.add("-"+ thisEntry.getValue());
            }
            data = new ArrayList<>();
            for (int i = 0; i < wordlist.size(); i++) {
                data.add(new DictionaryModel(wordlist.get(i), meanlist.get(i)));
            }
            adapter = new DictionaryAdapter(data);
            recyclerView.setAdapter(adapter);
        }
    }
    }


