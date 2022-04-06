package com.n2n.letsgo.Video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.n2n.letsgo.MainActivity;
import com.n2n.letsgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Video_activity extends AppCompatActivity {
    public static String API_KEY = "AIzaSyDZa1UZo9bpERClWHre2pAwpilYwj5B4fE";
    String ID_PLAYLIST ="PLie7Bz5kXLyH-QpxrVEci8qUOgZ7qbezf";
    String urlgetJson = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+ID_PLAYLIST+"&key="+API_KEY+"&maxResults=50";

    ListView lvVideo;
    ArrayList<VideoModule> arrayVideo;
    VideoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);

        lvVideo = findViewById(R.id.listviewVideo);
        arrayVideo = new ArrayList<>();

        adapter = new VideoAdapter(this,R.layout.row_video,arrayVideo);
        lvVideo.setAdapter(adapter);

        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(Video_activity.this,PlayVideo.class);
                intent.putExtra("idVideo",arrayVideo.get(i).getIdVideo());
                startActivity(intent);
            }
        });
        getJson(urlgetJson);


    }
    private void getJson(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonItems = response.getJSONArray("items");
                    String title ="";
                    String url="";
                    String idVideo ="";
                    for (int i=0;i<jsonItems.length();i++){
                        JSONObject jsonItem = jsonItems.getJSONObject(i);

                        JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");

                        title = jsonSnippet.getString("title");

                        JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");

                        JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");

                        url = jsonMedium.getString("url");

                        JSONObject jsonResourceID = jsonSnippet.getJSONObject("resourceId");

                        idVideo = jsonResourceID.getString("videoId");

                        arrayVideo.add(new VideoModule(title,url,idVideo));


                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                error -> Toast.makeText(Video_activity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonObjectRequest);

    }
}