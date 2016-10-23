package com.jomik.apparelapp.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.j256.ormlite.stmt.QueryBuilder;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.EventGuest;
import com.jomik.apparelapp.domain.entities.EventType;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.activities.EditEventActivity;
import com.jomik.apparelapp.presentation.activities.EventSearchActivity;
import com.jomik.apparelapp.presentation.adapters.EventsRvAdapter;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Joe Deluca on 4/10/2016.
 */
public abstract class GroupListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        LinearLayout addButton = (LinearLayout) view.findViewById(R.id.btn_create_event);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditEventActivity.class);
                intent.putExtra("eventType", getEventType());
                startActivity(intent);
            }
        });

        LinearLayout findButton = (LinearLayout) view.findViewById(R.id.btn_find_event);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EventSearchActivity.class);
                intent.putExtra("eventType", getEventType());
                startActivity(intent);
            }
        });

        return view;
    }

    public List<Event> onFindUserEventsStart() throws ParseException, SQLException {
        List<Event> eventsIOwn = new ArrayList<>();
        List<Event> eventsIHaveJoined = new ArrayList<>();

        OrmLiteSqlHelper helper  = new OrmLiteSqlHelper(getContext());

        // Get all events where i own OR i am a guest of
        QueryBuilder<Event, String> eventQb = helper.getEventDao().queryBuilder();
        eventQb.where()
                .eq("marked_for_delete", false)
                .and()
                .eq("event_type", getEventType()).query();

        QueryBuilder<EventGuest, String> eventGuestQb = helper.getEventGuestDao().queryBuilder();
        eventGuestQb.where().eq("marked_for_delete", false);

        QueryBuilder<User, String> userQb = helper.getUserDao().queryBuilder();
        userQb.where().eq("uuid", AuthenticationManager.getAuthenticatedUser(getContext()).getUuid());

        eventsIHaveJoined = eventQb.join(eventGuestQb.join(userQb)).query();

        eventQb = helper.getEventDao().queryBuilder();
        eventQb.where()
                .eq("marked_for_delete", false)
                .and()
                .eq("event_type", getEventType()).query();

        eventsIOwn = eventQb.join(userQb).query();

        // remove duplicates
        LinkedHashSet set = new LinkedHashSet();
        set.addAll(eventsIOwn);
        set.addAll(eventsIHaveJoined);

        return new ArrayList<>(set);
    }

    protected void onDataloadComplete(List<Event> events) {
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        EventsRvAdapter adapter = new EventsRvAdapter(events, getEventType());
        rv.setAdapter(adapter);
    }

    protected abstract EventType getEventType();
}