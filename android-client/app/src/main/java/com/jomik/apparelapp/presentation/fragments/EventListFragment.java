package com.jomik.apparelapp.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.event.EventsRepository;
import com.jomik.apparelapp.infrastructure.events.FindUserEventsComplete;
import com.jomik.apparelapp.infrastructure.events.FindUserEventsStart;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.activities.EditEventActivity;
import com.jomik.apparelapp.presentation.activities.EventSearchActivity;
import com.jomik.apparelapp.presentation.adapters.EventsAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Joe Deluca on 4/10/2016.
 */
public class EventListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayAdapter adapter;

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

        Button addButton = (Button) view.findViewById(R.id.btn_create_event);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditEventActivity.class);
                startActivity(intent);
            }
        });

        Button findButton = (Button) view.findViewById(R.id.btn_find_event);
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
        getListView().setOnItemClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessage(FindUserEventsStart event) {
        EventsRepository eventsRepository = RepositoryFactory.getEventsRepository(RepositoryFactory.Type.IN_MEMORY);
        EventBus.getDefault().post(new FindUserEventsComplete(eventsRepository.findAllForUser(AuthenticationManager.getAuthenticatedUser())));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(FindUserEventsComplete event) {
        adapter = new EventsAdapter(getActivity().getApplicationContext(), event.getEvents());
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), EditEventActivity.class);
        intent.putExtra("id", view.getTag().toString());
        startActivity(intent);
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