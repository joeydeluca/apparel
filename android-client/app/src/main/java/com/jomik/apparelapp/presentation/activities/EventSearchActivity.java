package com.jomik.apparelapp.presentation.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.infrastructure.events.EventSearchComplete;
import com.jomik.apparelapp.infrastructure.events.EventSearchStart;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

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
    public void onMessage(EventSearchStart eventSearchStart) throws InterruptedException, ParseException, SQLException {

        // find all events TODO: use server api
        Uri uri = ApparelContract.Events.CONTENT_URI ;
        Cursor cursor = getContentResolver().query(uri, ApparelContract.Events.PROJECTION_ALL, null, null, null);
        OrmLiteSqlHelper helper = new OrmLiteSqlHelper(getApplicationContext());

        List<Event> events = helper.getEventDao().queryForAll();

        EventBus.getDefault().post(new EventSearchComplete(events));
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
