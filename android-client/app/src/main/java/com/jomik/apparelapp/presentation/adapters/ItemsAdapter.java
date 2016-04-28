package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.item.Item;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class ItemsAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final List<Item> items;

    public ItemsAdapter(Context context, List<Item> items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_item_list, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.clothing_item_list_item_name);
        textView.setText(items.get(position).getName());

        textView = (TextView) rowView.findViewById(R.id.clothing_item_list_item_description);
        textView.setText(items.get(position).getDescription());

        Uri uri = Uri.parse("asset://pic.png");
        SimpleDraweeView draweeView = (SimpleDraweeView) rowView.findViewById(R.id.my_image_view);
        draweeView.setImageURI(uri);

        rowView.setTag(items.get(position).getId());

        return rowView;
    }
}
