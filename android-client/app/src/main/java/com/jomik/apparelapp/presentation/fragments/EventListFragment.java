package com.jomik.apparelapp.presentation.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.entities.user.User;
import com.jomik.apparelapp.infrastructure.events.FindUserEventsComplete;
import com.jomik.apparelapp.infrastructure.events.FindUserEventsStart;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.activities.EditEventActivity;
import com.jomik.apparelapp.presentation.activities.EventSearchActivity;
import com.jomik.apparelapp.presentation.adapters.EventsRvAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
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
    public void onMessage(FindUserEventsStart findUserEventsStart) throws ParseException {

        User user = AuthenticationManager.getAuthenticatedUser(getContext());

        // Find all owning events
        Uri uri = ApparelContract.Events.CONTENT_URI ;
        Cursor cursor = getActivity().getContentResolver().query(uri, ApparelContract.Events.PROJECTION_ALL, DbSchema.PREFIX_TBL_EVENTS + "." + ApparelContract.Events.OWNER_UUID + " = ?", new String[] {user.getUuid()}, null);
        List<Event> events = new ArrayList<>();
        while(cursor.moveToNext()) {
            Event event = new Event();
            event.setId(SqlHelper.getLong(cursor, ApparelContract.Events._ID, DbSchema.PREFIX_TBL_EVENTS));
            event.setTitle(SqlHelper.getString(cursor, ApparelContract.Events.TITLE, DbSchema.PREFIX_TBL_EVENTS));
            event.setStartDate(SqlHelper.getString(cursor, ApparelContract.Events.START_DATE, DbSchema.PREFIX_TBL_EVENTS));
            event.setLocation(SqlHelper.getString(cursor, ApparelContract.Events.LOCATION, DbSchema.PREFIX_TBL_EVENTS));
            event.setOwnerUuid(SqlHelper.getString(cursor, ApparelContract.Events.OWNER_UUID, DbSchema.PREFIX_TBL_EVENTS));
            event.setOwnerFacebookId(SqlHelper.getString(cursor, ApparelContract.Users.FACEBOOK_ID, DbSchema.PREFIX_TBL_USERS));
            event.setOwnerName(SqlHelper.getString(cursor, ApparelContract.Users.NAME, DbSchema.PREFIX_TBL_USERS));
            String strEndDate = SqlHelper.getString(cursor, ApparelContract.Events.END_DATE, DbSchema.PREFIX_TBL_EVENTS);
            if(strEndDate != null && !strEndDate.trim().isEmpty()) {
                event.setEndDate(strEndDate);
            }
            event.setPhotoUuid(SqlHelper.getString(cursor, ApparelContract.Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS));
            event.setPhotoPath(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS));
            event.setPhotoPathSmall(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS));
            events.add(event);
        }

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