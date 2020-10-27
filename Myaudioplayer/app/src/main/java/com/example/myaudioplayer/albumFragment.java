package com.example.myaudioplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.myaudioplayer.MainActivity.albums;
import static com.example.myaudioplayer.MainActivity.musicFiles;


public class albumFragment extends Fragment {

    RecyclerView recyclerView;
    albumAdapter albumsAdapter;
    public albumFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if(!(albums.size() < 1)){
            albumsAdapter = new albumAdapter(getContext(),albums);
            recyclerView.setAdapter(albumsAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2 ));
        }
        return view;
    }
}