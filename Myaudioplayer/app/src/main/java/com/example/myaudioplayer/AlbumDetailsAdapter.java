package com.example.myaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder> {

    private Context mcontext;
    static ArrayList<MusicFiles>albumFiles;
    View view;
    public AlbumDetailsAdapter(Context context, ArrayList<MusicFiles> albumFiles) {
        this.mcontext = context;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mcontext).inflate(R.layout.music_items,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.album_name.setText(albumFiles.get(position).getTitle());
        byte [] image = getAlbumArt(albumFiles.get(position).getPath());

        if(image != null)
        {
            Glide.with(mcontext).asBitmap()
                    .load(image)
                    .into(holder.album_image);
        }
        else{
            Glide.with(mcontext)
                    .load(R.drawable.bewedoc)
                    .into(holder.album_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, PlayerActivity.class);
                intent.putExtra("sender","albumDetails");
                intent.putExtra("postition",position);
                mcontext.startActivity(intent);
            }
});


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView album_image;
        TextView album_name;

        public MyHolder(@NonNull View itemView){
            super(itemView);
            album_image = itemView.findViewById(R.id.musicImg);
            album_name = itemView.findViewById(R.id.musicFileName);
        }
    }
    private byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte [] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

}