package com.jomik.apparelapp.presentation.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.jomik.apparelapp.domain.entities.Photo;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;
import com.jomik.apparelapp.presentation.validator.FormValidator;
import com.jomik.apparelapp.presentation.validator.ImageValidator;

import java.sql.SQLException;

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

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");

        final OrmLiteSqlHelper helper = new OrmLiteSqlHelper(getApplicationContext());

        Item item = null;
        try {
            item = id != null ? helper.getItemDao().queryForId(id) : null;
            if(item != null) {
                txtItemName.setText(item.getName());
                txtItemDescription.setText(item.getDescription());
                spnType.setSelection(item.getItemCategory().ordinal());
                if(item.getPhoto() != null && chosenImageUri == null) {
                    ImageHelper.setImageUri(simpleDraweeView, item.getPhoto().getPhotoPath());
                }
            } else {
                item = new Item();
                item.setUser(AuthenticationManager.getAuthenticatedUser(getApplicationContext()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final Item newItem = item;

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate!
                if(!FormValidator.validate(txtItemName) ||
                        (newItem.getPhoto() == null && !ImageValidator.validate(EditItemActivity.this, chosenImageUri))) {
                    return;
                }

                if(spnType.getSelectedItem().toString().equals(ItemCategory.PLEASE_CHOOSE.getDisplayName())) {
                    Toast.makeText(getApplicationContext(), "Category is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save image record
                if(chosenImageUri != null) {
                    Photo photo = newItem.getPhoto();
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
                    newItem.setPhoto(photo);
                }

                // Save item
                newItem.setName(txtItemName.getText().toString());
                newItem.setDescription(txtItemDescription.getText().toString());
                newItem.setItemCategory(ItemCategory.getEnumFromDisplayName(spnType.getSelectedItem().toString()));
                newItem.incrementVersion();
                try {
                    helper.getItemDao().createOrUpdate(newItem);
                } catch (SQLException e) {
                    e.printStackTrace();
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
