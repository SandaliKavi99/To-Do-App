package com.example.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.Model.ToDoModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private Context context;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String DUE_DATE = "due_date";
    private static final String IMGNAME = "image_name";
    private static final String CATEGORY = "category";
    private static final String IMG = "image";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT," + DESCRIPTION + " TEXT," + DUE_DATE + " TEXT," + CATEGORY + " TEXT," +IMG+" BLOB)";

    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imgInByte;

    private SQLiteDatabase db;
    public DatabaseHandler(Context context){
        super(context,NAME, null,VERSION);
        this.context = context;
    }

    //execute the table creation
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }

    // table is already exists then drop the table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void openDatabase(){
        db= this.getWritableDatabase();
    }

    //insert_task
    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put(TITLE, task.getTitle());
        cv.put(DESCRIPTION, task.getDescription());
        cv.put(DUE_DATE, task.getDueDate());
        cv.put(CATEGORY, task.getCategory());

        Long result =db.insert(TODO_TABLE,null,cv);
        if(result == -1){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Added Successfully",Toast.LENGTH_SHORT).show();
            System.out.println(task.getCategory());
        }
    }

//    view all data
    public Cursor readAllData(){
        String query = " SELECT * FROM " + TODO_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);

        }
        return cursor;
    }

    public void updateTaskDetail(ToDoModel toDoModel){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put(TITLE, toDoModel.getTitle());
        cv.put(DESCRIPTION, toDoModel.getDescription());
        cv.put(DUE_DATE, toDoModel.getDueDate());

        int result = db.update(TODO_TABLE,cv,"id=?",new String[]{String.valueOf(toDoModel.getId())});

        if(result == -1){
            Toast.makeText(context,"Failed to Update",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Update Successfully",Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImg(ToDoModel toDoModel){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap img = toDoModel.getImg();
        byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imgInByte = byteArrayOutputStream.toByteArray();

        cv.put(IMG, imgInByte);

        int result = db.update(TODO_TABLE,cv,"id=?",new String[]{String.valueOf(toDoModel.getId())});

        if(result == -1){
            Toast.makeText(context,"Failed to Attach",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Attach Successfully",Toast.LENGTH_SHORT).show();
        }
    }

//    delete task
    public void deleteTaskDetail(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TODO_TABLE,"id=?",new String[]{String.valueOf(taskId)});

        if(result == -1){
            Toast.makeText(context,"Failed to Delete",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Delete Successfully",Toast.LENGTH_SHORT).show();

        }
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(DESCRIPTION,status);
        db.update(TODO_TABLE, cv, ID+"=?", new String[]{String.valueOf(id)});
    }
    public void updateTask(int id, String task){
        ContentValues cv = new ContentValues();
        cv.put(TITLE, task);
        db.update(TODO_TABLE,cv, ID + "=?", new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(TODO_TABLE,ID+"=?", new String[] {String.valueOf(id)});
    }


}
