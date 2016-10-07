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
import com.jomik.apparelapp.domain.entities.EventType;
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

        final TextView txtAlreadyJoinedText = (TextView) findViewById(R.id.alreadyJoinedText);
        final TextView txtEventTitle = (TextView) findViewById(R.id.title);
        final TextView txtLocation = (TextView) findViewById(R.id.location);
        final TextView txtDescription = (TextView) findViewById(R.id.description);
        final TextView txtDate = (TextView) findViewById(R.id.date);
        final TextView txtOwner = (TextView) findViewById(R.id.txtOwner);

        //txtToolbarTitle.setText("View Event");

        // Populate fields if editing
        Intent intent = getIntent();
        final Event event = (Event) intent.getSerializableExtra("event");

        final String eventTypeLabel = EventType.EVENT == event.getEventType() ? "event" : "circle";

        txtAlreadyJoinedText.setText(String.format("You have already joined this %s", eventTypeLabel));

        if(event.getLocation() != null) {
            txtLocation.setText(event.getLocation());
        } else {
            txtLocation.setVisibility(View.GONE);
        }

        if(event.getStartDate() != null && event.getEndDate() != null) {
            txtDate.setText(SqlHelper.getDisplayDateFromEvent(event));
        } else {
            txtDate.setVisibility(View.GONE);
        }

        txtEventTitle.setText(event.getTitle());

        txtDescription.setText(event.getDescription());
        txtOwner.setText("Created by " + event.getOwner().getName());

        if(event.getPhoto() != null) {
            ImageHelper.setImageUri(eventImageView, event.getPhoto());
        } else {
            eventImageView.setVisibility(View.GONE);
        }

        if(event.getOwner() != null) {
            ImageHelper.setFacebookProfileImageUri(userImageView, event.getOwner().getFacebookId());
        }

        final User user = AuthenticationManager.getAuthenticatedUser(getApplicationContext());

        final OrmLiteSqlHelper helper = new OrmLiteSqlHelper(getApplicationContext());
        try {
            if(helper.getEventGuestDao().queryBuilder().where().eq("event_uuid", event.getUuid()).and().eq("guest_uuid", user.getUuid()).and().eq("marked_for_delete",false).countOf() > 0) {
                btnJoin.setVisibility(View.INVISIBLE);
            } else {
                txtAlreadyJoinedText.setVisibility(View.INVISIBLE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            txtAlreadyJoinedText.setVisibility(View.INVISIBLE);
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
                    txtAlreadyJoinedText.setVisibility(View.VISIBLE);

                    // Save event in db
                    if (finalEvent.getPhoto() != null) {
                        helper.getPhotoDao().createIfNotExists(finalEvent.getPhoto());
                    }
                    helper.getUserDao().createIfNotExists(finalEvent.getOwner());
                    helper.getEventDao().createIfNotExists(finalEvent);

                    // Trigger sync - so we download the new event data
                    ContentResolver.requestSync(AuthenticationManager.getSyncAccount(getApplicationContext()), ApparelContract.AUTHORITY, Bundle.EMPTY);

                    Toast.makeText(getApplicationContext(), String.format("You have joined the %s", eventTypeLabel), Toast.LENGTH_LONG).show();

                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

}
