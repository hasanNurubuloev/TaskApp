package com.geektech.taskapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.geektech.taskapp.onboard.OnBoardActivity;
import com.geektech.taskapp.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/*1. Показывать ПрогрессБар
2. Сохранять url аватара для юзера
3. Чтоб аватары не добавлялись, а менялись на Storage*/
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    HomeFragment hf;
    Task task;
    private TextView editTextName;
    private TextView editTextEmail;
    private String name;
    private String email;
    private String userId;
    private ImageView imageViewHader;
    View header;
    Button signOut;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isShown = Prefs.getInstance().getIsShown();
        name = Prefs.getInstance().getPreferences()
                .getString("name", "No name");
        email = Prefs.getInstance().getPreferences()
                .getString("email", "No name");
        userId = FirebaseAuth.getInstance().getUid();
        Log.e("ololo", "onCreate: name email " + name + email);


        if (!isShown) {
            startActivity(new Intent(this, OnBoardActivity.class));
            finish();
            return;
        }
//        if (FirebaseAuth.getInstance().getCurrentUser() == null) { //
//            startActivity(new Intent(this, PhoneActivity.class));
//            finish();
//            return;
//        }

        setContentView(R.layout.activity_main);

        info();
        signOut = findViewById(R.id.sign_out);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivityForResult(intent, 42);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        imageViewHader = header.findViewById(R.id.imageView);
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        storage.child("avatars/" + userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this).load(uri).circleCrop().into(imageViewHader);
            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        initFile();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void downloadFile(final File imageFile) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://square.github.io/picasso/static/sample.png");
                    FileUtils.copyURLToFile(url, imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextName = header.findViewById(R.id.text_name_header);
        editTextEmail = header.findViewById(R.id.text_email_header);

        editTextEmail.setText(email);
        editTextName.setText(name);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //
    @AfterPermissionGranted(101)
    private void initFile() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File folder = new File(getExternalCacheDir(), "TaskApp");
            folder.mkdirs();
            File file = new File(folder, "note.txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File imageFile = new File(folder, "image.png");
            downloadFile(imageFile);


        } else {
            EasyPermissions.requestPermissions(this, "Разрешить?", 101, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    //    @Override
//    public void sendInfo(Task task) {
//        startActivityForResult(new Intent(this, FormActivity.class ), 101);
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                ((HomeFragment) fragment.getChildFragmentManager().getFragments().get(0)).sortList();
                return true;
            case R.id.sign_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Выйти из аккаунта?")
                        .setMessage("Вы уверены?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();

                            }
                        });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toaster.show("Удаление отменено");
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            task = new Task();
            task = (Task) data.getSerializableExtra("key");

        }
    }

    private void info() {
        FirebaseFirestore.getInstance().collection("users").document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String image = documentSnapshot.getString("avatar");
                            Glide.with(MainActivity.this).load(image).circleCrop().into(imageViewHader);
                            imageViewHader.setImageURI(Uri.parse(image));
                            editTextName.setText(name);
                            editTextEmail.setText(email);
                        }
                    }
                });
    }

}


