package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Adapter.ToDoAdapter;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView tasksRecyclerView;
    private CustomerAdaptor customerAdaptor;
    private ArrayList<ToDoModel> taskList;
    private FloatingActionButton fab;
    private DatabaseHandler db;
    private Context context;
    private SearchView searchView;
    private Button allBtn;
    private Button category1;
    private Button category2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        db=new DatabaseHandler(MainActivity.this);
        db.openDatabase();

        taskList = new ArrayList<>();

        //get search view as a variable
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });



        //get all categories
        allBtn=findViewById(R.id.btnall);
        category1=findViewById(R.id.btncat1);
        category2=findViewById(R.id.btncat2);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorize("all");
            }
        });

        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorize("Category1");

            }
        });

        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorize("Category2");
            }
        });





        //get recyclerview as a variable
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageButton imgBtn = findViewById(R.id.faqBtn);

        fab=findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });

        imgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,FaqActivity.class);
                startActivity(intent);
            }
        });

        viewAll();
        customerAdaptor = new CustomerAdaptor(MainActivity.this,this,taskList);
        tasksRecyclerView.setAdapter(customerAdaptor);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    private void categorize(String text) {
        ArrayList<ToDoModel> categorizedList = new ArrayList<>();


        if(text=="all"){
            categorizedList=taskList;

        }
        if(text=="Category1"){
            for(ToDoModel task : taskList){
                if(task.getCategory().contains("Category1")){
                    categorizedList.add(task);
                }
            }

        }

        if(text=="Category2"){
            for(ToDoModel task : taskList){
                Log.i("tag1",task.getCategory());

                if(task.getCategory().contains("Category2")){
                    categorizedList.add(task);
                }
            }

        }

        customerAdaptor.setCategorizedList(categorizedList);
    }

    //filterList
    private void filterList(String text) {
        ArrayList<ToDoModel> filteredList = new ArrayList<>();
        for(ToDoModel task : taskList){
             if(task.getTitle().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(task);
             }
             if(task.getDueDate().contains(text)){
                   filteredList.add(task);
             }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show();
        }else{
            customerAdaptor.setFilteredList(filteredList);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){

            recreate();

        }
    }

    //    display all task
    void viewAll(){
        Cursor cursor = db.readAllData();
        String today;
        if (cursor.getCount() == 0){
            Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                ToDoModel task = new ToDoModel();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setDescription(cursor.getString(2));
                task.setDueDate(cursor.getString(3));
                task.setPriority(Integer.parseInt(cursor.getString(4)));


                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = format.parse(task.getDueDate());
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Month is 0-based, so add 1
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                   today= (month<10)?  year +"-0"+month+"-"+ day : year +"-"+month+"-"+day;

                   if(task.getDueDate().equals(today)){
                       createNotification();
                   }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                taskList.add(task);
            }
        }
    }
    public void handleDialogClose(DialogInterface dialog){
        Collections.reverse(taskList);

    }

//notification channel creation
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
          CharSequence name = "ToDoRemainderChannel";
          String description = "Channal for ToDo Reminder";
          int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NotifyMe",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
//    notification creation
    private void createNotification(){
        createNotificationChannel();
        Intent intent = new Intent(MainActivity.this,NotificationBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,0,pendingIntent);
    }
}
