package com.jomik.apparelapp.presentation.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.List;

/**
 * Created by Joe Deluca on 4/10/2016.
 */
public class ItemListFragment extends ListFragment implements AdapterView.OnItemClickListener {

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
                Snackbar.make(view, "Replace with your own action fvdfvdf", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ItemsRepository itemsRepository = RepositoryFactory.createItemsRepository(RepositoryFactory.Type.IN_MEMORY);

        //ListView listView = (ListView) findViewById(R.id.listview);
        final List values = itemsRepository.findAll();

        final ArrayAdapter adapter = new ItemsAdapter(getActivity().getApplicationContext(), values);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    public void addItem() {

    }

}