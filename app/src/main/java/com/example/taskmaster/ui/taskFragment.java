package com.example.taskmaster.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.taskmaster.R;

public class taskFragment extends Fragment {
    private static final String ARG_PARAM1 = "title";

    // TODO: Rename and change types of parameters
    private String title;

    public taskFragment() {
        // Required empty public constructor
    }

    public static taskFragment newInstance(String title, String body) {
        taskFragment fragment = new taskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.task_item_layout, container, false);
    }
}
