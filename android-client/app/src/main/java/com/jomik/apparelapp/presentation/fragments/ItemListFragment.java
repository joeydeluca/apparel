package com.jomik.apparelapp.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.item.ItemsRepository;
import com.jomik.apparelapp.presentation.ItemsAdapter;
import com.jomik.apparelapp.presentation.activities.EditItemActivity;

import java.util.List;

/**
 * Created by Joe Deluca on 4/10/2016.
 */
public class ItemListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayAdapter adapter;

    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_list_fragment, container, false);

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditItemActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ItemsRepository itemsRepository = RepositoryFactory.getItemsRepository(RepositoryFactory.Type.IN_MEMORY);

        //ListView listView = (ListView) findViewById(R.id.listview);
        final List values = itemsRepository.findAll();

        adapter = new ItemsAdapter(getActivity().getApplicationContext(), values);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), EditItemActivity.class);
        intent.putExtra("id", view.getTag().toString());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();


    }
}