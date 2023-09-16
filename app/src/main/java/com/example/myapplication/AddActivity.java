package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.Utils.DatabaseHandler;

public class AddActivity extends AppCompatActivity {
    EditText titleInput,descriptionInput,dueDateInput,priorityInput;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        titleInput= findViewById(R.id.title);
        descriptionInput= findViewById(R.id.description);
        dueDateInput= findViewById(R.id.dueDate);
        priorityInput= findViewById(R.id.priority);
        addBtn= findViewById(R.id.save);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler(AddActivity.this);
                ToDoModel task = new ToDoModel();

                task.setTitle(titleInput.getText().toString().trim());
                task.setDescription(descriptionInput.getText().toString().trim());
                task.setDueDate(dueDateInput.getText().toString().trim());
                task.setPriority(Integer.parseInt(priorityInput.getText().toString().trim()));

                databaseHandler.insertTask(task);
                
            }
        });


    }
}