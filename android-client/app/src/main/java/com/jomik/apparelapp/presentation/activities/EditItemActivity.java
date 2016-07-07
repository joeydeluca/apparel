package com.jomik.apparelapp.presentation.activities;

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

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.domain.entities.item.ItemCategory;
import com.jomik.apparelapp.domain.entities.item.ItemColor;
import com.jomik.apparelapp.domain.entities.item.ItemPattern;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.*;

public class EditItemActivity extends AppCompatActivity {

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

        // Populate fields if editing
        Intent intent = getIntent();
        final long id = intent.getLongExtra("id", -1);
        if(id != -1) {
            Uri uri = ContentUris.withAppendedId(Items.CONTENT_URI, id);
            Cursor cursor = getContentResolver().query(uri, Items.PROJECTION_ALL, null, null, null);
            if (cursor.moveToFirst()) {
                txtItemName.setText(cursor.getString(cursor.getColumnIndexOrThrow(Items.NAME)));
                txtItemDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(Items.DESCRIPTION)));
                spnColor.setSelection(ItemColor.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(Items.ITEM_COLOR))).ordinal());
                spnPattern.setSelection(ItemPattern.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(Items.ITEM_PATTERN))).ordinal());
                spnType.setSelection(ItemCategory.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(Items.ITEM_CATEGORY))).ordinal());
            }
            cursor.close();

        } else {
            btnDelete.setVisibility(View.INVISIBLE);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(Items.NAME, txtItemName.getText().toString());
                values.put(Items.DESCRIPTION, txtItemDescription.getText().toString());
                values.put(Items.ITEM_COLOR, spnColor.getSelectedItem().toString());
                values.put(Items.ITEM_PATTERN, spnPattern.getSelectedItem().toString());
                values.put(Items.ITEM_CATEGORY, spnType.getSelectedItem().toString());

                if(id == -1) {
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

}
