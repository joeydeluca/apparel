package com.jomik.apparelapp.presentation.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.Photo;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.presentation.adapters.ItemImageRvAdapter;
import com.jomik.apparelapp.presentation.adapters.ItemSelectionAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class OutfitSelectionActivity extends AppCompatActivity {

    private static final String GUEST_EVENT_UUID = "guestEventUuid";
    private static final String SELECTED_ITEMS = "selectedItems";
    private static final String OUTFIT_DESCRIPTION = "outfitDescription";
    private static final String EVENT_ID = "eventId";
    private static final String EVENT_START_DATE = "eventStartDate";
    private static final String EVENT_END_DATE = "eventEndDate";
    private static final String EVENT_TARGET_DATE = "eventTargetDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_selection);

        final String eventId = getIntent().getStringExtra(EVENT_ID);
        final String eventGuestUuid = getIntent().getStringExtra(GUEST_EVENT_UUID);
        final Date targetDate = (Date) getIntent().getSerializableExtra(EVENT_TARGET_DATE);
        final Date startDate = (Date) getIntent().getSerializableExtra(EVENT_START_DATE);
        final Date endDate = (Date) getIntent().getSerializableExtra(EVENT_END_DATE);
        final List<Item> selectedItems = (List<Item>) getIntent().getSerializableExtra(SELECTED_ITEMS);
        final String outfitDescription = getIntent().getStringExtra(OUTFIT_DESCRIPTION);

        final String targetDisplayDate = SqlHelper.dateFormatForDisplay.format(targetDate);

        final TextView txtDate = (android.widget.TextView) findViewById(R.id.date);
        txtDate.setText(targetDisplayDate);
        final TextView txtToolbarTitle = (android.widget.TextView) findViewById(R.id.toolbar_title);
        txtToolbarTitle.setText("Select outfit");
        final TextView btnDone = (TextView) findViewById(R.id.toolbar_done_button);
        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        final TextView txtOutfitDescription = (android.widget.TextView) findViewById(R.id.txtOutfitDescription);
        txtOutfitDescription.setText(outfitDescription);

        GridView gridView = (GridView) findViewById(R.id.grid);

        RecyclerView rv = (RecyclerView) findViewById(R.id.hlist);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(llm);

        final ItemImageRvAdapter adapter = new ItemImageRvAdapter(selectedItems);
        rv.setAdapter(adapter);

        Uri uri = ApparelContract.Items.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, ApparelContract.Items.PROJECTION_ALL, null, null, null);
        final List<Item> items = new ArrayList<>();
        while(cursor.moveToNext()) {
            Item item = new Item();
            SqlHelper.setCommonFieldsFromCursor(cursor, item, DbSchema.PREFIX_TBL_ITEMS);
            item.setName(SqlHelper.getString(cursor, ApparelContract.Items.NAME, DbSchema.PREFIX_TBL_ITEMS));
            item.setPhotoUuid(SqlHelper.getString(cursor, ApparelContract.Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS));
            Photo photo = new Photo();
            SqlHelper.setCommonFieldsFromCursor(cursor, photo, DbSchema.PREFIX_TBL_PHOTOS);
            photo.setPhotoPath(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS));
            photo.setPhotoPathSmall(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS));
            item.setPhoto(photo);

            items.add(item);
        }
        cursor.close();

        gridView.setAdapter(new ItemSelectionAdapter(this, items.toArray(new Item[items.size()]), new HashSet<>(selectedItems)));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                SimpleDraweeView image = (SimpleDraweeView) v.findViewById(R.id.grid_item_image);

                Item selectedItem = items.get(position);

                if (selectedItems.contains(selectedItem)) {
                    image.setBackgroundResource(0);
                    selectedItems.remove(selectedItem);
                } else {
                    image.setBackgroundResource(R.drawable.com_facebook_button_like_icon_selected);
                    selectedItems.add(0, selectedItem);
                }

                adapter.notifyDataSetChanged();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String guestOutfitUuid = getOrCreateEventGuestOutfitUuid(eventGuestUuid, targetDisplayDate);

                deleteExistingOutfit(eventGuestUuid, targetDisplayDate);

                saveOutfit(guestOutfitUuid, selectedItems);

                saveOutfitDescription(guestOutfitUuid, outfitDescription, txtOutfitDescription.getText().toString());

                Toast.makeText(OutfitSelectionActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ViewEventOutfitsActivity.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("targetDate", targetDate);
                intent.putExtra("startDate", startDate);
                intent.putExtra("endDate", endDate);
                startActivity(intent);
                finish();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private String getOrCreateEventGuestOutfitUuid(String eventGuestUuid, String eventDate) {
        String guestOutfitUuid = null;
        Uri uri = ApparelContract.EventGuestOutfits.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, new String[]{"uuid"}, "event_guest_uuid = ? and event_date = ?", new String[]{eventGuestUuid, SqlHelper.getDateForDb(eventDate)}, null);
        while (cursor.moveToNext()) {
            guestOutfitUuid = cursor.getString(cursor.getColumnIndex(ApparelContract.EventGuestOutfits.UUID));
        }
        cursor.close();

        if(guestOutfitUuid == null) {
            guestOutfitUuid = UUID.randomUUID().toString();
            ContentValues values = new ContentValues();
            values.put(ApparelContract.EventGuestOutfits.UUID, guestOutfitUuid);
            values.put(ApparelContract.EventGuestOutfits.EVENT_DATE, SqlHelper.getDateForDb(eventDate));
            values.put(ApparelContract.EventGuestOutfits.EVENT_GUEST_UUID, eventGuestUuid);
            getContentResolver().insert(ApparelContract.EventGuestOutfits.CONTENT_URI, values);
        }

        return guestOutfitUuid;
    }

    private void deleteExistingOutfit(String eventGuestUuid, String eventDate) {
        getContentResolver().delete(ApparelContract.EventGuestOutfitItems.CONTENT_URI, "event_guest_outfit_uuid in (select ego.uuid from event_guest_outfits ego where ego.event_date = ? and ego.event_guest_uuid = ?)", new String[]{SqlHelper.getDateForDb(eventDate), eventGuestUuid});
    }

    private void saveOutfit(String guestOutfitUuid, List<Item> selectedItems) {
        for(Item selectedItem : selectedItems) {
            ContentValues values = new ContentValues();
            values.put(ApparelContract.EventGuestOutfitItems.UUID, UUID.randomUUID().toString());
            values.put(ApparelContract.EventGuestOutfitItems.EVENT_GUEST_OUTFIT_UUID, guestOutfitUuid);
            values.put(ApparelContract.EventGuestOutfitItems.ITEM_UUID, selectedItem.getUuid());
            getContentResolver().insert(ApparelContract.EventGuestOutfitItems.CONTENT_URI, values);
        }
    }

    private void saveOutfitDescription(String guestOutfitUuid, String originalOutfitDescription, String newOutfitDescription) {
        if(!newOutfitDescription.equals(originalOutfitDescription)) {
            ContentValues values = new ContentValues();
            values.put(ApparelContract.EventGuestOutfits.DESCRIPTION, newOutfitDescription);
            getContentResolver().update(ApparelContract.EventGuestOutfits.CONTENT_URI, values, "uuid = ?", new String[]{guestOutfitUuid});
        }
    }

    public static Intent getIntent(Context context, String eventId, String eventGuestUuid, Date targetDate, Date eventStartDate, Date eventEndDate, ArrayList<Item> selectedItems, String outfitDescription) {
        Intent intent = new Intent(context, OutfitSelectionActivity.class);
        intent.putExtra(EVENT_ID, eventId);
        intent.putExtra(GUEST_EVENT_UUID, eventGuestUuid);
        intent.putExtra(EVENT_TARGET_DATE, targetDate);
        intent.putExtra(EVENT_START_DATE, eventStartDate);
        intent.putExtra(EVENT_END_DATE, eventEndDate);
        intent.putExtra(SELECTED_ITEMS, selectedItems);
        intent.putExtra(OUTFIT_DESCRIPTION, outfitDescription);
        return intent;
    }

}
