package com.jomik.apparelapp.presentation.activities;

import android.content.Intent;
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
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.item.ItemsRepository;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_item_edit);

        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final EditText txtItemName = (EditText) findViewById(R.id.edit_item_name);
        final EditText txtItemDescription = (EditText) findViewById(R.id.edit_item_description);
        final Spinner spnColor = (Spinner) findViewById(R.id.edit_item_spinner_color);
        final Spinner spnPattern = (Spinner) findViewById(R.id.edit_item_spinner_pattern);
        final Spinner spnType = (Spinner) findViewById(R.id.edit_item_spinner_type);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);

        final ItemsRepository itemsRepository = RepositoryFactory.getItemsRepository(RepositoryFactory.Type.IN_MEMORY);

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
        final String itemId = intent.getStringExtra("id");
        final Item item;
        if(itemId != null) {
            item = itemsRepository.findOne(itemId);
            txtItemName.setText(item.getName());
            txtItemDescription.setText(item.getDescription());
            spnColor.setSelection(item.getItemColor().ordinal());
            spnPattern.setSelection(item.getItemPattern().ordinal());
            spnType.setSelection(item.getItemCategory().ordinal());
        } else {
            item = new Item();
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setName(txtItemName.getText().toString());
                item.setDescription(txtItemDescription.getText().toString());
                item.setItemCategory(ItemCategory.BOTTOMS);
                item.setItemColor((ItemColor) spnColor.getSelectedItem());
                item.setItemPattern((ItemPattern) spnPattern.getSelectedItem());
                item.setItemCategory((ItemCategory) spnType.getSelectedItem());
                item.setPhotoId(123);

                itemsRepository.save(item);

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
