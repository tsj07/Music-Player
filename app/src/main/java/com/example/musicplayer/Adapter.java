package com.example.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.SongsViewHolder>{

    private String[] data;
    public Adapter(String[] data){
        this.data = data;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.songs_list_layout, parent, false);
        return new SongsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        String s = data[position];
        holder.title.setText(s);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class  SongsViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView album;

        public SongsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            album = itemView.findViewById(R.id.album);
        }
    }
}
