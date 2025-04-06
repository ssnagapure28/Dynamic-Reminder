package com.example.projectmodel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton addTaskButton;
   // private DatabaseReference tasksRef;
   // private ValueEventListener tasksValueEventListener;
    private List<Task> taskList2;
    taskDAO dao;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addTaskButton = view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(this);

        // Initialize the RecyclerView and its adapter
        recyclerView = view.findViewById(R.id.recyclerView);
        taskList2 = new ArrayList<>();
        dao=new taskDAO();
        dao.getAllTasks(taskList -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                taskList2 = taskList;
                taskAdapter = new TaskAdapter(taskList, getContext(), requireActivity());

                // Set the adapter for the RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                recyclerView.setAdapter(taskAdapter);
            });


        });


        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addTaskButton) {
            // Open the AddTaskFragment
            AddTaskFragment addTaskFragment = new AddTaskFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addTaskFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}