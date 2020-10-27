package com.example.myaudioplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MusicFiles> mfiles;

    MusicAdapter(Context mContext, ArrayList<MusicFiles> mfiles){
        this.mContext = mContext;
        this.mfiles = mfiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.fileName.setText(mfiles.get(position).getTitle());
        Glide.with(mContext).asBitmap()
                .load(R.drawable.music_icon)
                .into(holder.albumArt);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("positon",position);
                mContext.startActivity(intent);
            }
        });
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(mContext,v);
                popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                Toast.makeText(mContext, "Delete clicked", Toast.LENGTH_SHORT).show();
                                deleteSong(position, v);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }
    public void deleteSong(int postition,View v){
        Uri contentUri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,Long.parseLong(mfiles.get(postition).getID()));
        mfiles.remove(postition);
        File file = new File(mfiles.get(postition).getPath());
        boolean delete = file.delete();
        if (delete)
        {
            mContext.getContentResolver().delete(contentUri,null,null);
            mfiles.remove(postition);
            notifyItemRemoved(postition);
            notifyItemRangeChanged(postition,mfiles.size());
            Snackbar.make(v,"File is Deleted",Snackbar.LENGTH_LONG).show();
        }
        else{
            Snackbar.make(v,"File Can't Be Deleted",Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    public int getItemCount() {
        return mfiles.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        ImageView albumArt,menuMore;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.musicFileName);
            albumArt = itemView.findViewById(R.id.musicImg);
            menuMore = itemView.findViewById(R.id.menuMore);
        }
    }

     void updateList(ArrayList<MusicFiles> musicFilesArrayList){
        mfiles = new ArrayList<>();
        mfiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();
    }

}
