package com.jomik.apparelapp.presentation.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.ItemCategory;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.Items;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.Photos;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;
import com.jomik.apparelapp.presentation.validator.FormValidator;
import com.jomik.apparelapp.presentation.validator.ImageValidator;

import java.util.UUID;

public class EditItemActivity extends AppCompatActivity {

    private SimpleDraweeView simpleDraweeView;
    private Uri mCropImageUri = null;

    Uri chosenImageUri = null;
    String chosenImageUuid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final EditText txtItemName = (EditText) findViewById(R.id.edit_item_name);
        final EditText txtItemDescription = (EditText) findViewById(R.id.edit_item_description);
        final Spinner spnType = (Spinner) findViewById(R.id.edit_item_spinner_type);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        String existingPhotoUuid = null;
        String existingPhotoPath = null;

        txtToolbarTitle.setText("Edit Item");

        final ArrayAdapter spinnerTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ItemCategory.getCategoryLabels());
        spinnerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(spinnerTypeAdapter);

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.image);
        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(ImagePickerHelper.getPickImageChooserIntent(getApplicationContext()), 200);
            }
        });

        final User user = AuthenticationManager.getAuthenticatedUser(getApplicationContext());

        Intent intent = getIntent();
        // Populate fields if editing

        int version = 0;

        final long id = intent.getLongExtra("id", -1);
        if(id != -1) {
            Uri uri = ContentUris.withAppendedId(Items.CONTENT_URI, id);
            Cursor cursor = getContentResolver().query(uri, Items.PROJECTION_ALL, null, null, null);
            if (cursor.moveToFirst()) {
                txtItemName.setText(SqlHelper.getString(cursor, Items.NAME, DbSchema.PREFIX_TBL_ITEMS));
                txtItemDescription.setText(SqlHelper.getString(cursor, Items.DESCRIPTION, DbSchema.PREFIX_TBL_ITEMS));
                spnType.setSelection(ItemCategory.valueOf(SqlHelper.getString(cursor, Items.ITEM_CATEGORY, DbSchema.PREFIX_TBL_ITEMS)).ordinal());
                existingPhotoUuid = SqlHelper.getString(cursor, Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS);
                existingPhotoPath = SqlHelper.getString(cursor, Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS);
                version = SqlHelper.getInt(cursor, Items.VERSION, DbSchema.PREFIX_TBL_ITEMS);
            }
            cursor.close();

        }

        if(existingPhotoPath != null && chosenImageUri == null) {
            ImageHelper.setImageUri(simpleDraweeView, existingPhotoPath);
        }

        final String finalExistingPhotoPath = existingPhotoPath;
        final String finalExistingPhotoUuid = existingPhotoUuid;
        final int finalVersion = version;
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate!
                if(!FormValidator.validate(txtItemName) ||
                        (finalExistingPhotoPath == null && !ImageValidator.validate(EditItemActivity.this, chosenImageUri))) {
                    return;
                }

                if(spnType.getSelectedItem().toString().equals(ItemCategory.PLEASE_CHOOSE.getDisplayName())) {
                    Toast.makeText(getApplicationContext(), "Category is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save image record
                String photoUuid = finalExistingPhotoUuid;
                if(chosenImageUuid != null) {
                    if(photoUuid != null) {
                        getContentResolver().delete(Photos.CONTENT_URI, "uuid = ?", new String[] {photoUuid});
                    }
                    ContentValues values = new ContentValues();
                    values.put(Photos.UUID, chosenImageUuid);
                    values.put(Photos.LOCAL_PATH, chosenImageUri.getPath());
                    values.put(Photos.LOCAL_PATH_SM, chosenImageUri.getPath());
                    getContentResolver().insert(Photos.CONTENT_URI, values);
                    photoUuid = chosenImageUuid;
                }

                // Save item
                String dbItemCategory = ItemCategory.getEnumFromDisplayName(spnType.getSelectedItem().toString()).toString();

                ContentValues values = new ContentValues();
                values.put(Items.NAME, txtItemName.getText().toString());
                values.put(Items.DESCRIPTION, txtItemDescription.getText().toString());
                values.put(Items.ITEM_CATEGORY, dbItemCategory);
                values.put(Items.PHOTO_UUID, photoUuid);
                values.put(Items.VERSION, finalVersion);

                if(id == -1) {
                    values.put(Items.UUID, UUID.randomUUID().toString());
                    values.put(Items.USER_UUID, user.getUuid());
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            chosenImageUri = data.getParcelableExtra("chosenImageUri");
            chosenImageUuid = data.getStringExtra("chosenImageUuid");
            if(chosenImageUri != null) {
                simpleDraweeView.setImageURI(chosenImageUri);
            }
        }
        else if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = ImagePickerHelper.getPickImageResultUri(data, getApplicationContext());

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ImagePickerHelper.isUriRequiresPermissions(imageUri, getApplicationContext())) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra("uri", imageUri);
                startActivityForResult(intent, 1);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
            intent.putExtra("uri", mCropImageUri);
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

}
