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
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.events.FindUserEventsComplete;
import com.jomik.apparelapp.infrastructure.events.FindUserEventsStart;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.activities.EditEventActivity;
import com.jomik.apparelapp.presentation.activities.EventSearchActivity;
import com.jomik.apparelapp.presentation.adapters.EventsRvAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Joe Deluca on 4/10/2016.
 */
public class EventListFragment extends Fragment {

    public EventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        LinearLayout addButton = (LinearLayout) view.findViewById(R.id.btn_create_event);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditEventActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout findButton = (LinearLayout) view.findViewById(R.id.btn_find_event);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EventSearchActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessage(FindUserEventsStart findUserEventsStart) throws ParseException, SQLException {

        User user = AuthenticationManager.getAuthenticatedUser(getContext());

        OrmLiteSqlHelper helper  = new OrmLiteSqlHelper(getContext());
        List<Event> events = helper.getEventDao().queryBuilder().where().eq("marked_for_delete", false).query();

        EventBus.getDefault().post(new FindUserEventsComplete(events));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(FindUserEventsComplete userEvent) {

        RecyclerView rv = (RecyclerView) getActivity().findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        EventsRvAdapter adapter = new EventsRvAdapter(userEvent.getEvents());
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new FindUserEventsStart());

        super.onResume();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
}