package com.jomik.apparelapp.presentation.activities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.util.UUID;

public class ViewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        final Button btnJoin = (Button) findViewById(R.id.join_button);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);

        final TextView txtEventTitle = (TextView) findViewById(R.id.title);

        txtToolbarTitle.setText("View Event");

        // Populate fields if editing
        Intent intent = getIntent();
        final long eventId = intent.getLongExtra("id", -1);
        String eventUuid = null;
        String photoUuid = null;
        String photoPath = null;

        Uri uri = ContentUris.withAppendedId(ApparelContract.Events.CONTENT_URI, eventId);
        Cursor cursor = getContentResolver().query(uri, ApparelContract.Events.PROJECTION_ALL, null, null, null);
        if (cursor.moveToFirst()) {
            txtEventTitle.setText(SqlHelper.getString(cursor, ApparelContract.Events.TITLE, DbSchema.PREFIX_TBL_EVENTS));
            eventUuid = SqlHelper.getString(cursor, ApparelContract.Events.UUID, DbSchema.PREFIX_TBL_EVENTS);
            photoUuid = SqlHelper.getString(cursor, ApparelContract.Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS);
            photoPath = SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS);
        }
        cursor.close();

        if(photoUuid != null) {
            ImageHelper.setImageUri(simpleDraweeView, photoPath, photoUuid);
        }

        final String finalEventUuid = eventUuid;

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(ApparelContract.EventGuests.UUID, UUID.randomUUID().toString());
                values.put(ApparelContract.EventGuests.EVENT_UUID, finalEventUuid);
                values.put(ApparelContract.EventGuests.GUEST_UUID, AuthenticationManager.getAuthenticatedUser(getApplicationContext()).getUuid());

                getContentResolver().insert(ApparelContract.EventGuests.CONTENT_URI, values);

                Toast.makeText(getApplicationContext(), "You have joined the event", Toast.LENGTH_LONG).show();

            }
        });

    }


}
