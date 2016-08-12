package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;
import com.jomik.apparelapp.presentation.activities.EditEventActivity;
import com.jomik.apparelapp.presentation.activities.ViewEventOutfitsActivity;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class EventsRvAdapter extends RecyclerView.Adapter<EventsRvAdapter.EventViewHolder>{

    private final List<Event> events;
    private Context context;

    public EventsRvAdapter(List<Event> events) {
        this.events = events;
    }


    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event_list, viewGroup, false);
        EventViewHolder pvh = new EventViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, final int i) {
        Event event = events.get(i);

        holder.title.setText(event.getTitle());
        holder.location.setText(event.getLocation());
        holder.ownerText.setText("Created by " + event.getOwnerName());

        ImageHelper.setImageUri(holder.eventPhoto, event.getPhotoPath(), event.getPhotoUuid());
        ImageHelper.setFacebookProfileImageUri(holder.profilePhoto, event.getOwnerFacebookId());

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
                        if (!events.get(i).getOwnerUuid().equals(AuthenticationManager.getAuthenticatedUser(context).getUuid())) {
                            events.remove(events.get(i));
                            notifyItemRemoved(i);
                        }
                        //events.get(i).getAttendees().remove(AuthenticationManager.getAuthenticatedUser(context));
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
                intent.putExtra("event", events.get(i));
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

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView location;
        ImageView btnMenu;
        SimpleDraweeView eventPhoto;
        SimpleDraweeView profilePhoto;
        TextView ownerText;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            location = (TextView)itemView.findViewById(R.id.location);
            btnMenu = (ImageView) itemView.findViewById(R.id.btnMenu);
            eventPhoto = (SimpleDraweeView) itemView.findViewById(R.id.my_image_view);
            profilePhoto = (SimpleDraweeView) itemView.findViewById(R.id.imgUser);
            ownerText = (TextView) itemView.findViewById(R.id.txtOwner);
        }
    }
}
