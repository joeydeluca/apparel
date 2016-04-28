package com.jomik.apparelapp.presentation.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.event.EventsRepository;
import com.jomik.apparelapp.infrastructure.events.EventSearchComplete;
import com.jomik.apparelapp.infrastructure.events.EventSearchStart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventSearchActivity extends AppCompatActivity {

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        final Button btnSearch = (Button) findViewById(R.id.btn_search);

        txtToolbarTitle.setText("Find an event");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(EventSearchActivity.this, "",
                "Searching...", true);
                dialog.show();

                EventBus.getDefault().post(new EventSearchStart("joeyiscool"));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessage(EventSearchStart event) throws InterruptedException {

        EventsRepository eventsRepository = RepositoryFactory.getEventsRepository(RepositoryFactory.Type.IN_MEMORY);
        EventBus.getDefault().post(new EventSearchComplete(eventsRepository.findAll()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(EventSearchComplete event) {
        dialog.hide();
        EventBus.getDefault().postSticky(event.getSearchResults());
        Intent intent = new Intent(getApplicationContext(), EventSearchResultsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


}
