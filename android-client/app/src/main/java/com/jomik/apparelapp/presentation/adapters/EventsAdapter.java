package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class EventsAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final List<Event> events;

    public EventsAdapter(Context context, List<Event> events) {
        super(context, -1, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_event_list, parent, false);

        Event event = events.get(position);

        TextView textView = (TextView) rowView.findViewById(R.id.title);
        textView.setText(event.getTitle());

        textView = (TextView) rowView.findViewById(R.id.location);
        textView.setText(event.getLocation());

        SimpleDraweeView draweeView = (SimpleDraweeView) rowView.findViewById(R.id.my_image_view);
        ImageHelper.setImageUri(draweeView, event.getPhoto().getPhotoPathSmall());

        rowView.setTag(events.get(position));

        return rowView;
    }
}
