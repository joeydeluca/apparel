package com.jomik.apparelapp.presentation.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.stmt.QueryBuilder;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.EventGuest;
import com.jomik.apparelapp.domain.entities.EventGuestOutfit;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.adapters.EventOutfitsRvAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewEventOutfitsActivity extends AppCompatActivity {

    TextView txtDate;
    ImageView btnBack;
    ImageView btnForward;

    private DatePickerDialog mDatePickerDialog;
    private FloatingActionButton addButton;

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

        addButton = (FloatingActionButton) findViewById(R.id.addButton);

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

        String myOutfitDescription = null;
        EventGuest myEventGuest = null;

        List<EventGuestOutfit> outfits = new ArrayList<>();
        OrmLiteSqlHelper helper  = new OrmLiteSqlHelper(getApplicationContext());
        try {
            QueryBuilder<EventGuestOutfit, String> outfitQb = helper.getEventGuestOutfitDao().queryBuilder();
            outfitQb.where().eq("event_date", datePage.getTargetDate());

            QueryBuilder<Event, String> eventQb = helper.getEventDao().queryBuilder();
            eventQb.where().eq("uuid", eventId);

            QueryBuilder<EventGuest, String> eventGuestQb = helper.getEventGuestDao().queryBuilder();
            eventGuestQb.where().eq("marked_for_delete", false);

            outfits = outfitQb.join(eventGuestQb.join(eventQb)).query();

            myEventGuest = helper.getEventGuestDao().queryBuilder().where().eq("event_uuid", eventId).and().eq("guest_uuid", AuthenticationManager.getAuthenticatedUser(this).getUuid()).queryForFirst();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ArrayList<Item> mySelectedItems = new ArrayList<>();

        for(EventGuestOutfit outfit : outfits) {
            if(outfit.getEventGuest().getGuest().getUuid().equals(AuthenticationManager.getAuthenticatedUser(this).getUuid())) {
                myOutfitDescription = outfit.getDescription();
                mySelectedItems.addAll(outfit.getItems());
                break;
            }
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        EventOutfitsRvAdapter adapter = new EventOutfitsRvAdapter(outfits);
        rv.setAdapter(adapter);

        if(myEventGuest != null) {
            final String finalMyEventGuestUuid = myEventGuest.getUuid();
            final String finalMyOutfitDescription = myOutfitDescription;
            final EventGuest finalMyEventGuest = myEventGuest;
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(
                            OutfitSelectionActivity.getIntent(getApplicationContext(), eventId, finalMyEventGuestUuid, datePage.getTargetDate(), datePage.getEventStartDate(), datePage.getEventEndDate(), mySelectedItems, finalMyOutfitDescription, finalMyEventGuest)
                    );
                }
            });
            addButton.setVisibility(View.VISIBLE);
        } else {
            addButton.setVisibility(View.INVISIBLE);
        }

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