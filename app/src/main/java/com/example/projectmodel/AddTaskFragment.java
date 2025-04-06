package com.example.projectmodel;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.textfield.TextInputEditText;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskFragment extends Fragment {

    Task editTask;
    boolean isEdit;
    AddTaskFragment()
    {

    }
    AddTaskFragment(Task task, boolean Edit)
    {
        this.editTask=task;
        this.isEdit=Edit;

    }
    taskDAO dao;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    // Declare variables to store selected date and time
    private int year, month, day, hour, minute;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the views
        TextInputEditText titleEditText = view.findViewById(R.id.titleEditText);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        ImageView calendarImageView = view.findViewById(R.id.calendarImageView);
        TextView timeTextView = view.findViewById(R.id.timeTextView);
        ImageView clockImageView = view.findViewById(R.id.clockImageView);
        TextInputEditText contentEditText = view.findViewById(R.id.contentEditText);
        Button saveButton = view.findViewById(R.id.saveButton);

        if(editTask!=null)
        {
            titleEditText.setText(editTask.getTitle());
            dateTextView.setText(editTask.getDate());
            String time=editTask.getTime();
            time=time.substring(0, time.length() - 3);
            timeTextView.setText(time);
        }

        // Set click listener for the calendar image view
        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                final Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);


                // Create a DatePickerDialog to allow the user to select a date
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Create a Calendar object with the selected date
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        // Get the current date
                        Calendar currentDate = Calendar.getInstance();

                        // Compare the selected date with the current date
                        if (selectedDate.before(currentDate)) {
                            // Selected date is in the past, show an error message
                            Toast.makeText(getActivity(), "Please select a date in the future", Toast.LENGTH_SHORT).show();
                        } else {
                            // Store the selected date in variables
                            AddTaskFragment.this.year = year;
                            month = monthOfYear;
                            day = dayOfMonth;

                            // Display the selected date
                            String selectedDateStr = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            dateTextView.setText(selectedDateStr);
                        }
                    }
                }, currentYear, currentMonth, currentDay);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        // Set click listener for the clock image view
        clockImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time
                final Calendar currentTime = Calendar.getInstance();

                // Create a TimePickerDialog to allow the user to select a time
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Create a calendar object with the selected time
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);

                        // Compare the selected time with the current time
//                        if (selectedTime.after(currentTime)) {
                            // The selected time is in the future
                            // Store the selected time in variables

                            hour = hourOfDay;
                            AddTaskFragment.this.minute = minute;

                            // Display the selected time
                            String selectedTimeStr = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                            timeTextView.setText(selectedTimeStr);
//                        } else {
                            // The selected time is in the past or too close to the current time
                          //  Toast.makeText(getActivity(), "Please select a time in the future", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true);

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });



        // Set click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(titleEditText.getText().toString()+":"+dateTextView.getText().toString()+":"+timeTextView.getText().toString()+":"+contentEditText.getText().toString());
                String title = titleEditText.getText().toString().trim();
                String date = dateTextView.getText().toString().trim();
                String time = timeTextView.getText().toString().trim();
                String content = contentEditText.getText().toString().trim();

                // Validate the input
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
                    // Show error message if title, date, or time is empty
                    Toast.makeText(getActivity(), "Please enter both title, date and time", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if content exceeds the maximum number of lines
                    int lineCount = contentEditText.getLineCount();
                    if (lineCount > 3) {
                        // Show an error message to the user
                        Toast.makeText(getActivity(), "Content should not exceed 3 lines/30 words", Toast.LENGTH_SHORT).show();
                    }else{
                        // Get the current time
                        final Calendar currentTime = Calendar.getInstance();

                        // Get the selected date and time
                        Calendar selectedDateTime = Calendar.getInstance();
                        selectedDateTime.set(year, month, day, hour, minute);

                        // Calculate the time difference between the selected time and current time
                        long timeDifferenceInMillis = selectedDateTime.getTimeInMillis() - currentTime.getTimeInMillis();
                        int timeDifferenceInMinutes = (int) (timeDifferenceInMillis / (60 * 1000));

                        // Check if the selected time is at least six minutes later
                        if (timeDifferenceInMinutes < 6) {
                            // Show an error message to the user
                            Toast.makeText(getActivity(), "Please select a time at least six minutes later", Toast.LENGTH_SHORT).show();
                        } else {
                            // Save the task to Firebase
                            saveTaskToAWS(title, date, time, content);
                        }
                    }
                }
            }
        });
    }

    private void saveTaskToAWS(String title, String date, String time, String content) {

        dao=new taskDAO();

        // Create a Task object
        Task task = new Task();
        task.setTitle(title);
        task.setDate(date);
        task.setTime_time(time);
        task.setNear_activity();

        if(isEdit)
        {
            editTask.setTitle(title);
            editTask.setDate(date);
            editTask.setTime_time(time);
            editTask.setNear_activity();

            dao.editTask(editTask, success -> {
                if (success) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getContext(), "Task edited successfully", Toast.LENGTH_SHORT).show();
                        navigateToHomeFragment();

                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getContext(), "Failed to edit task", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
        else {
            dao.insertTask(task, success -> {
                if (success) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getContext(), "Task inserted successfully", Toast.LENGTH_SHORT).show();
                        navigateToHomeFragment();

                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getContext(), "Failed to insert task", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }


        // For the reminder...
        // Create a Calendar object with the selected date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        calendar.set(Calendar.SECOND, 0); //ensure that the notification will trigger at the exact minute, without any seconds delay.

        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Calculate the time difference between the current time and the selected time
        long timeDifference = calendar.getTimeInMillis() - currentTimeMillis;

        // Create an Intent to trigger the ReminderBroadcastReceiver
        Intent reminderIntent = new Intent(getActivity(), ReminderBroadcastReceiver.class);
        reminderIntent.putExtra("title", title);
        reminderIntent.putExtra("content", content);

        // Create a PendingIntent to wrap the reminderIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, reminderIntent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // Schedule the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, currentTimeMillis + timeDifference, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentTimeMillis + timeDifference, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, currentTimeMillis + timeDifference, pendingIntent);
        }

    }

    private void navigateToHomeFragment() {
        // Implement the navigation logic to go back to HomeFragment
        // Example: use a FragmentManager to replace the current fragment with HomeFragment
        // Create an instance of the HomeFragment
        HomeFragment homeFragment = new HomeFragment();

        // Get the FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Replace the current fragment with HomeFragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
    }
}
