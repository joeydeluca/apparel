package com.jomik.apparelapp.presentation.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.presentation.adapters.EventsAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class EventSearchResultsActivity extends ListActivity implements AdapterView.OnItemClickListener {

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search_results);

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        final TextView txtToolbarDone = (TextView) findViewById(R.id.toolbar_done_button);
        final ImageView imgToolbarCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        txtToolbarDone.setVisibility(View.INVISIBLE);
        txtToolbarTitle.setText("Search Results");
        imgToolbarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getListView().setOnItemClickListener(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessage(List<Event> events) {
        adapter = new EventsAdapter(getApplicationContext(), events);
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), ViewEventActivity.class);
        intent.putExtra("event", (Event) view.getTag());
        startActivity(intent);
    }
}
