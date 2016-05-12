package com.jomik.apparelapp.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.event.EventsRepository;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.adapters.EventOutfitsRvAdapter;
import com.jomik.apparelapp.presentation.adapters.EventsRvAdapter;

public class ViewEventOutfitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_outfits);

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        txtToolbarTitle.setText("Guest outfits");

        final EventsRepository eventsRepository = RepositoryFactory.getEventsRepository(RepositoryFactory.Type.IN_MEMORY);

        Intent intent = getIntent();
        final String eventId = intent.getStringExtra("id");
        final Event event = eventsRepository.findOne(eventId);


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        EventOutfitsRvAdapter adapter = new EventOutfitsRvAdapter(event.getAttendees());
        rv.setAdapter(adapter);

    }


}
