package com.jomik.apparelapp.presentation.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.event.EventsRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtFromDate;

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        final TextView btnDelete = (TextView) findViewById(R.id.delete_button);
        final EditText txtEventTitle = (EditText) findViewById(R.id.title);
        final EditText txtEventLocation = (EditText) findViewById(R.id.location);
        txtFromDate = (EditText) findViewById(R.id.date_from);

        txtFromDate.setInputType(InputType.TYPE_NULL);

        setDateTimeField();

        txtToolbarTitle.setText("Edit Event");

        final EventsRepository eventsRepository = RepositoryFactory.getEventsRepository(RepositoryFactory.Type.IN_MEMORY);

        // Populate fields if editing
        Intent intent = getIntent();
        final String eventId = intent.getStringExtra("id");
        final Event event;
        if(eventId != null) {
            event = eventsRepository.findOne(eventId);
            txtEventTitle.setText(event.getTitle());
            txtEventLocation.setText(event.getLocation());
            if(event.getStartDate() != null) {
                txtFromDate.setText(dateFormatter.format(event.getStartDate()));
            }
        } else {
            event = new Event();
            btnDelete.setVisibility(View.INVISIBLE);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setTitle(txtEventTitle.getText().toString());
                event.setLocation(txtEventLocation.getText().toString());
                event.setPhotoId(123);
                try {
                    event.setStartDate(dateFormatter.parse(txtFromDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                eventsRepository.save(event);

                Toast.makeText(EditEventActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                onBackPressed();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsRepository.delete(event);

                Toast.makeText(EditEventActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                onBackPressed();
            }
        });

    }

    private void setDateTimeField() {
        txtFromDate.setOnClickListener(this);
        txtFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    fromDatePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtFromDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {
        if(view == txtFromDate) {
            fromDatePickerDialog.show();
        }
    }

}
