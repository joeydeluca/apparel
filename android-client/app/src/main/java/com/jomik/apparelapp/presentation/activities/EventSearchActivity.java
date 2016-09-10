package com.jomik.apparelapp.presentation.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.infrastructure.events.EventSearchComplete;
import com.jomik.apparelapp.infrastructure.events.EventSearchStart;
import com.jomik.apparelapp.infrastructure.rest.RestService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class EventSearchActivity extends AppCompatActivity {

    private static final String TAG = "EventSearchActivity";

    ProgressDialog dialog;
    EditText txtKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        final TextView txtToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        txtKeyword = (EditText) findViewById(R.id.keyword);
        final Button btnSearch = (Button) findViewById(R.id.btn_search);

        txtToolbarTitle.setText("Find an event");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(EventSearchActivity.this, "",
                "Searching...", true);
                dialog.show();

                EventBus.getDefault().post(new EventSearchStart("joeyiscool"));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessage(EventSearchStart eventSearchStart) throws InterruptedException, ParseException, SQLException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestService.BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        RestService restService = retrofit.create(RestService.class);

        List<Event> events = null;
        try {
            Response<List<Event>> response = restService.searchEvents(txtKeyword.getText().toString()).execute();
            if(!response.isSuccessful())  {
                String message = "Response not successful. Code: " + response.code() + ".";
                if(response.errorBody() != null) {
                    message = message + " " + response.errorBody().string();
                }
                throw new IOException(message);
            }

            events = response.body();

            Log.i(TAG, "Events found: " + events.size());

            EventBus.getDefault().post(new EventSearchComplete(events));

        } catch (IOException e) {
            dialog.hide();
            Log.e(TAG, "Could not reach server.", e);
            e.printStackTrace();
            Toast.makeText(EventSearchActivity.this, "Could not reach server. Please try again in a few minutes.", Toast.LENGTH_LONG).show();
            return;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(EventSearchComplete event) {
        dialog.hide();
        EventBus.getDefault().postSticky(event.getSearchResults());
        Intent intent = new Intent(getApplicationContext(), EventSearchResultsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


}
