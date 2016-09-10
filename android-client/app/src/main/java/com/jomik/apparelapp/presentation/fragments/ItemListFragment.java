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

import com.j256.ormlite.stmt.QueryBuilder;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.activities.EditItemActivity;
import com.jomik.apparelapp.presentation.activities.FacebookLoginActivity;
import com.jomik.apparelapp.presentation.adapters.ItemsAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Deluca on 4/10/2016.
 */
public class ItemListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(AuthenticationManager.getAuthenticatedUser(getContext()) == null) {
            Intent intent = new Intent(getActivity().getApplicationContext(), FacebookLoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

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

        setData();

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), EditItemActivity.class);
        intent.putExtra("id", view.getTag().toString());
        startActivity(intent);
    }

    private void setData() {
        List<Item> items = new ArrayList<>();
        OrmLiteSqlHelper helper = new OrmLiteSqlHelper(getContext());
        try {
            QueryBuilder<User, String> userQb = helper.getUserDao().queryBuilder();
            userQb.where().eq("uuid", AuthenticationManager.getAuthenticatedUser(getContext()).getUuid());
            items.addAll(helper.getItemDao().queryBuilder().join(userQb).where().eq("marked_for_delete", false).query());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapter = new ItemsAdapter(getContext(), items);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

}