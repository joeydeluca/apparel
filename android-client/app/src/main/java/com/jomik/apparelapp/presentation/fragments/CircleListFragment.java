package com.jomik.apparelapp.presentation.fragments;

import com.jomik.apparelapp.domain.entities.EventType;
import com.jomik.apparelapp.infrastructure.events.FindUserCirclesComplete;
import com.jomik.apparelapp.infrastructure.events.FindUserCirclesStart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by Joe Deluca on 4/10/2016.
 */
public class CircleListFragment extends GroupListFragment {

    @Override
    protected EventType getEventType() {
        return EventType.CIRCLE;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessage(FindUserCirclesStart findUserEventsStart) throws ParseException, SQLException {
        EventBus.getDefault().post(new FindUserCirclesComplete(onFindUserEventsStart()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(FindUserCirclesComplete userEvent) {
        onDataloadComplete(userEvent.getEvents());
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new FindUserCirclesStart());

        super.onResume();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

}