package com.geektech.taskapp.ui.slideshow;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geektech.taskapp.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private List<String> urls;
    RecyclerView recyclerView;
    ImageViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);


        recyclerView = root.findViewById(R.id.imageRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        urls = new ArrayList<>();
        urls.add("https://square.github.io/picasso/static/debug.png");
        urls.add("https://square.github.io/picasso/static/sample.png");
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ImageViewAdapter(urls);
        recyclerView.setAdapter(adapter);

        return root;
    }


}