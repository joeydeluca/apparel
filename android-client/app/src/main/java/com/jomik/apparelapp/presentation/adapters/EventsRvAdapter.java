package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.activities.EditEventActivity;
import com.jomik.apparelapp.presentation.activities.ViewEventOutfitsActivity;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class EventsRvAdapter extends RecyclerView.Adapter<EventsRvAdapter.PersonViewHolder>{

    private final List<Event> events;
    private Context context;

    public EventsRvAdapter(List<Event> events) {
        this.events = events;
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event_list, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, final int i) {
        holder.title.setText(events.get(i).getTitle());
        holder.location.setText(events.get(i).getLocation());

        final PopupMenu popup = new PopupMenu(context, holder.btnMenu);

        if(events.get(i).getOwnerUuid().equals(AuthenticationManager.getAuthenticatedUser(context).getUuid())) {
            popup.getMenu().add(1, R.id.menu_manage, 1, "Manage");
        }
        popup.getMenu().add(1, R.id.menu_leave, 1, "Leave");

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_manage:
                        Intent intent = new Intent(context, EditEventActivity.class);
                        intent.putExtra("id", events.get(i).getId());
                        context.startActivity(intent);
                        break;
                    case R.id.menu_leave:
                        if (!events.get(i).getOwnerUuid().equals(AuthenticationManager.getAuthenticatedUser(context))) {
                            events.remove(events.get(i));
                            notifyItemRemoved(i);
                        }
                        events.get(i).getAttendees().remove(AuthenticationManager.getAuthenticatedUser(context));
                        //eventsRepository.save(events.get(i));
                        Toast.makeText(context, "You have left the event", Toast.LENGTH_LONG).show();
                        break;
                }

                return true;
            }
        });
        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewEventOutfitsActivity.class);
                intent.putExtra("id", events.get(i).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView location;
        ImageView btnMenu;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            location = (TextView)itemView.findViewById(R.id.location);
            btnMenu = (ImageView) itemView.findViewById(R.id.btnMenu);
        }
    }
}
