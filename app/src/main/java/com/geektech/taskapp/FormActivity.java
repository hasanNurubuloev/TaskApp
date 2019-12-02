package com.geektech.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FormActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText editDescription;
    Button save;
    private Task task = new Task();
    Intent intent = new Intent();

    ISave iSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        save = findViewById(R.id.save);


//            task.setTitle(editTitle.getText().toString().trim());
//            task.setDescription(editDescription.getText().toString().trim());
    }

    public void onClick(View view) {
        String title = editTitle.getText().toString().trim();
        String description =editDescription.getText().toString().trim() ;
        task.setTitle(title);
        task.setDescription(description);

        intent.putExtra("key", task);
        setResult(RESULT_OK, intent);
        finish();
    }


}
