package com.jomik.apparelapp.presentation.activities;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.item.ItemCategory;
import com.jomik.apparelapp.domain.entities.item.ItemColor;
import com.jomik.apparelapp.domain.entities.item.ItemPattern;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.Items;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.Photos;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;
import com.jomik.apparelapp.presentation.validator.FormValidator;
import com.jomik.apparelapp.presentation.validator.ImageValidator;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.util.List;
import java.util.UUID;

public class EditItemActivity extends AppCompatActivity {

    private SimpleDraweeView simpleDraweeView;
    private ImagePicker imagePicker = new ImagePicker(EditItemActivity.this);
    private ChosenImage chosenImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final EditText txtItemName = (EditText) findViewById(R.id.edit_item_name);
        final EditText txtItemDescription = (EditText) findViewById(R.id.edit_item_description);
        final Spinner spnColor = (Spinner) findViewById(R.id.edit_item_spinner_color);
        final Spinner spnPattern = (Spinner) findViewById(R.id.edit_item_spinner_pattern);
        final Spinner spnType = (Spinner) findViewById(R.id.edit_item_spinner_type);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        final TextView btnDelete = (TextView) findViewById(R.id.edit_item_delete_button);
        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        String existingPhotoUuid = null;
        String existingPhotoPath = null;

        txtToolbarTitle.setText("Edit Item");

        final ArrayAdapter spinnerColorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ItemColor.values());
        spinnerColorAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnColor.setAdapter(spinnerColorAdapter);

        final ArrayAdapter spinnerPatternAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ItemPattern.values());
        spinnerPatternAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPattern.setAdapter(spinnerPatternAdapter);

        final ArrayAdapter spinnerTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ItemCategory.values());
        spinnerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(spinnerTypeAdapter);

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.image);
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
            Uri uri = ContentUris.withAppendedId(Items.CONTENT_URI, id);
            Cursor cursor = getContentResolver().query(uri, Items.PROJECTION_ALL, null, null, null);
            if (cursor.moveToFirst()) {
                txtItemName.setText(SqlHelper.getString(cursor, Items.NAME, DbSchema.PREFIX_TBL_ITEMS));
                txtItemDescription.setText(SqlHelper.getString(cursor, Items.DESCRIPTION, DbSchema.PREFIX_TBL_ITEMS));
                spnColor.setSelection(ItemColor.valueOf(SqlHelper.getString(cursor, Items.ITEM_COLOR, DbSchema.PREFIX_TBL_ITEMS)).ordinal());
                spnPattern.setSelection(ItemPattern.valueOf(SqlHelper.getString(cursor, Items.ITEM_PATTERN, DbSchema.PREFIX_TBL_ITEMS)).ordinal());
                spnType.setSelection(ItemCategory.valueOf(SqlHelper.getString(cursor, Items.ITEM_CATEGORY, DbSchema.PREFIX_TBL_ITEMS)).ordinal());
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
                // Validate!
                if(!FormValidator.validate(txtItemName) ||
                        (finalExistingPhotoPath == null && !ImageValidator.validate(EditItemActivity.this, chosenImage))) {
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

                // Save item
                ContentValues values = new ContentValues();
                values.put(Items.NAME, txtItemName.getText().toString());
                values.put(Items.DESCRIPTION, txtItemDescription.getText().toString());
                values.put(Items.ITEM_COLOR, spnColor.getSelectedItem().toString());
                values.put(Items.ITEM_PATTERN, spnPattern.getSelectedItem().toString());
                values.put(Items.ITEM_CATEGORY, spnType.getSelectedItem().toString());
                values.put(Items.PHOTO_UUID, photoUuid);

                if(id == -1) {
                    values.put(Items.UUID, UUID.randomUUID().toString());
                    getContentResolver().insert(Items.CONTENT_URI, values);
                } else {
                    getContentResolver().update(ContentUris.withAppendedId(Items.CONTENT_URI, id), values, null, null);
                }

                Toast.makeText(EditItemActivity.this, "Saved", Toast.LENGTH_SHORT).show();

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
                getContentResolver().delete(ContentUris.withAppendedId(Items.CONTENT_URI, id), null, null);

                Toast.makeText(EditItemActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                if(imagePicker == null) {
                    imagePicker = new ImagePicker(EditItemActivity.this);
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
