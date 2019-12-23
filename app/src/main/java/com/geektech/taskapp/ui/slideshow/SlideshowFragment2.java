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
import androidx.recyclerview.widget.RecyclerView;

import com.geektech.taskapp.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class SlideshowFragment2 extends Fragment implements View.OnClickListener {

    private SlideshowViewModel slideshowViewModel;
    private List<String> urls;
    private ProgressBar progressBar;
    RecyclerView recyclerView;
    ImageViewAdapter adapter;
    private List<File> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        recyclerView = root.findViewById(R.id.imageRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        list = new ArrayList<>();
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        adapter = new ImageViewAdapter(list);
//        recyclerView.setAdapter(adapter);


        root.findViewById(R.id.buttonDownload).setOnClickListener(this);
        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        urls = new ArrayList<>();
        urls.add("https://square.github.io/picasso/static/debug.png");
        urls.add("https://square.github.io/picasso/static/sample.png");
        return root;
    }

    @Override
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            File folder = new File(Environment.getExternalStorageDirectory(), "TaskApp/Images");
            folder.mkdirs();
            downloadFiles(folder);
        }else {
            EasyPermissions.requestPermissions(getActivity(), "Разрешить?", 101, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }


    }

    private void downloadFiles(final File folder) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < urls.size(); i++) {
                    String url = urls.get(i);
                    String filename = url.substring(url.lastIndexOf("l") + 1);
                    File file = new File(folder, filename);//todo
                    try {
                        FileUtils.copyURLToFile(new URL(url), file);
                        list.add(file);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.INVISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    });

                }

            }
        });
        thread.start();
    }
}