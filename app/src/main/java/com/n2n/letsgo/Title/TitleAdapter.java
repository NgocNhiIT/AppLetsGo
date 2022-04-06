package com.n2n.letsgo.Title;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n2n.letsgo.R;
import com.n2n.letsgo.Vocabulary.Learning;
import com.n2n.letsgo.Vocabulary.Review;

import java.util.ArrayList;

public class TitleAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> ID, Title, Meaning;
    ArrayList<byte[]> Picture;
    String   type;

    //constructor
    public TitleAdapter(Title_activity chooseTitle, ArrayList<String> arrID, ArrayList<String> arrTitle, ArrayList<String> arrMeaning, ArrayList<byte[]> arrPicture) {
        context = chooseTitle;
        ID = arrID;
        Title = arrTitle;
        Meaning = arrMeaning;
        Picture = arrPicture;
    }

    @Override
    public int getCount() {
        return ID.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tvTitle, tvMeaning;
        ImageView imgTitle;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.title_adapter, parent, false);


        holder.tvTitle = view.findViewById(R.id.tvTitle);
        holder.tvMeaning = view.findViewById(R.id.tvMeaning);
        holder.imgTitle = view.findViewById(R.id.imgPicture);


        holder.tvTitle.setText(Title.get(position));
        holder.tvMeaning.setText(Meaning.get(position));

        Bitmap bmp = BitmapFactory.decodeByteArray(Picture.get(position), 0,Picture.get(position).length);
        holder.imgTitle.setImageBitmap(bmp);

        view.setOnClickListener(v -> {

            Bundle bundle;
            Intent in = ((Title_activity) context).getIntent();
            bundle = in.getBundleExtra("Data");
            type = bundle.getString("Type");

            if (type.equals("learning")) {
                bundle.putString("ID", ID.get(position));
                in = new Intent(v.getContext(), Learning.class);
                in.putExtra("Data", bundle);
                context.startActivity(in);

            } else if (type.equals("review")) {
                bundle.putString("ID",ID.get(position));
                in = new Intent(v.getContext(), Review.class);
                in.putExtra("Data", bundle);
                context.startActivity(in);
            }
        });
        return view;
    }
}

