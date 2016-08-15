package com.jomik.apparelapp.presentation.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.domain.entities.photo.Photo;
import com.jomik.apparelapp.domain.entities.user.User;
import com.jomik.apparelapp.domain.entities.usereventoutfit.UserEventOutfit;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.EventGuestOutfitItems;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.EventGuestOutfits;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.EventGuests;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.Items;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.Users;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.Photos;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.adapters.EventOutfitsRvAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewEventOutfitsActivity extends AppCompatActivity {

    TextView txtDate;
    ImageView btnBack;
    ImageView btnForward;

    private DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_outfits);

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        txtToolbarTitle.setText("Guest outfits");

        final ImageView imgCancel = (ImageView) findViewById(R.id.toolbar_cancel_button);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtDate = (TextView) findViewById(R.id.date);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnForward = (ImageView) findViewById(R.id.btnForward);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        final String eventId = getIntent().getStringExtra("eventId");
        final Date startDate = (Date) getIntent().getSerializableExtra("startDate");
        final Date endDate = (Date) getIntent().getSerializableExtra("endDate");
        final Date targetDate = (Date) getIntent().getSerializableExtra("targetDate");

        DatePage datePage;
        if(targetDate != null) {
            datePage = new DatePage(startDate, endDate, targetDate);
        } else {
            datePage = new DatePage(startDate, endDate);
        }

        setData(eventId, datePage);
    }

    private void setData(final String eventId, final DatePage datePage) {
        txtDate.setText(datePage.getDisplayTargetDate());

        mDatePickerDialog = createDatePickerDialog(eventId, datePage);

        String myEventGuestUuid = null;
        final Set<Item> mySelectedItems = new HashSet<>();
        String myOutfitDescription = null;

        String[] select = new String[] {
                SqlHelper.getSelectColumn(EventGuests.UUID, DbSchema.PREFIX_TBL_EVENT_GUESTS),
                SqlHelper.getSelectColumn(EventGuests.EVENT_UUID, DbSchema.PREFIX_TBL_EVENT_GUESTS),
                SqlHelper.getSelectColumn(EventGuests.GUEST_UUID, DbSchema.PREFIX_TBL_EVENT_GUESTS),

                SqlHelper.getSelectColumn(Users.UUID, DbSchema.PREFIX_TBL_USERS),
                SqlHelper.getSelectColumn(Users.NAME, DbSchema.PREFIX_TBL_USERS),
                SqlHelper.getSelectColumn(Users.FACEBOOK_ID, DbSchema.PREFIX_TBL_USERS),

                SqlHelper.getSelectColumn(EventGuestOutfits.UUID, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS),
                SqlHelper.getSelectColumn(EventGuestOutfits.DESCRIPTION, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS),
                SqlHelper.getSelectColumn(EventGuestOutfits.EVENT_DATE, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS),
                SqlHelper.getSelectColumn(EventGuestOutfits.EVENT_GUEST_UUID, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS),
                SqlHelper.getSelectColumn(EventGuestOutfits.PHOTO_UUID, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS),

                SqlHelper.getSelectColumn(EventGuestOutfitItems.UUID, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFIT_ITEMS),
                SqlHelper.getSelectColumn(EventGuestOutfitItems.ITEM_UUID, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFIT_ITEMS),
                SqlHelper.getSelectColumn(EventGuestOutfitItems.EVENT_GUEST_OUTFIT_UUID, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFIT_ITEMS),

                SqlHelper.getSelectColumn(Items.UUID, DbSchema.PREFIX_TBL_ITEMS),
                SqlHelper.getSelectColumn(Items.NAME, DbSchema.PREFIX_TBL_ITEMS),
                SqlHelper.getSelectColumn(Items.PHOTO_UUID, DbSchema.PREFIX_TBL_ITEMS),

                SqlHelper.getSelectColumn(Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS),
                SqlHelper.getSelectColumn(Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS),
                SqlHelper.getSelectColumn(Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS),
        };

        Uri uri = EventGuests.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, select, DbSchema.PREFIX_TBL_EVENTS + "._id = ? and (" + DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS + ".event_date is null or " + DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS + ".event_date = ?)", new String[]{SqlHelper.getDateForDb(datePage.getDisplayTargetDate()), eventId, SqlHelper.getDateForDb(datePage.getDisplayTargetDate())}, null);

        Map<String, UserEventOutfit> EventOutfitMap = new HashMap<>();

        while(cursor.moveToNext()) {
            System.out.println("===============================");
            for(int i = 0; i < cursor.getColumnCount() - 1; i++){
                System.out.println(cursor.getColumnName(i) + ": " + cursor.getString(i));
            }
            System.out.println("===============================");

            String eventOutfitUuid = SqlHelper.getString(cursor, EventGuestOutfits.UUID, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS);
            UserEventOutfit eventOutfit = EventOutfitMap.get(eventOutfitUuid);

            if(eventOutfit == null) {
                eventOutfit = new UserEventOutfit();
                eventOutfit.setUser(getUser(cursor));
                eventOutfit.setDate(SqlHelper.getString(cursor, EventGuestOutfits.EVENT_DATE, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS));
                eventOutfit.setDescription(SqlHelper.getString(cursor, EventGuestOutfits.DESCRIPTION, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS));
                EventOutfitMap.put(eventOutfitUuid, eventOutfit);

                if(AuthenticationManager.getAuthenticatedUser(this).getUuid().equals(eventOutfit.getUser().getUuid())) {
                    myEventGuestUuid = SqlHelper.getString(cursor, EventGuests.UUID, DbSchema.PREFIX_TBL_EVENT_GUESTS);
                    myOutfitDescription = SqlHelper.getString(cursor, EventGuestOutfits.DESCRIPTION, DbSchema.PREFIX_TBL_EVENT_GUEST_OUTFITS);
                }
            }

            List<Item> items = eventOutfit.getItems();
            if(items == null) {
                items = new ArrayList<>();
            }
            eventOutfit.setItems(items);

            String itemUuid = SqlHelper.getString(cursor, Items.UUID, DbSchema.PREFIX_TBL_ITEMS);
            if(itemUuid != null && !itemUuid.isEmpty()) {
                items.add(getItem(cursor));
            }

            if(AuthenticationManager.getAuthenticatedUser(this).getUuid().equals(eventOutfit.getUser().getUuid())) {
                mySelectedItems.addAll(items);
            }

        }


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        EventOutfitsRvAdapter adapter = new EventOutfitsRvAdapter(new ArrayList<>(EventOutfitMap.values()));
        rv.setAdapter(adapter);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        final String finalMyEventGuestUuid = myEventGuestUuid;
        final String finalMyOutfitDescription = myOutfitDescription;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        OutfitSelectionActivity.getIntent(getApplicationContext(), eventId, finalMyEventGuestUuid, datePage.getTargetDate(), datePage.getEventStartDate(), datePage.getEventEndDate(), new ArrayList<>(mySelectedItems), finalMyOutfitDescription)
                );
            }
        });

        final DatePage previousPage = datePage.getPreviousPage();
        if(previousPage != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setData(eventId, previousPage);
                }
            });
            btnBack.setVisibility(View.VISIBLE);
        } else {
            btnBack.setVisibility(View.INVISIBLE);
        }

        final DatePage nextPage = datePage.getNextPage();
        if(nextPage != null) {
            btnForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setData(eventId, nextPage);
                }
            });
            btnForward.setVisibility(View.VISIBLE);
        } else {
            btnForward.setVisibility(View.INVISIBLE);
        }
    }

    private User getUser(Cursor cursor) {
        User user = new User();
        user.setUuid(SqlHelper.getString(cursor, Users.UUID, DbSchema.PREFIX_TBL_USERS));
        user.setName(SqlHelper.getString(cursor, Users.NAME, DbSchema.PREFIX_TBL_USERS));
        user.setFacebookId(SqlHelper.getString(cursor, Users.FACEBOOK_ID, DbSchema.PREFIX_TBL_USERS));

        return user;
    }

    private Event getEvent(Cursor cursor) {
        return new Event();
    }

    private Item getItem(Cursor cursor) {
        Item item = new Item();
        item.setUuid(SqlHelper.getString(cursor, Items.UUID, DbSchema.PREFIX_TBL_ITEMS));
        item.setName(SqlHelper.getString(cursor, Items.NAME, DbSchema.PREFIX_TBL_ITEMS));
        item.setPhotoUuid(SqlHelper.getString(cursor, Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS));
        item.setPhotoPath(SqlHelper.getString(cursor, Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS));
        item.setPhotoPathSmall(SqlHelper.getString(cursor, Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS));


        return item;
    }

    private DatePickerDialog createDatePickerDialog(final String eventId, final DatePage datePage) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(datePage.getTargetDate());

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date selectedDate = calendar.getTime();

                if(selectedDate.before(datePage.getEventStartDate()) || selectedDate.after(datePage.getEventEndDate())) {
                    Toast.makeText(getApplicationContext(), "Selected date is outside event date", Toast.LENGTH_SHORT).show();
                } else {
                    setData(eventId, new DatePage(datePage.getEventStartDate(), datePage.getEventEndDate(), selectedDate));
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(datePage.eventStartDate.getTime());
        datePickerDialog.getDatePicker().setMaxDate(datePage.eventEndDate.getTime());

        return datePickerDialog;
    }

    final class DatePage {
        private Date eventStartDate;
        private Date eventEndDate;
        private Date targetDate;
        private Date previousDate = null;
        private Date nextDate = null;

        public DatePage(Date eventStartDate, Date eventEndDate) {
            this.eventStartDate = eventStartDate;
            this.eventEndDate = eventEndDate;
            setTargetDate();
            setPreviousDate();
            setNextDate();
        }

        public DatePage(Date eventStartDate, Date eventEndDate, Date targetDate) {
            this.eventStartDate = eventStartDate;
            this.eventEndDate = eventEndDate;
            this.targetDate = targetDate;
            setPreviousDate();
            setNextDate();
        }

        private void setTargetDate() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date today = calendar.getTime();

            if((today.equals(eventStartDate) || today.before(eventEndDate)) &&
                    (today.equals(eventEndDate) || today.after(eventStartDate))) {
                this.targetDate = today;
            } else {
                this.targetDate = eventStartDate;
            }
        }

        private void setPreviousDate() {
            if(targetDate.after(eventStartDate)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(targetDate);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                this.previousDate = calendar.getTime();
            }
        }

        private void setNextDate() {
            if(targetDate.before(eventEndDate)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(targetDate);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                this.nextDate = calendar.getTime();
            }
        }

        public String getDisplayTargetDate() {
            return SqlHelper.dateFormatForDisplay.format(targetDate);
        }

        public DatePage getPreviousPage() {
            if(previousDate == null) return null;
            return new DatePage(eventStartDate, eventEndDate, previousDate);
        }

        public DatePage getNextPage() {
            if(nextDate == null) return null;
            return new DatePage(eventStartDate, eventEndDate, nextDate);
        }

        public Date getEventStartDate() {
            return eventStartDate;
        }

        public Date getEventEndDate() {
            return eventEndDate;
        }

        public Date getTargetDate() {
            return targetDate;
        }
    }

}