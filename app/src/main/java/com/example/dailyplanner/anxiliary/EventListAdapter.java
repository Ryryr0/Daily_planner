package com.example.dailyplanner.anxiliary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyplanner.R;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private List<Event> eventList;
    private OnEventClickListener onEventClickListener;

    public EventListAdapter(List<Event> eventList, OnEventClickListener onEventClickListener) {
        this.eventList = eventList;
        this.onEventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.imageEvent.setImageResource(event.getImageResId());
        holder.textEventTitle.setText(event.getTitle());
        holder.textEventDescription.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView imageEvent;
        TextView textEventTitle;
        TextView textEventDescription;
        Button buttonAddToTask;
        Button buttonDelete;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageEvent = itemView.findViewById(R.id.image_event);
            textEventTitle = itemView.findViewById(R.id.text_event_title);
            textEventDescription = itemView.findViewById(R.id.text_event_description);
            buttonAddToTask = itemView.findViewById(R.id.button_add_to_task);
            buttonDelete = itemView.findViewById(R.id.button_delete);

            buttonAddToTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEventClickListener.onAddToTaskClick(getAdapterPosition());
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEventClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnEventClickListener {
        void onAddToTaskClick(int position);
        void onDeleteClick(int position);
    }
}