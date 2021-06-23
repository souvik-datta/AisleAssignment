package com.souvik.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.souvik.myapplication.R;
import com.souvik.myapplication.databinding.RecyclerRowBinding;
import com.souvik.myapplication.model.Likes;

import java.util.ArrayList;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {

    private ArrayList<Likes> list;
    private onClickListener listener;

    public LikesAdapter(ArrayList<Likes> list, onClickListener listener){
        this.list = list;
        this.listener = listener;
    }
    
    @Override
    public LikesAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        RecyclerRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recycler_row,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull  LikesAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerRowBinding binding;

        public ViewHolder(@NonNull RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Likes data,onClickListener listener){
            binding.tvName.setText(data.getFirstName());
            Glide.with(binding.ivImage.getContext())
                    .load(data.getAvatar())
                    .error(R.drawable.photo_1)
                    .into(binding.ivImage);
            binding.clContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public interface onClickListener{
        void onClick(int position);
    }
}
