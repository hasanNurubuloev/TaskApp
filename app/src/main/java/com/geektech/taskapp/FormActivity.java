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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText editDesc;
    HomeFragment homeFragment;
    Button save;
    String userId;

    private Task mTask = new Task();
    Intent intent = new Intent();

    TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDescription);
        save = findViewById(R.id.save);
//        userId = FirebaseAuth.getInstance().getUid();
        getInfo();
        edit();


    }

    public void edit() {

        mTask = (Task) getIntent().getSerializableExtra("Task");
        if (mTask != null) {
            editTitle.setText(mTask.getTitle());
            editDesc.setText(mTask.getDescription());

        }
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

    public void onClick(View view) {

        String title = editTitle.getText().toString().trim();
        String description = editDesc.getText().toString().trim();

        if (mTask != null) {
            mTask.setTitle(title);
            mTask.setDescription(description);
            updateTask();
        } else {
            mTask = new Task(title, description);
            addToFireStore();
        }


    }

    private void updateTask() {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .document(mTask.getId())
                .set(mTask)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            App.getDatabase().taskDao().update(mTask);
                            finish();
                        }
                    }
                });
    }

    private void addToFireStore() {
        FirebaseFirestore.getInstance().collection("tasks")
                .add(mTask)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            mTask.setId(task.getResult().getId());
                            App.getDatabase().taskDao().insert(mTask);
                            finish();
                        }
                    }
                });
    }


}