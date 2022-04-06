package com.n2n.letsgo.Dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n2n.letsgo.R;

import java.util.ArrayList;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MyViewHolder>{

    private ArrayList<DictionaryModel> data;
    Boolean check=false;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView word,meaning;

        RelativeLayout expandable;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.expandable= itemView.findViewById(R.id.expandableLayout);
            this.word= itemView.findViewById(R.id.wordtext);
            this.meaning = itemView.findViewById(R.id.meaningtext);

        }
    }

    public DictionaryAdapter(ArrayList<DictionaryModel> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);

        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(v -> {
            if(!check)
            {
                myViewHolder.expandable.animate()
                        .alpha(0.0f)
                        .setDuration(1000);
                myViewHolder.expandable.setVisibility(View.GONE);
                check=true;
            }
            else {
                myViewHolder.expandable.setVisibility(View.VISIBLE);
                myViewHolder.expandable.animate()
                        .alpha(1.0f)
                        .setDuration(1000);
                check=false;

            }
        });

        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        TextView word1= holder.word;
        TextView meaning1 = holder.meaning;

        word1.setText(data.get(position).getWord());
        meaning1.setText(data.get(position).getMeaning());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
