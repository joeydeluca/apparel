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
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.*;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtFromDate;
    private EditText txtToDate;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDraweeView simpleDraweeView;
    private ImagePicker imagePicker = new ImagePicker(EditEventActivity.this);
    private ChosenImage chosenImage = null;

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
        txtToDate = (EditText) findViewById(R.id.date_to);
        txtToDate.setInputType(InputType.TYPE_NULL);

        setDateTimeField();

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
                txtFromDate.setText(SqlHelper.getString(cursor, Events.START_DATE, DbSchema.PREFIX_TBL_EVENTS));
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
                if(!FormValidator.validate(txtEventTitle, txtEventLocation, txtFromDate) ||
                    (finalExistingPhotoPath == null && !ImageValidator.validate(EditEventActivity.this, chosenImage))) {
                    return;
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
                values.put(Events.PHOTO_UUID, photoUuid);
                values.put(Events.OWNER_UUID, userUuid);
                values.put(Events.START_DATE, txtFromDate.getText().toString());

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

    private void setDateTimeField(final EditText editTextDate) {
        editTextDate.setOnClickListener(this);
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    startDatePickerDialog.show();
                }
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextDate.setText(dateFormatter.format(newDate.getTime()));
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
