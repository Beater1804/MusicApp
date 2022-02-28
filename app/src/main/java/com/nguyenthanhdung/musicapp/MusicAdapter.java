package com.nguyenthanhdung.musicapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<MusicModel> data = new ArrayList<>();

    public MusicAdapter() {

    }

    public void setData(List<MusicModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        MusicModel musicModel = data.get(position);

        View view = holder.getView();
        TextView tvName = view.findViewById(R.id.tvNameOfSong);
        tvName.setText(musicModel.getNameOfSong());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {

        private View view;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
        View getView(){
            return view;
        }
    }


}
