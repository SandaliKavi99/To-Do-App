package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.Utils.DatabaseHandler;

public class UpdateTask extends AppCompatActivity {
    EditText task_title,task_description,task_due_date,task_priority;
    Button editBtn,deleteBtn;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        task_title = findViewById(R.id.title2);
        task_description = findViewById(R.id.description2);
        task_due_date = findViewById(R.id.dueDate2);
        task_priority = findViewById(R.id.priority2);
        editBtn = findViewById(R.id.edit);
        deleteBtn = findViewById(R.id.delete);

        Bundle extras = getIntent().getExtras();
        task_title.setText(extras.getString("Title"));
        task_description.setText(extras.getString("Description"));
        task_due_date.setText(extras.getString("DueDate"));
        task_priority.setText(extras.getString("Priority"));

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler(UpdateTask.this);
                ToDoModel task = new ToDoModel();

                // Assuming you want to get the ID from the extras (you need to set it in the intent previously)
                task.setId(extras.getInt("TaskID"));

                task.setTitle(task_title.getText().toString().trim());
                task.setDescription(task_description.getText().toString().trim());
                task.setDueDate(task_due_date.getText().toString().trim());



                try {
                    task.setPriority(Integer.parseInt(task_priority.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    Toast.makeText(UpdateTask.this, "Invalid priority value!", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseHandler.updateTaskDetail(task);

                // Feedback for the user
                Toast.makeText(UpdateTask.this, "Task updated!", Toast.LENGTH_SHORT).show();

                // Finish the activity (you might also want to refresh the list in the previous activity)
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler(UpdateTask.this);
                ToDoModel task = new ToDoModel();

                // Assuming you want to get the ID from the extras (you need to set it in the intent previously)
                task.setId(extras.getInt("TaskID"));

                databaseHandler.deleteTaskDetail(task.getId());

                // Finish the activity (you might also want to refresh the list in the previous activity)
                finish();
            }
        });



    }

}
