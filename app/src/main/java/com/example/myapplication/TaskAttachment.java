package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.Utils.DatabaseHandler;

import java.io.IOException;

public class TaskAttachment extends AppCompatActivity {
    Button attachment,select;
    ImageView imageView;
    private Uri imageUrl;
    private Bitmap imageToStore;
    private DatabaseHandler ddatabaseHandler = new DatabaseHandler(this);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_attachment);



        attachment = findViewById(R.id.imageSave);
        imageView = findViewById(R.id.imageView);

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (imageView.getDrawable() != null){
                        ToDoModel task = new ToDoModel();
                        Bundle extras = getIntent().getExtras();
                        task.setId(extras.getInt("TaskID"));
                        task.setImg(imageToStore);
                        ddatabaseHandler.uploadImg(task);
                    }

                }
                catch (Exception e){
                    Toast.makeText(TaskAttachment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("my_error",e.getMessage());
                }

            }
        });
    }
    public void chooseImage(View objView){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUrl = data.getData();

            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUrl);
                imageView.setImageBitmap(imageToStore);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}