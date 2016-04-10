package com.jomik.apparelapp.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.item.ItemsRepository;
import com.jomik.apparelapp.infrastructure.settings.Setting;
import com.jomik.apparelapp.infrastructure.settings.SettingsUtil;
import com.jomik.apparelapp.presentation.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ItemsRepository itemsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemsRepository = RepositoryFactory.createItemsRepository(RepositoryFactory.Type.IN_MEMORY);

        SettingsUtil.getSettingValue(Setting.MODE, getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.listview);
        final List values = itemsRepository.findAll();

        final ArrayAdapter adapter = new ItemsAdapter(getApplicationContext(), values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Item item = (Item) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), TabbedActivity.class);
                                startActivity(intent);
                            }
                        });
            }
        });



        //Fresco.initialize(this);

       /* Uri uri = Uri.parse("asset://pic.png");
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        draweeView.setImageURI(uri);*/




    }

}
