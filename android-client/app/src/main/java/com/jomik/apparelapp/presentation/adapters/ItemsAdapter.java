package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.sql.SQLException;
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
        View view = inflater.inflate(R.layout.row_item_list, parent, false);

        final Item item = items.get(position);

        TextView textView = (TextView) view.findViewById(R.id.clothing_item_list_item_name);
        textView.setText(item.getName());

        textView = (TextView) view.findViewById(R.id.clothing_item_list_item_description);
        textView.setText(item.getDescription());

        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);

        String photoPath = item.getPhoto().getPhotoPath();
        if(photoPath != null) {
            ImageHelper.setImageUri(draweeView, item.getPhoto());
        }

        ImageView btnMenu = (ImageView) view.findViewById(R.id.btnMenu);
        final PopupMenu popup = new PopupMenu(context, btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });
        popup.getMenu().add(1, R.id.menu_delete, 2, "Delete");
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_delete:
                        OrmLiteSqlHelper helper = new OrmLiteSqlHelper(context);
                        item.setMarkedForDelete(true);
                        try {
                            helper.getItemDao().update(item);

                            items.remove(item);
                            notifyDataSetChanged();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();



                        break;
                }

                return true;
            }
        });

        view.setTag(item.getUuid());

        return view;
    }
}
