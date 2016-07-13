package com.jomik.apparelapp.presentation.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.entities.usereventoutfit.UserEventOutfit;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.event.EventsRepository;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        final Button btnJoin = (Button) findViewById(R.id.join_button);

        final TextView txtEventTitle = (TextView) findViewById(R.id.title);

        txtToolbarTitle.setText("View Event");

        final EventsRepository eventsRepository = RepositoryFactory.getEventsRepository(RepositoryFactory.Type.IN_MEMORY);

        // Populate fields if editing
        Intent intent = getIntent();
        final String eventId = intent.getStringExtra("id");
        final Event event = eventsRepository.findOne(eventId);
        txtEventTitle.setText(event.getTitle());

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
                Toast.makeText(getApplicationContext(), "You have joined the event", Toast.LENGTH_LONG).show();

                UserEventOutfit userEventOutfit = new UserEventOutfit();
                userEventOutfit.setUser(AuthenticationManager.getAuthenticatedUser(getApplicationContext()));
                userEventOutfit.setEvent(event);
                event.getAttendees().add(userEventOutfit);
                eventsRepository.save(event);
            }
        });

    }


}
