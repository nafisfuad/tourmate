package com.nafisfuad.firebasemad16.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nafisfuad.firebasemad16.R;
import com.nafisfuad.firebasemad16.pojos.Moments;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.MomentViewHolder> {
    private Context context;
    private List<Moments> momentsList;

    public MomentsAdapter(Context context, List<Moments> momentsList) {
        this.context = context;
        this.momentsList = momentsList;
    }

    @NonNull
    @Override
    public MomentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MomentViewHolder(LayoutInflater.from(context).inflate(R.layout.gallery_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MomentViewHolder holder, int position) {
        Picasso.get().load(momentsList.get(position).getDownloadUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return momentsList.size();
    }

    class MomentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MomentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gallery_row_moment);
        }
    }
}
