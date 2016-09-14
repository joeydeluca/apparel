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

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.EventGuest;
import com.jomik.apparelapp.domain.entities.EventType;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;
import com.jomik.apparelapp.presentation.activities.EditEventActivity;
import com.jomik.apparelapp.presentation.activities.ViewEventOutfitsActivity;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class EventsRvAdapter extends RecyclerView.Adapter<EventsRvAdapter.EventViewHolder>{

    private final List<Event> events;
    private Context context;
    private EventType mEventType;

    public EventsRvAdapter(List<Event> events, EventType eventType) {
        this.events = events;
        this.mEventType = eventType;
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
        final Event event = events.get(i);

        holder.title.setText(event.getTitle());
        holder.description.setText(event.getDescription());
        holder.ownerText.setText("Created by " + event.getOwner().getName());
        if(EventType.EVENT == mEventType) {
            holder.date.setText(SqlHelper.getDisplayDateFromEvent(event));
            holder.location.setText(event.getLocation());
        } else {
            holder.location.setVisibility(View.GONE);
        }

        if(event.getPhoto() != null) {
            ImageHelper.setImageUri(holder.eventPhoto, event.getPhoto().getPhotoPath());
        } else {
            holder.eventPhoto.setVisibility(View.GONE);
        }

        ImageHelper.setFacebookProfileImageUri(holder.profilePhoto, event.getOwner().getFacebookId());

        final PopupMenu popup = new PopupMenu(context, holder.btnMenu);

        if(event.getOwner().getUuid().equals(AuthenticationManager.getAuthenticatedUser(context).getUuid())) {
            popup.getMenu().add(1, R.id.menu_manage, 1, "Manage");
            popup.getMenu().add(1, R.id.menu_delete, 10, "Delete");
        }

        EventGuest myEventGuest = null;
        for(EventGuest eventGuest : event.getEventGuests()) {
            if(!eventGuest.isMarkedForDelete() &&
                    AuthenticationManager.getAuthenticatedUser(context).getUuid().equals(eventGuest.getGuest().getUuid())) {
                myEventGuest = eventGuest;
                break;
            }
        }

        if(myEventGuest != null) {
            popup.getMenu().add(1, R.id.menu_leave, 2, "Leave");
        }

        final OrmLiteSqlHelper helper = new OrmLiteSqlHelper(context);

        final EventGuest finalMyEventGuest = myEventGuest;
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_manage:
                        Intent intent = new Intent(context, EditEventActivity.class);
                        intent.putExtra("id", event.getUuid());
                        context.startActivity(intent);
                        break;
                    case R.id.menu_leave:
                        finalMyEventGuest.setMarkedForDelete(true);

                        try {
                            helper.getEventGuestDao().update(finalMyEventGuest);

                            // Remove event from screen only if current user is not the owner
                            if (!event.getOwner().getUuid().equals(AuthenticationManager.getAuthenticatedUser(context).getUuid())) {
                                events.remove(event);
                                notifyItemRemoved(i);
                            }

                            // Remove link from menu
                            popup.getMenu().removeItem(R.id.menu_leave);

                            Toast.makeText(context, "You have left the event", Toast.LENGTH_LONG).show();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        break;
                    case R.id.menu_delete:
                        // TODO: Are you sure prompt

                        event.setMarkedForDelete(true);

                        try {
                            helper.getEventDao().update(event);

                            events.remove(event);
                            notifyItemRemoved(i);
                            Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

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
                try {
                    Intent intent = new Intent(context, ViewEventOutfitsActivity.class);
                    intent.putExtra("eventId", event.getUuid().toString());
                    intent.putExtra("startDate", event.getStartDate());
                    intent.putExtra("endDate", event.getEndDate());
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        TextView description;
        TextView date;
        ImageView btnMenu;
        SimpleDraweeView eventPhoto;
        SimpleDraweeView profilePhoto;
        TextView ownerText;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            location = (TextView)itemView.findViewById(R.id.location);
            description = (TextView)itemView.findViewById(R.id.description);
            btnMenu = (ImageView) itemView.findViewById(R.id.btnMenu);
            eventPhoto = (SimpleDraweeView) itemView.findViewById(R.id.my_image_view);
            profilePhoto = (SimpleDraweeView) itemView.findViewById(R.id.imgUser);
            ownerText = (TextView) itemView.findViewById(R.id.txtOwner);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
