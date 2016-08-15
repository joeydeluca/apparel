package com.jomik.apparelapp.presentation.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.Events;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.Photos;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;
import com.jomik.apparelapp.presentation.validator.FormValidator;
import com.jomik.apparelapp.presentation.validator.ImageValidator;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtFromDate;
    private EditText txtToDate;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private SimpleDraweeView simpleDraweeView;
    private ImagePicker imagePicker = new ImagePicker(EditEventActivity.this);
    private ChosenImage chosenImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        final TextView btnDelete = (TextView) findViewById(R.id.delete_button);
        final EditText txtEventTitle = (EditText) findViewById(R.id.title);
        final EditText txtEventLocation = (EditText) findViewById(R.id.location);
        final EditText txtDescription = (EditText) findViewById(R.id.description);
        txtFromDate = (EditText) findViewById(R.id.date_from);
        txtFromDate.setInputType(InputType.TYPE_NULL);
        txtToDate = (EditText) findViewById(R.id.date_to);
        txtToDate.setInputType(InputType.TYPE_NULL);

        startDatePickerDialog = createDatePickerDialog(txtFromDate);
        endDatePickerDialog = createDatePickerDialog(txtToDate);
        setupDateField(txtFromDate, startDatePickerDialog);
        setupDateField(txtToDate, endDatePickerDialog);

        txtToolbarTitle.setText("Edit Event");

        String existingPhotoUuid = null;
        String existingPhotoPath = null;

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.setImagePickerCallback(getImagePickerCallback());
                imagePicker.pickImage();
            }
        });

        // Populate fields if editing
        Intent intent = getIntent();
        final long id = intent.getLongExtra("id", -1);
        if(id != -1) {
            Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, id);
            Cursor cursor = getContentResolver().query(uri, Events.PROJECTION_ALL, null, null, null);
            if (cursor.moveToFirst()) {
                txtEventTitle.setText((SqlHelper.getString(cursor, Events.TITLE, DbSchema.PREFIX_TBL_EVENTS)));
                txtEventLocation.setText(SqlHelper.getString(cursor, Events.LOCATION, DbSchema.PREFIX_TBL_EVENTS));
                txtDescription.setText(SqlHelper.getString(cursor, Events.DESCRIPTION, DbSchema.PREFIX_TBL_EVENTS));
                txtFromDate.setText(SqlHelper.getDateForDisplay(cursor, Events.START_DATE, DbSchema.PREFIX_TBL_EVENTS));
                txtToDate.setText(SqlHelper.getDateForDisplay(cursor, Events.END_DATE, DbSchema.PREFIX_TBL_EVENTS));
                existingPhotoUuid = SqlHelper.getString(cursor, Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS);
                existingPhotoPath = SqlHelper.getString(cursor, Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS);
            }
            cursor.close();
        } else {
            btnDelete.setVisibility(View.INVISIBLE);
        }

        if(existingPhotoPath != null) {
            ImageHelper.setImageUri(simpleDraweeView, existingPhotoPath, existingPhotoUuid);
        }

        final String finalExistingPhotoPath = existingPhotoPath;
        final String finalExistingPhotoUuid = existingPhotoUuid;

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate
                if(!FormValidator.validate(txtEventTitle, txtEventLocation, txtFromDate, txtDescription) ||
                    (finalExistingPhotoPath == null && !ImageValidator.validate(EditEventActivity.this, chosenImage))) {
                    return;
                }

                if(txtToDate.getText().toString().length() > 0) {
                    try {
                        Date dateFrom = SqlHelper.dateFormatForDisplay.parse(txtFromDate.getText().toString());
                        Date dateTo = SqlHelper.dateFormatForDisplay.parse(txtToDate.getText().toString());
                        if(dateFrom.after(dateTo)) {
                            txtToDate.setError("End Date cannot be before Start Date");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    txtToDate.setText(null);
                }

                // Save image record
                String photoUuid = finalExistingPhotoUuid;
                if(chosenImage != null) {
                    if(photoUuid != null) {
                        getContentResolver().delete(Photos.CONTENT_URI, "uuid = ?", new String[] {photoUuid});
                    }
                    String newPhotoUuid = UUID.randomUUID().toString();
                    ContentValues values = new ContentValues();
                    values.put(Photos.UUID, newPhotoUuid);
                    values.put(Photos.LOCAL_PATH, chosenImage.getThumbnailPath());
                    values.put(Photos.LOCAL_PATH_SM, chosenImage.getThumbnailSmallPath());
                    getContentResolver().insert(Photos.CONTENT_URI, values);
                    photoUuid = newPhotoUuid;
                }

                String userUuid = AuthenticationManager.getAuthenticatedUser(getApplicationContext()).getUuid();

                ContentValues values = new ContentValues();
                values.put(Events.TITLE, txtEventTitle.getText().toString());
                values.put(Events.LOCATION, txtEventLocation.getText().toString());
                values.put(Events.DESCRIPTION, txtDescription.getText().toString());
                values.put(Events.PHOTO_UUID, photoUuid);
                values.put(Events.OWNER_UUID, userUuid);
                values.put(Events.START_DATE, SqlHelper.getDateForDb(txtFromDate.getText().toString()));
                values.put(Events.END_DATE, SqlHelper.getDateForDb(txtToDate.getText().toString()));
                if(id == -1) {
                    values.put(Events.UUID, UUID.randomUUID().toString());
                    getContentResolver().insert(Events.CONTENT_URI, values);
                } else {
                    getContentResolver().update(ContentUris.withAppendedId(Events.CONTENT_URI, id), values, null, null);
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(ContentUris.withAppendedId(Events.CONTENT_URI, id), null, null);

                Toast.makeText(EditEventActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                onBackPressed();
            }
        });

    }

    private void setupDateField(final EditText editTextDate, final DatePickerDialog datePickerDialog) {
        editTextDate.setText(SqlHelper.dateFormatForDisplay.format(new Date()));
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
            Date displayDate = SqlHelper.dateFormatForDisplay.parse(editText.getText().toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                if(imagePicker == null) {
                    imagePicker = new ImagePicker(EditEventActivity.this);
                    imagePicker.setImagePickerCallback(getImagePickerCallback());
                }

                imagePicker.submit(data);
            }
        }
    }

    private ImagePickerCallback getImagePickerCallback() {
        return new ImagePickerCallback(){
            @Override
            public void onImagesChosen(List<ChosenImage> images) {
                chosenImage = images.get(0);
                ImageHelper.setImageUri(simpleDraweeView, chosenImage.getThumbnailSmallPath());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "Something went terribly wrong.", Toast.LENGTH_SHORT);
            }
        };
    }

}
