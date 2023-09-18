package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdaptor extends RecyclerView.Adapter<CustomerAdaptor.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList<ToDoModel> taskList;
    int position;

    CustomerAdaptor(Activity activity,Context context,ArrayList<ToDoModel> toDoModelArrayList){
        this.context = context;
        this.activity = activity;
        this.taskList = toDoModelArrayList;
    }

    //categorization
    public void setCategorizedList(ArrayList<ToDoModel> categorizedList){
        this.taskList=categorizedList;
        notifyDataSetChanged();
    }


    //set filteredList
    public void setFilteredList(ArrayList<ToDoModel> filteredList){
        this.taskList=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view =layoutInflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdaptor.MyViewHolder holder,final int position) {
        holder.task_id.setText(String.valueOf(taskList.get(position).getId()));
        holder.task_title.setText(String.valueOf(taskList.get(position).getTitle()));
        holder.task_description.setText(String.valueOf(taskList.get(position).getDescription()));
        holder.task_due_date.setText(String.valueOf(taskList.get(position).getDueDate()));
        holder.centegory_name.setText(String.valueOf(taskList.get(position).getCategory()));
//        Log.i("my_error",taskList.get(position).getCategory());

        System.out.println(taskList.get(position).getCategory());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition(); // fetch the position here

                if (currentPosition == RecyclerView.NO_POSITION) {
                    // This happens if, for some reason, the item doesn't exist in the adapter anymore (e.g., it was removed).
                    return;
                }

                Intent intent = new Intent(context, UpdateTask.class);
                intent.putExtra("TaskID",taskList.get(currentPosition).getId());
                intent.putExtra("Title", String.valueOf(taskList.get(currentPosition).getTitle()));
                intent.putExtra("Description", String.valueOf(taskList.get(currentPosition).getDescription()));
                intent.putExtra("DueDate", String.valueOf(taskList.get(currentPosition).getDueDate()));
                intent.putExtra("Priority", String.valueOf(taskList.get(currentPosition).getPriority()));

                activity.startActivityForResult(intent,1);
            }
        });
        holder.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition(); // fetch the position here

                if (currentPosition == RecyclerView.NO_POSITION) {
                    // This happens if, for some reason, the item doesn't exist in the adapter anymore (e.g., it was removed).
                    return;
                }
                Intent intent = new Intent(context,TaskAttachment.class);
                intent.putExtra("TaskID",taskList.get(currentPosition).getId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView task_title,task_description,task_due_date,task_id,centegory_name;
        ImageButton attachment;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            task_id = itemView.findViewById(R.id.taskId);
            task_title = itemView.findViewById(R.id.taskTitle);
            task_description = itemView.findViewById(R.id.taskDescription);
            task_due_date = itemView.findViewById(R.id.taskDueDate);
            linearLayout = itemView.findViewById(R.id.mainLayout);
            attachment = itemView.findViewById(R.id.attachment);
            centegory_name=itemView.findViewById(R.id.categoryView);
        }
    }
}
