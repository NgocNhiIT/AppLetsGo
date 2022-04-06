package com.n2n.letsgo.Video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.n2n.letsgo.MainActivity;
import com.n2n.letsgo.R;

public class PlayVideo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayerView youTubePlayerView;
    String id="";
    int REQUESVIDEO =999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        Intent intent = getIntent();
        id = intent.getStringExtra("idVideo");

        youTubePlayerView = findViewById(R.id.myYT);

        youTubePlayerView.initialize(Video_activity.API_KEY,this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(id);
        youTubePlayer.setFullscreen(true);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(PlayVideo.this,REQUESVIDEO);
        }else{
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUESVIDEO){
            youTubePlayerView.initialize(Video_activity.API_KEY,PlayVideo.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}