package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class ItemImageAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final List<Item> items;

    public ItemImageAdapter(Context context, List<Item> items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_item_selection_result_list, parent, false);

        Item item = items.get(position);
        SimpleDraweeView draweeView = (SimpleDraweeView) rowView.findViewById(R.id.image);
        ImageHelper.setImageUri(draweeView, item.getPhoto());

        rowView.setTag(items.get(position).getUuid());

        return rowView;
    }
}
