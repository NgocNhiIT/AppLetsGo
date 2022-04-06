package com.n2n.letsgo.Video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.n2n.letsgo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<VideoModule> videoList;

    public VideoAdapter(Context context, int layout, List<VideoModule> videoList) {
        this.context = context;
        this.layout = layout;
        this.videoList = videoList;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgThumbnail;
        TextView textviewTitle;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);

            holder.textviewTitle = view.findViewById(R.id.textviewTitle);
            holder.imgThumbnail = view.findViewById(R.id.imgThumbnail);

            view.setTag(holder);

        }else{
            holder = (ViewHolder) view.getTag();
        }
        VideoModule video =  videoList.get(i);

        holder.textviewTitle.setText(video.getTitle());
        Picasso.with(context).load(video.getThumbnail()).into(holder.imgThumbnail);
        return view;
    }
}
