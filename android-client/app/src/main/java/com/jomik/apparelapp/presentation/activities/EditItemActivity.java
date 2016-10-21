package com.jomik.apparelapp.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
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

public class EditItemActivity extends ImagePickerActivity {

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
                    ImageHelper.setImageUri(simpleDraweeView, item.getPhoto());
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
                    if(newItem.getPhoto() != null) {
                        Photo existingPhoto = newItem.getPhoto();
                        existingPhoto.setMarkedForDelete(true);
                        existingPhoto.incrementVersion();
                        try {
                            helper.getPhotoDao().update(existingPhoto);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        // TODO: delete existing photo from drive
                    }

                    Photo photo = new Photo();
                    photo.setPhotoPath(chosenImageUri.getPath());
                    photo.setPhotoPathSmall(chosenImageUri.getPath());
                    photo.incrementVersion();
                    try {
                        helper.getPhotoDao().create(photo);
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

}
