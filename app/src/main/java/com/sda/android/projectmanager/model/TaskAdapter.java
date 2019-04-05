package com.sda.android.projectmanager.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sda.android.projectmanager.R;

public class TaskAdapter extends FirebaseRecyclerAdapter<Task, TaskAdapter.ViewHolder> {


    public TaskAdapter(@NonNull FirebaseRecyclerOptions<Task> options) {
        super(options);
    }

    public interface RVItemSelected{
        void onRecycleViewItemSelected(String taskKey, Task task);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        TextView tvTaskTitle;
        ImageView ivTaskStatus;
        LinearLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            ivTaskStatus = itemView.findViewById(R.id.ivTaskStatus);
            card = itemView.findViewById(R.id.card);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final Task model) {

        holder.tvTaskTitle.setText(model.getTitle());


        switch (model.getStatus()){
            case "todo":
                holder.ivTaskStatus.setImageResource(R.drawable.todo);
                holder.card.setBackgroundResource(R.drawable.bg_card_todo);
                break;
            case "in-progress":
                holder.ivTaskStatus.setImageResource(R.drawable.progress);
                holder.card.setBackgroundResource(R.drawable.bg_card_progress);
                break;
            case "done":
                holder.ivTaskStatus.setImageResource(R.drawable.done);
                holder.card.setBackgroundResource(R.drawable.bg_card_done);
                break;
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskKey = getRef(position).getKey();

                RVItemSelected homeActivity = (RVItemSelected) v.getContext();
                homeActivity.onRecycleViewItemSelected(taskKey, model);
            }
        });
    }
}
