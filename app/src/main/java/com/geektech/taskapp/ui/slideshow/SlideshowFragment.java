package com.geektech.taskapp.ui.slideshow;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.geektech.taskapp.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;

public class SlideshowFragment extends Fragment implements View.OnClickListener {

    private SlideshowViewModel slideshowViewModel;
    private List<String> urls;
        private ProgressBar progressBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
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
        File folder = new File(Environment.getExternalStorageDirectory(), "TaskApp/Images");
        folder.mkdirs();
        downloadFiles(folder);


    }

    private void downloadFiles(final File folder){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <urls.size() ; i++) {
                    String url = urls.get(i);
                    String filename = url.substring(url.lastIndexOf("l") + 1);
                    File file = new File(folder, filename );//todo
                    try {
                        FileUtils.copyURLToFile(new URL(url), file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    progressBar.setVisibility(View.INVISIBLE);

                }

            }
        });thread.start();
    }
}