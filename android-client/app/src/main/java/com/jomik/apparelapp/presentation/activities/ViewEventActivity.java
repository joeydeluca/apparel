package com.jomik.apparelapp.presentation.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.EventGuest;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.sql.SQLException;

public class ViewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        final Button btnJoin = (Button) findViewById(R.id.join_button);
        SimpleDraweeView eventImageView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        SimpleDraweeView userImageView = (SimpleDraweeView) findViewById(R.id.imgUser);

        final TextView txtEventTitle = (TextView) findViewById(R.id.title);
        final TextView txtLocation = (TextView) findViewById(R.id.location);
        final TextView txtDescription = (TextView) findViewById(R.id.description);
        final TextView txtDate = (TextView) findViewById(R.id.date);
        final TextView txtOwner = (TextView) findViewById(R.id.txtOwner);


        //txtToolbarTitle.setText("View Event");

        // Populate fields if editing
        Intent intent = getIntent();
        final Event event = (Event) intent.getSerializableExtra("event");

        txtEventTitle.setText(event.getTitle());
        txtLocation.setText(event.getLocation());
        txtDescription.setText(event.getDescription());
        txtOwner.setText("Created by " + event.getOwner().getName());
        txtDate.setText(SqlHelper.getDisplayDateFromEvent(event));

        if(event.getPhoto() != null) {
            ImageHelper.setImageUri(eventImageView, event.getPhoto().getPhotoPath());
        }
        if(event.getOwner() != null) {
            ImageHelper.setFacebookProfileImageUri(userImageView, event.getOwner().getFacebookId());
        }

        final User user = AuthenticationManager.getAuthenticatedUser(getApplicationContext());

        final OrmLiteSqlHelper helper = new OrmLiteSqlHelper(getApplicationContext());
        try {
            if(helper.getEventGuestDao().queryBuilder().where().eq("event_uuid", event.getUuid()).and().eq("guest_uuid", user.getUuid()).countOf() > 0) {
                btnJoin.setVisibility(View.INVISIBLE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        /*imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        final Event finalEvent = event;
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventGuest eventGuest = new EventGuest();
                eventGuest.setGuest(user);
                eventGuest.setEvent(finalEvent);
                try {
                    helper.getEventGuestDao().create(eventGuest);
                    btnJoin.setVisibility(View.INVISIBLE);

                    // Save event in db
                    helper.getUserDao().createIfNotExists(finalEvent.getOwner());
                    helper.getPhotoDao().createIfNotExists(finalEvent.getPhoto());
                    helper.getEventDao().createIfNotExists(finalEvent);

                    // Trigger sync - so we download the new event data
                    ContentResolver.requestSync(AuthenticationManager.getSyncAccount(getApplicationContext()), ApparelContract.AUTHORITY, Bundle.EMPTY);

                    Toast.makeText(getApplicationContext(), "You have joined the event", Toast.LENGTH_LONG).show();
                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

}
