package com.geektech.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.geektech.taskapp.room.TaskDao;
import com.geektech.taskapp.ui.home.HomeFragment;

public class FormActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText desc;
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
        desc = findViewById(R.id.editDescription);
        save = findViewById(R.id.save);
        edit();


    }

    public void edit() {

        task = (Task) getIntent().getSerializableExtra("Task");
        if (task != null) {
            editTitle.setText(task.getTitle());
            desc.setText(task.getDescription());

        }


    }


    public void onClick(View view) {

        String title = editTitle.getText().toString().trim();
        String description = desc.getText().toString().trim();

        if (task != null) {
            task.setTitle(title);
            task.setDescription(description);
            App.getDatabase().taskDao().update(task);

        } else {
            task = new Task(title, description);
            App.getDatabase().taskDao().insert(task);
       /* intent.putExtra("key", task);
        setResult(RESULT_OK, inte
        finish();

    }


}

        */
        }
        finish();
    }
}