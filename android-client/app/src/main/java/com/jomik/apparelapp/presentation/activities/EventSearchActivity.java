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
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.infrastructure.events.EventSearchComplete;
import com.jomik.apparelapp.infrastructure.events.EventSearchStart;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
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
    public void onMessage(EventSearchStart eventSearchStart) throws InterruptedException, ParseException {

        // find all events TODO: use server api
        Uri uri = ApparelContract.Events.CONTENT_URI ;
        Cursor cursor = getContentResolver().query(uri, ApparelContract.Events.PROJECTION_ALL, null, null, null);
        List<Event> events = new ArrayList<>();
        while(cursor.moveToNext()) {
            Event event = new Event();
            event.setId(SqlHelper.getLong(cursor, ApparelContract.Events._ID, DbSchema.PREFIX_TBL_EVENTS));
            event.setTitle(SqlHelper.getString(cursor, ApparelContract.Events.TITLE, DbSchema.PREFIX_TBL_EVENTS));
            event.setStartDate(SqlHelper.getDateForDisplay(cursor, ApparelContract.Events.START_DATE, DbSchema.PREFIX_TBL_EVENTS));
            event.setLocation(SqlHelper.getString(cursor, ApparelContract.Events.LOCATION, DbSchema.PREFIX_TBL_EVENTS));
            event.setOwnerUuid(SqlHelper.getString(cursor, ApparelContract.Events.OWNER_UUID, DbSchema.PREFIX_TBL_EVENTS));
            event.setEndDate(SqlHelper.getDateForDisplay(cursor, ApparelContract.Events.END_DATE, DbSchema.PREFIX_TBL_EVENTS));
            event.setDescription(SqlHelper.getString(cursor, ApparelContract.Events.DESCRIPTION, DbSchema.PREFIX_TBL_EVENTS));
            event.setPhotoUuid(SqlHelper.getString(cursor, ApparelContract.Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS));
            event.setPhotoPath(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS));
            event.setPhotoPathSmall(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS));
            event.setOwnerFacebookId(SqlHelper.getString(cursor, ApparelContract.Users.FACEBOOK_ID, DbSchema.PREFIX_TBL_USERS));
            event.setOwnerName(SqlHelper.getString(cursor, ApparelContract.Users.NAME, DbSchema.PREFIX_TBL_USERS));

            events.add(event);
        }

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
