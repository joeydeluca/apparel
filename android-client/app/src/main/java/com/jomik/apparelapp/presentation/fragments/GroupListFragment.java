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

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.EventType;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.presentation.activities.EditEventActivity;
import com.jomik.apparelapp.presentation.activities.EventSearchActivity;
import com.jomik.apparelapp.presentation.adapters.EventsRvAdapter;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

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
        OrmLiteSqlHelper helper  = new OrmLiteSqlHelper(getContext());
        return helper.getEventDao().queryBuilder().where()
                .eq("marked_for_delete", false)
                .and()
                .eq("event_type", getEventType()).query();
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