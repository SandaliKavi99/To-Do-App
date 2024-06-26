package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.Utils.DatabaseHandler;
import com.google.android.material.datepicker.MaterialDatePicker;

public class AddActivity extends AppCompatActivity {
    EditText titleInput,descriptionInput,dueDateInput,priorityInput;
    Button addBtn;
    String[] items = {"Urgent","Later"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item:" + item, Toast.LENGTH_SHORT).show();
            }
        });

        titleInput = findViewById(R.id.title);
        descriptionInput = findViewById(R.id.description);
        dueDateInput = findViewById(R.id.dueDate);
        addBtn = findViewById(R.id.save);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler(AddActivity.this);
                ToDoModel task = new ToDoModel();

                task.setTitle(titleInput.getText().toString().trim());
                task.setDescription(descriptionInput.getText().toString().trim());
                task.setDueDate(dueDateInput.getText().toString().trim());
                task.setCategory(autoCompleteTextView.getText().toString().trim());
                Log.i("print", autoCompleteTextView.getText().toString().trim());
                databaseHandler.insertTask(task);
//                System.out.println(task.getCategory());

            }
        });


    }


}