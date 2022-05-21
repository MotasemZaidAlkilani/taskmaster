package com.example.taskmaster.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.example.taskmaster.R;

import java.util.List;

public class viewAdapter extends RecyclerView.Adapter<viewAdapter.CustomViewHolder> {
    public static List<Task> dataList;
    CustomClickListener listener;



    @NonNull
    @Override
    public viewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.task_item_layout, parent, false);
        return new CustomViewHolder(listItemView, listener);

    }

    public viewAdapter(List<Task> dataList, CustomClickListener listener) {
        this.dataList=dataList;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull viewAdapter.CustomViewHolder holder, int position) {
        Task task=dataList.get(position);
        holder.title.setText(task.getTitle());
       holder.state.setText(task.getStatus());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

   static class CustomViewHolder extends RecyclerView.ViewHolder {
TextView title;
TextView state;
        CustomClickListener listener;
        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);
            this.listener = listener;
            title = itemView.findViewById(R.id.title);
            state = itemView.findViewById(R.id.state);
            itemView.setOnClickListener(view -> this.listener.onTaskDataItemClicked(getAdapterPosition()));

        }
    }
    public interface CustomClickListener {
        void onTaskDataItemClicked(int position);
    }
}