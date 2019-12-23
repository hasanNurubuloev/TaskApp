package com.geektech.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.geektech.taskapp.room.TaskDao;
import com.geektech.taskapp.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText editDesc;
    HomeFragment homeFragment;
    Button save;
    private Task task = new Task();
    Intent intent = new Intent();

    TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDescription);
        save = findViewById(R.id.save);
        edit();


    }

    public void edit() {

        task = (Task) getIntent().getSerializableExtra("Task");
        if (task != null) {
            editTitle.setText(task.getTitle());
            editDesc.setText(task.getDescription());

        }
    }


    public void onClick(View view) {

        String title = editTitle.getText().toString().trim();
        String description = editDesc.getText().toString().trim();

        if (task != null) {
            task.setTitle(title);
            task.setDescription(description);
            App.getDatabase().taskDao().update(task);

        } else {
            task = new Task(title, description);

            App.getDatabase().taskDao().insert(task);
        }


        String title1 = editTitle.getText().toString();
        String desc = editDesc.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("title", title1);
        map.put("description", desc);
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("tasks")
                .document(userId)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toaster.show("Успешно");

                        } else {
                            Toaster.show("Ошибка");

                        }
                    }
                });

        finish();
    }



    private void getInfo() {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .document()
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String title = task.getResult().getString("title");
                            String desc = task.getResult().getString("description");
                            editTitle.setText(title);
                            editDesc.setText(desc);
                        }
                    }
                });
    }
}