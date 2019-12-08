package com.geektech.taskapp.ui.gallery;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.os.Environment.getExternalStorageDirectory;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    Button button;
    ArrayList<URL> urls;
    ArrayList<File> files;
    ProgressBar progressBar;
//    Handler handler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

            }
        });
        button = root.findViewById(R.id.downloadBtn);
        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        file();
        return root;
    }

    @AfterPermissionGranted(101)
    private void file() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    File folder = new File(getExternalStorageDirectory(), "Gallery");
                    folder.mkdirs();

                    files = new ArrayList<>();

                    File imageFile1 = new File(folder, "image1.png");
                    File imageFile2 = new File(folder, "image2.png");
                    File imageFile3 = new File(folder, "image3.png");
                    File imageFile4 = new File(folder, "image4.png");
                    File imageFile5 = new File(folder, "image5.png");


                    files.add(imageFile1);
                    files.add(imageFile2);
                    files.add(imageFile3);
                    files.add(imageFile4);
                    files.add(imageFile5);
                    downloadFile();
                } else {
                    EasyPermissions.requestPermissions(getActivity(), "Разрешить?", 101, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });
    }

    public void downloadFile() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    urls = new ArrayList<>();

                    urls.add(new URL("https://square.github.io/picasso/static/debug.png"));
                    urls.add(new URL("https://square.github.io/picasso/static/sample.png"));
                    urls.add(new URL("https://st2.depositphotos.com/2001755/5408/i/450/depositphotos_54081723-stock-photo-beautiful-nature-landscape.jpg"));
                    urls.add(new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQoc1J1qZx4Ogm9-0hrkKC7RvH0rGW8U5pv6v3wl1zfSkDF-FKi&s"));
                    urls.add(new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjQBJWBho1TIpjLnH7jNwt5LIQQv4hXcBeeW4hBkpVxw3M01abRg&s"));
                    for (int i = 0; i < urls.size(); i++) {

                        FileUtils.copyURLToFile(urls.get(i), files.get(i));

                    }
                    progressBar.setVisibility(View.INVISIBLE);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}