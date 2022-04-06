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
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n2n.letsgo.DataBaseHelper;
import com.n2n.letsgo.R;
import com.n2n.letsgo.TypeReView.Picture_Word_activity;
import com.n2n.letsgo.TypeReView.Word_Picture_activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Review extends AppCompatActivity {
    DataBaseHelper db = new DataBaseHelper(this);
    TextView tvTitle, tvMeaning, tvBack;
    ImageView imgFront;
    RadioButton rbtAnswer1, rbtAnswer2, rbtAnswer3;
    ArrayList<String> arrWord, arrMeaning, arrSound, arrAnswerShurffle;
    ArrayList<byte[]> arrPicture;
    int count1 = 0, chay = 0;
    String strID, Answer;
    AnimatorSet setRightOut, setLeftIn;
    boolean isBackVisible = false;
    RelativeLayout rltFlash, rltBackCard;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_layout);

        Intent in = getIntent();
        Bundle bundle = in.getBundleExtra("Data");
        strID = bundle.getString("ID");

        imgFront = findViewById(R.id.imgFront);
        tvTitle = findViewById(R.id.tvTitle);
        tvMeaning = findViewById(R.id.tvmeaning);
        rbtAnswer1 = findViewById(R.id.rbAnswer1);
        rbtAnswer2 = findViewById(R.id.rbAnswer2);
        rbtAnswer3 = findViewById(R.id.rbAnswer3);
        rltFlash = findViewById(R.id.relatFlash);
        rltBackCard = findViewById(R.id.rltBack);
        tvBack = findViewById(R.id.imgBack);
        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.flash_right_in);
        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.flash_left_in);



        GetTitleName(tvTitle, "SELECT Title FROM Title WHERE IDtitle=" + strID);
        if (Records(strID) > 0) {
            ShowFirstQuestion(imgFront, "SELECT IDtitle, Word, Meaning, Picture, Sound FROM Word WHERE IDtitle=" + strID);
            ShowAnswer(Answer);
        } else {
            Toast.makeText(Review.this, "No records to show", Toast.LENGTH_LONG).show();
        }
    }

    public void GetTitleName(TextView tvName, String strSELECT) {
        String name = null;
        Cursor cur = db.getCursor(strSELECT);
        if (cur.moveToFirst()) {
            do {
                name = cur.getString(0);
            } while (cur.moveToNext());
        }

        tvName.setText(name);

    }
    public void ShowFirstQuestion(ImageView imgView, String strSelect) {
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
        Answer = arrWord.get(0);
        tvBack.setText(Answer);
        tvMeaning.setText("Meaning: " + arrMeaning.get(0));
        tvMeaning.setVisibility(View.INVISIBLE);
    }

    public int Records(String ID) {
        int count;
        Cursor cur = db.getCursor("SELECT * FROM Word where IDtitle='" + ID + "'");
        count = cur.getCount();
        return count;
    }

    public ArrayList<String> ArrWordShuffle() {
        ArrayList<String> arrword = new ArrayList<>();
        Cursor cur = db.getCursor("SELECT Word FROM Word");
        if (cur.moveToFirst()) {
            do {
                arrword.add(cur.getString(0));
            } while (cur.moveToNext());
        }
        return arrword;
    }
    public void ShowAnswer(String ans) {
        arrAnswerShurffle = new ArrayList<>();
        arrAnswerShurffle = ArrWordShuffle();
        Collections.shuffle(arrAnswerShurffle);

        List<String> answer = new ArrayList<>();
        answer.add(ans);
        for (int i = 0; i < arrAnswerShurffle.size(); i++) {
            if (count1 < 2) {
                answer.add(arrAnswerShurffle.get(i));
                count1++;
            }
        }
        Collections.shuffle(answer);

        List<RadioButton> buttons = new ArrayList<>();
        buttons.add(findViewById(R.id.rbAnswer1));
        buttons.add(findViewById(R.id.rbAnswer2));
        buttons.add(findViewById(R.id.rbAnswer3));

        for (int i = 0; i < answer.size(); i++) {
            buttons.get(i).setText(answer.get(i));
        }
    }

    public void FlipCard() {
        if (!isBackVisible) {
            setRightOut.setTarget(imgFront);
            setLeftIn.setTarget(rltBackCard);
            setRightOut.start();
            setLeftIn.start();
            isBackVisible = true;
            rltBackCard.setVisibility(View.VISIBLE);
        } else {
            setRightOut.setTarget(rltBackCard);
            setLeftIn.setTarget(imgFront);
            setRightOut.start();
            setLeftIn.start();
            isBackVisible = false;
            rltBackCard.setVisibility(View.VISIBLE);
            tvMeaning.setVisibility(View.INVISIBLE);
        }
    }

    public void Next() {
        String title = tvTitle.getText().toString();

        if (chay < (arrWord.size() - 1)) {
            chay++;
            Bitmap bmp = BitmapFactory.decodeByteArray(arrPicture.get(chay), 0, arrPicture.get(chay).length);
            imgFront.setImageBitmap(bmp);
            Answer = arrWord.get(chay);
            tvBack.setText(Answer);
            tvMeaning.setText("Meaning: " + arrMeaning.get(chay));
            tvMeaning.setVisibility(View.INVISIBLE);
            ShowAnswer(Answer);
            if (isBackVisible) {
                setLeftIn.setTarget(imgFront);
                setLeftIn.start();
                rltBackCard.setVisibility(View.INVISIBLE);
                isBackVisible = !isBackVisible;
                tvMeaning.setVisibility(View.INVISIBLE);
            }
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Thông báo");
            alert.setMessage("Bạn đã ôn tập xong chủ đề " + title + " . Bạn có muôn ôn lại từ đầu?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                chay = 0;
                Bitmap bmp2 = BitmapFactory.decodeByteArray(arrPicture.get(chay), 0, arrPicture.get(chay).length);
                imgFront.setImageBitmap(bmp2);
                tvMeaning.setText(arrWord.get(chay));
                tvBack.setText(arrMeaning.get(chay));
                Answer = arrWord.get(chay);
            });
            alert.setNegativeButton("No", (dialog, which) -> onBackPressed());
            alert.create().show();
        }
    }

    public void Preview() {
        if (chay > 0) {
            chay--;
            Bitmap bmp = BitmapFactory.decodeByteArray(arrPicture.get(chay), 0, arrPicture.get(chay).length);
            imgFront.setImageBitmap(bmp);
            tvBack.setText(Answer);
            tvMeaning.setText("Meaning is: " + arrMeaning.get(chay));
            tvMeaning.setVisibility(View.INVISIBLE);
            ShowAnswer(Answer);
        }
    }

    public void CheckChooseAnswer() {
        String answer;
        if (rbtAnswer1.isChecked()) {
            answer = rbtAnswer1.getText().toString();
            if (answer.equals(Answer)) {
                FlipCard();
                tvMeaning.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(Review.this, "Bạn sai rồi! Thử chọn lại nào!!!", Toast.LENGTH_SHORT).show();
            }
        }

        if (rbtAnswer2.isChecked()) {
            answer = rbtAnswer2.getText().toString();
            if (answer.equals(Answer)) {
                FlipCard();
                tvMeaning.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(Review.this, "Bạn sai rồi! Thử chọn lại nào!!!", Toast.LENGTH_SHORT).show();
            }
        }

        if (rbtAnswer3.isChecked()) {
            answer = rbtAnswer3.getText().toString();
            if (answer.equals(Answer)) {
                FlipCard();
                tvMeaning.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(Review.this, "Bạn sai rồi! Thử chọn lại nào!!!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onCLickView(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                CheckChooseAnswer();
                break;
            case R.id.btnShowAnswer:
                FlipCard();
                break;
            case R.id.btNextReview:
                Next();
                break;
            case R.id.btPrevReview:
                Preview();
                break;
        }

    }

    }
