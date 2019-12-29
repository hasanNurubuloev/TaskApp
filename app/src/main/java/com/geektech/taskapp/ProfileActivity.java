package com.geektech.taskapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
/*
1.Отправлять email
2.Task отправляем в Firebase
3.Сохраняем имя и почту локально(SharedPref)*/

public class ProfileActivity extends AppCompatActivity {
    EditText editName, editEmail;
    ImageView imageView;
    String userId;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.edit_email);
        imageView = findViewById(R.id.imageView);
        userId = FirebaseAuth.getInstance().getUid();
   //     getInfo();
        getInfo2();
    }

    private void getInfo2() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            editName.setText(name);
                            editEmail.setText(email);
                        }
                    }
                });
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        storage.child("avatars/" + userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileActivity.this).load(uri).circleCrop().into(imageView);
            }
        });
    }

//    private void getInfo() {
//        FirebaseFirestore.getInstance()
//                .collection("users")
//                .document()
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            String name = task.getResult().getString("name");
//                            String email = task.getResult().getString("email");
//                            editName.setText(name);
//                            editEmail.setText(email);
//                        }
//                    }
//                });
//    }

    public void onClick(View view) {
        if (imageUri != null) uploadImage(null);
        else save(null);

    }



    private void uploadImage(final Uri uri) {
        String userId = FirebaseAuth.getInstance().getUid();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("avatars/image1.jpg");
        UploadTask task = reference.putFile(imageUri);
        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    save(String.valueOf(uri));
                }
            }
        });
    }



    private void save (String avatarUri){
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("avatar", avatarUri);
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toaster.show("Успешно");
                            finish();
                        } else {
                            Toaster.show("Ошибка");
                        }
                    }
                });
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.putString("name", name);
        editor.apply();
    }

    public void onClickImage(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode ==RESULT_OK && requestCode == 101 && data!= null){
            imageUri = data.getData();
            uploadImage(data.getData());
            Glide.with(this).load(data.getData()).circleCrop().into(imageView);
        }
    }
}
