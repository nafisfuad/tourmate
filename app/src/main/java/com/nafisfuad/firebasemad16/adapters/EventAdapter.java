package com.nafisfuad.firebasemad16.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;


import com.nafisfuad.firebasemad16.R;
import com.nafisfuad.firebasemad16.databinding.EventRowBinding;
import com.nafisfuad.firebasemad16.pojos.TourmateEvent;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<TourmateEvent> eventList;
    private EventRowBinding binding;

    public EventAdapter(Context context, List<TourmateEvent> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.event_row, parent, false);
        return new EventViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        binding.rowEventName.setText(eventList.get(position).getEventName());
        binding.rowDepartureDate.setText(eventList.get(position).getDepartureDate());

        binding.rowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.event_row_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String eventId = eventList.get(position).getEventID();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventId);
                        switch (menuItem.getItemId()) {
                            case R.id.item_details:
                                Navigation.findNavController(holder.itemView).navigate(R.id.action_eventListFragment_to_eventDetailsFragment, bundle);
                                break;
                            case R.id.item_edit:
                                break;
                            case R.id.item_delete:
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}