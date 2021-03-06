package com.jomik.apparelapp.presentation.fragments;

import com.jomik.apparelapp.domain.entities.EventType;
import com.jomik.apparelapp.infrastructure.events.FindUserEventsComplete;
import com.jomik.apparelapp.infrastructure.events.FindUserEventsStart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by Joe Deluca on 4/10/2016.
 */
public class EventListFragment extends GroupListFragment {

    @Override
    protected EventType getEventType() {
        return EventType.EVENT;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(FindUserEventsComplete userEvent) {
        onDataloadComplete(userEvent.getEvents());
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


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessage(FindUserEventsStart findUserEventsStart) throws ParseException, SQLException {
        EventBus.getDefault().post(new FindUserEventsComplete(onFindUserEventsStart()));
    }
}