package com.jomik.apparelapp.presentation.adapters;

import android.content.ContentProviderClient;
import android.content.ContentUris;
import android.content.ContentValues;
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
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
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
        final Event event = events.get(i);

        holder.title.setText(event.getTitle());
        holder.location.setText(event.getLocation());
        holder.description.setText(event.getDescription());
        holder.ownerText.setText("Created by " + event.getOwner().getName());

        String displayDate = SqlHelper.dateFormatForDb.format(event.getStartDate());
        if(event.getEndDate() != null && !event.getEndDate().equals(event.getStartDate())) {
            displayDate = displayDate + " - " + event.getEndDate();
        }
        holder.date.setText(displayDate);

        ImageHelper.setImageUri(holder.eventPhoto, event.getPhoto().getPhotoPath());
        ImageHelper.setFacebookProfileImageUri(holder.profilePhoto, event.getOwner().getFacebookId());

        final PopupMenu popup = new PopupMenu(context, holder.btnMenu);

        if(events.get(i).getOwnerUuid().equals(AuthenticationManager.getAuthenticatedUser(context).getUuid())) {
            popup.getMenu().add(1, R.id.menu_manage, 1, "Manage");
            popup.getMenu().add(1, R.id.menu_delete, 10, "Delete");
        }
        popup.getMenu().add(1, R.id.menu_leave, 2, "Leave");

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_manage:
                        Intent intent = new Intent(context, EditEventActivity.class);
                        intent.putExtra("id", event.getId());
                        context.startActivity(intent);
                        break;
                    case R.id.menu_leave:
                        if (!event.getOwnerUuid().equals(AuthenticationManager.getAuthenticatedUser(context).getUuid())) {
                            events.remove(event);
                            notifyItemRemoved(i);
                        }
                        //events.get(i).getAttendees().remove(AuthenticationManager.getAuthenticatedUser(context));
                        //eventsRepository.save(events.get(i));
                        Toast.makeText(context, "You have left the event", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.menu_delete:
                        // TODO: Are you sure prompt
                        ContentValues values = new ContentValues();
                        values.put(ApparelContract.CommonColumns.MARKED_FOR_DELETE, 1);
                        values.put(ApparelContract.CommonColumns.VERSION, event.getVersion() + 1);
                        context.getContentResolver().update(ContentUris.withAppendedId(ApparelContract.Items.CONTENT_URI, event.getId()), values, null, null);

                        events.remove(event);
                        notifyItemRemoved(i);
                        Toast.makeText(context, "Event deleted", Toast.LENGTH_LONG).show();
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
                    intent.putExtra("eventId", event.getId().toString());
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
