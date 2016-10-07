package com.jomik.apparelapp.presentation.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.EventGuest;
import com.jomik.apparelapp.domain.entities.EventType;
import com.jomik.apparelapp.domain.entities.Photo;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;
import com.jomik.apparelapp.presentation.validator.FormValidator;
import com.jomik.apparelapp.presentation.validator.ImageValidator;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class EditEventActivity extends ImagePickerActivity implements View.OnClickListener {

    private EditText txtFromDate;
    private EditText txtToDate;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        final EventType eventType = (EventType) intent.getSerializableExtra("eventType");

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        final LinearLayout lblLocation = (LinearLayout) findViewById(R.id.lblLocation);
        final LinearLayout lblDate = (LinearLayout) findViewById(R.id.lblDate);
        final TextView lblEventDetails = (TextView) findViewById(R.id.lblEventDetails);
        final EditText txtEventTitle = (EditText) findViewById(R.id.title);
        final EditText txtEventLocation = (EditText) findViewById(R.id.location);
        final EditText txtDescription = (EditText) findViewById(R.id.description);
        txtFromDate = (EditText) findViewById(R.id.date_from);
        txtFromDate.setInputType(InputType.TYPE_NULL);
        txtToDate = (EditText) findViewById(R.id.date_to);
        txtToDate.setInputType(InputType.TYPE_NULL);
        if(EventType.EVENT != eventType) {
            lblEventDetails.setText("CIRCLE DETAILS");
            lblLocation.setVisibility(View.GONE);
            lblDate.setVisibility(View.GONE);
        }

        txtToolbarTitle.setText("Edit " + (eventType == EventType.EVENT ? "Event" : "Circle"));

        String existingPhotoUuid = null;
        String existingPhotoPath = null;

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(ImagePickerHelper.getPickImageChooserIntent(getApplicationContext()), 200);
            }
        });

        int version = 0;

        final OrmLiteSqlHelper helper = new OrmLiteSqlHelper(getApplicationContext());

        // Populate fields if editing
        Event event = null;
        try {
            event = id != null ? helper.getEventDao().queryForId(id) : null;
            if(event != null) {
                txtEventTitle.setText(event.getTitle());
                txtDescription.setText(event.getDescription());
                if(EventType.EVENT == eventType) {
                    txtEventLocation.setText(event.getLocation());
                    txtFromDate.setText(SqlHelper.dateFormatForDisplay.format(event.getStartDate()));
                    txtToDate.setText(SqlHelper.dateFormatForDisplay.format(event.getEndDate()));
                }
                if(event.getPhoto() != null && chosenImageUri == null) {
                    ImageHelper.setImageUri(simpleDraweeView, event.getPhoto());
                }
            } else {
                event = new Event();
                event.setOwner(AuthenticationManager.getAuthenticatedUser(getApplicationContext()));
                event.setEventType(eventType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final Event newEvent = event;

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate for event
                if(EventType.EVENT == eventType) {
                    if (!FormValidator.validate(txtEventTitle, txtEventLocation, txtFromDate, txtDescription) ||
                            (newEvent.getPhoto() == null && !ImageValidator.validate(EditEventActivity.this, chosenImageUri))) {
                        return;
                    }
                } else {
                    // Validate for Circle
                    if (!FormValidator.validate(txtEventTitle, txtDescription)) {
                        return;
                    }
                }

                if(EventType.EVENT == eventType) {
                    if (txtToDate.getText().toString().length() > 0) {
                        try {
                            Date dateFrom = SqlHelper.dateFormatForDisplay.parse(txtFromDate.getText().toString());
                            Date dateTo = SqlHelper.dateFormatForDisplay.parse(txtToDate.getText().toString());
                            if (dateFrom.after(dateTo)) {
                                txtToDate.setError("End Date cannot be before Start Date");
                                return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        txtToDate.setText(null);
                    }
                }

                // Save image record
                if(chosenImageUri != null) {
                    Photo photo = newEvent.getPhoto();
                    if (photo == null) {
                        photo = new Photo();
                    }
                    photo.setPhotoPath(chosenImageUri.getPath());
                    photo.setPhotoPathSmall(chosenImageUri.getPath());
                    photo.incrementVersion();
                    try {
                        helper.getPhotoDao().createOrUpdate(photo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    newEvent.setPhoto(photo);
                }

                try {
                    newEvent.setTitle(txtEventTitle.getText().toString());
                    newEvent.setDescription(txtDescription.getText().toString());
                    if(EventType.EVENT == eventType) {
                        newEvent.setLocation(txtEventLocation.getText().toString());
                        newEvent.setStartDate(SqlHelper.dateFormatForDisplay.parse(txtFromDate.getText().toString()));
                        newEvent.setEndDate(SqlHelper.dateFormatForDisplay.parse(txtToDate.getText().toString()));
                    }

                    newEvent.incrementVersion();
                    helper.getEventDao().createOrUpdate(newEvent);

                    // If we are creating a new event, add the owner as a guest
                    if(id == null) {
                        EventGuest eventGuest = new EventGuest();
                        eventGuest.setGuest(AuthenticationManager.getAuthenticatedUser(getApplicationContext()));
                        eventGuest.setEvent(newEvent);
                        helper.getEventGuestDao().create(eventGuest);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

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

        startDatePickerDialog = createDatePickerDialog(txtFromDate);
        endDatePickerDialog = createDatePickerDialog(txtToDate);
        setupDateField(txtFromDate, startDatePickerDialog);
        setupDateField(txtToDate, endDatePickerDialog);

    }

    private void setupDateField(final EditText editTextDate, final DatePickerDialog datePickerDialog) {
        if(editTextDate.getText().toString().length() == 0) {
            editTextDate.setText(SqlHelper.dateFormatForDisplay.format(new Date()));
        }
        editTextDate.setOnClickListener(this);
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show();
                }
            }
        });
    }

    private DatePickerDialog createDatePickerDialog(final EditText editText) {
        Calendar newCalendar = Calendar.getInstance();

        try {
            Date displayDate;
            if(editText.getText().toString().length() > 0) {
                displayDate = SqlHelper.dateFormatForDisplay.parse(editText.getText().toString());
            } else {
                displayDate = new Date();
            }
            newCalendar.setTime(displayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText.setText(SqlHelper.dateFormatForDisplay.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        if(view == txtFromDate) {
            startDatePickerDialog.show();
        } else if(view == txtToDate) {
            endDatePickerDialog.show();
        }
    }

}
