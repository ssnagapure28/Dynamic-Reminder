package com.example.projectmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;

    private Context context;
    private taskDAO dao;
    private FragmentActivity fragmentActivity;

    public TaskAdapter(List<Task> taskList, Context context, FragmentActivity fragmentActivity) {
        this.taskList = taskList;
        this.context = context;
        this.fragmentActivity=fragmentActivity;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
       return new TaskViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        //int reversedPosition = getItemCount() - 1 - position;
        Task task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.apprxtimeTextView.setText("Approx time required: "+task.getappx_time());
        holder.dateInitTextView.setText(task.getDate());
        holder.timeTextView.setText(task.getTime());
        System.out.println("Task Id:"+task.getKey());

        holder.bind(task);


        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(task);
            }
        });

        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(task.getKey());
                navigateToAddTaskFragment(task);
            }
        });




    }

    public int getItemCount() {
        return taskList.size();
    }
    private void showDeleteConfirmationDialog(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(task);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteTask(Task task) {
        dao=new taskDAO();
        System.out.println("Task needs to delete:"+task.getTitle());
        dao.deleteTask(task.getKey(), success -> {
            if (success) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context,"Task deleted successfully", Toast.LENGTH_SHORT).show();
                    navigateToHomeFragment();

                });
            } else {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void navigateToHomeFragment() {
        // Implement the navigation logic to go back to HomeFragment
        // Example: use a FragmentManager to replace the current fragment with HomeFragment
        // Create an instance of the HomeFragment
        HomeFragment homeFragment = new HomeFragment();

        // Get the FragmentManager
        FragmentManager fragmentManager =fragmentActivity.getSupportFragmentManager();

        // Replace the current fragment with HomeFragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
    }

    private void navigateToAddTaskFragment(Task task) {
        // Implement the navigation logic to go back to HomeFragment
        // Example: use a FragmentManager to replace the current fragment with HomeFragment
        // Create an instance of the HomeFragment
        System.out.println("inside navigateToAddTaskFragment:"+task.getKey());
        AddTaskFragment addTaskFragment = new AddTaskFragment(task,true);

        // Get the FragmentManager
        FragmentManager fragmentManager =fragmentActivity.getSupportFragmentManager();

        // Replace the current fragment with HomeFragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addTaskFragment)
                .commit();
    }





    public static class TaskViewHolder extends RecyclerView.ViewHolder {
       TextView titleTextView;
        TextView monthTextView;
       TextView apprxtimeTextView;
       TextView dateTextView;
       TextView yearTextView;
       TextView dateInitTextView;
       TextView timeTextView;
       ImageView deleteImageView;
       ImageView editImageView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.titleTextView);
            apprxtimeTextView=itemView.findViewById(R.id.apprxtimeTextView);
            dateInitTextView=itemView.findViewById(R.id.dateInitTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            timeTextView=itemView.findViewById(R.id.timeTextView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            editImageView = itemView.findViewById(R.id.editImageView);
            monthTextView = itemView.findViewById(R.id.monthTextView);

        }

        public void bind(Task task) {
            String month = getMonthString(task.getMonth());
            String day = String.valueOf(task.getDay());
            String date = String.valueOf(task.getDate());
            String year = String.valueOf(task.getYear());

            monthTextView.setText(month);
            dateTextView.setText(day);
            dateInitTextView.setText(date);
            yearTextView.setText(year);
            titleTextView.setText(task.getTitle());

            timeTextView.setText(task.getTime());
        }

        private String getMonthString(int month) {
            String[] monthArray = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            if (month >= 1 && month <= 12) {
                return monthArray[month - 1];
            } else {
                return "";
            }
        }
    }
}