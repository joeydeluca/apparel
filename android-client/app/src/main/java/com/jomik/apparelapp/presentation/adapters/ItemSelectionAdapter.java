package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.util.Set;

/**
 * Created by Joe Deluca on 7/28/2016.
 */
public class ItemSelectionAdapter extends BaseAdapter {
    private Context context;
    private final Item[] items;
    private final Set<Item> selectedItems;

    public ItemSelectionAdapter(Context context, Item[] items, Set<Item> selectedItems) {
        this.context = context;
        this.items = items;
        this.selectedItems = selectedItems;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if(convertView == null) {
            gridView = inflater.inflate(R.layout.row_item_grid, null);

            Item item = items[position];
            SimpleDraweeView draweeView = (SimpleDraweeView) gridView.findViewById(R.id.grid_item_image);
            ImageHelper.setImageUri(draweeView, item. getPhoto().getPhotoPath());

            if(selectedItems.contains(item)) {
                draweeView.setBackgroundResource(R.drawable.com_facebook_button_like_icon_selected);
            }

        } else {
            gridView = convertView;
        }

        return gridView;

    }
}
