package com.n2n.letsgo.Vocabulary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n2n.letsgo.DataBaseHelper;
import com.n2n.letsgo.R;

import java.util.ArrayList;

public class Learning extends AppCompatActivity {
    TextView tvName, tvBack;
    ImageView imgFront;
    ImageButton btSound, btnPre, btnNext;
    DataBaseHelper db = new DataBaseHelper(this);
    ArrayList<String> arrWord, arrMeaning, arrSound;
    ArrayList<byte[]> arrPicture;
    MediaPlayer mediaPlayer;
    RelativeLayout rltFlash, rltBackCard;
    int chay = 0;
    AnimatorSet setRightOut, setLeftIn;
    boolean isBackVisible = false;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_layout);

        Intent in = getIntent();
        Bundle bundle = in.getBundleExtra("Data");
        String strID = bundle.getString("ID");

        tvName = findViewById(R.id.tvNamePic);
        imgFront = findViewById(R.id.imgFront);
        tvBack = findViewById(R.id.imgBack);
        btnPre = findViewById(R.id.btPrev);
        btnNext = findViewById(R.id.btNext);
        rltFlash = findViewById(R.id.relatFlash);
        rltBackCard = findViewById(R.id.rltBack);
        btSound = findViewById(R.id.btSound);


        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.flash_right_in);
        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.flash_left_in);

        rltFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FlipCard();
            }
        });

        if (Records(strID) > 0) {
            Display(imgFront, tvName, "SELECT IDtitle, Word, Meaning, Picture, Sound FROM Word WHERE IDtitle = " + strID, tvBack);
        } else {

            DisableControl();
        }



    }

    public void Display(ImageView imgView, TextView tvname, String strSelect, TextView tvback) {

        arrWord = new ArrayList<>();
        arrMeaning = new ArrayList<>();
        arrPicture = new ArrayList<>();
        arrSound = new ArrayList<>();
        Cursor cur = db.getCursor(strSelect);
        if (cur.moveToFirst()) {
            do {

                arrWord.add(cur.getString(1));
                arrMeaning.add(cur.getString(2));
                arrPicture.add(cur.getBlob(3));
                arrSound.add(cur.getString(4));
                Bitmap bmp = BitmapFactory.decodeByteArray(arrPicture.get(0), 0, arrPicture.get(0).length);
                imgView.setImageBitmap(bmp);
            } while (cur.moveToNext());
        }
        tvname.setText(arrWord.get(0));
        tvback.setText(arrMeaning.get(0));
        tvback.setGravity(Gravity.CENTER);
        tvback.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        tvname.setGravity(Gravity.CENTER);

    }

    public int Records(String ID) {
        int count;
        Cursor cur = db.getCursor("SELECT * FROM Word where IDtitle='" + ID + "'");
        count = cur.getCount();
        return count;
    }

    public void FlipCard() {
        if (!isBackVisible) {
            setRightOut.setTarget(imgFront);
            setLeftIn.setTarget(rltBackCard);
            setRightOut.start();
            setLeftIn.start();
            tvName.setVisibility(View.INVISIBLE);
            isBackVisible = true;
        } else {
            setRightOut.setTarget(rltBackCard);
            setLeftIn.setTarget(imgFront);
            setRightOut.start();
            setLeftIn.start();
            tvName.setVisibility(View.VISIBLE);
            isBackVisible = false;
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier(arrSound.get(chay), "raw", getPackageName()));
        mediaPlayer.start();
        rltBackCard.setVisibility(View.VISIBLE);

    }

    public void DisableControl() {
        tvName.setVisibility(View.INVISIBLE);
        imgFront.setVisibility(View.INVISIBLE);
        tvBack.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        btnPre.setVisibility(View.INVISIBLE);
    }

    public void Next() {
        if (chay < (arrWord.size() - 1)) {
            chay++;
            Bitmap bmp = BitmapFactory.decodeByteArray(arrPicture.get(chay), 0, arrPicture.get(chay).length);
            imgFront.setImageBitmap(bmp);
            tvName.setText(arrWord.get(chay));
            tvBack.setText(arrMeaning.get(chay));

            if (isBackVisible) {
                setLeftIn.setTarget(imgFront);
                setLeftIn.start();
                rltBackCard.setVisibility(View.INVISIBLE);
                isBackVisible = !isBackVisible;
                tvName.setVisibility(View.VISIBLE);
            }
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Thông báo");
            alert.setMessage("Bạn đã hoàn thành bài học. Bạn có muốn học lại từ đầu?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                chay = 0;
                Bitmap bmp2 = BitmapFactory.decodeByteArray(arrPicture.get(chay), 0, arrPicture.get(chay).length);
                imgFront.setImageBitmap(bmp2);
                tvName.setText(arrWord.get(chay));
                tvBack.setText(arrMeaning.get(chay));
                tvName.setGravity(Gravity.CENTER);
            });
            alert.setNegativeButton("No", (dialog, which) -> onBackPressed());
            alert.create().show();

        }
    }

    public void Previous() {
        if (chay > 0) {
            chay--;
            Bitmap bmp = BitmapFactory.decodeByteArray(arrPicture.get(chay), 0, arrPicture.get(chay).length);
            imgFront.setImageBitmap(bmp);
            tvName.setText(arrWord.get(chay));
            tvBack.setText(arrMeaning.get(chay));
            mediaPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier(arrSound.get(chay).toString(), "raw", getPackageName()));
            mediaPlayer.start();

        }
    }
    public void onSound() {
        Boolean mpPlayingStatus = false;
        if (mpPlayingStatus == true) {
            mediaPlayer.stop();
            mediaPlayer.release();
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), getResources()
                    .getIdentifier(arrSound.get(chay), "raw", getPackageName()));
            mediaPlayer.start();
        }
    }

    public void onClickControl(View view) {

        switch (view.getId()) {
            case R.id.btSound:
                onSound();
                break;
            case R.id.btNext:
                Next();
                break;
            case R.id.btPrev:
                Previous();
                break;

        }
    }

}