/*
package com.jomik.apparelapp.presentation.adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

*/
/**
 * Created by Joe Deluca on 4/7/2016.
 *//*

public class ItemsCursorAdapter extends CursorAdapter {

    public ItemsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_item_list, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(ApparelContract.Items._ID));

        TextView textView = (TextView) view.findViewById(R.id.clothing_item_list_item_name);
        textView.setText(SqlHelper.getString(cursor, ApparelContract.Items.NAME, DbSchema.PREFIX_TBL_ITEMS));

        textView = (TextView) view.findViewById(R.id.clothing_item_list_item_description);
        textView.setText(SqlHelper.getString(cursor, ApparelContract.Items.DESCRIPTION, DbSchema.PREFIX_TBL_ITEMS));

        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);

        final int itemVersion = SqlHelper.getInt(cursor, ApparelContract.CommonColumns.VERSION, DbSchema.PREFIX_TBL_ITEMS) + 1;

        String photoPath = SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS);
        String photoUuid = SqlHelper.getString(cursor, ApparelContract.Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS);
        if(photoPath != null) {
            ImageHelper.setImageUri(draweeView, photoPath);
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
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        ContentValues values = new ContentValues();
                        values.put(ApparelContract.CommonColumns.MARKED_FOR_DELETE, 1);
                        values.put(ApparelContract.CommonColumns.VERSION, itemVersion);
                        context.getContentResolver().update(ContentUris.withAppendedId(ApparelContract.Items.CONTENT_URI, id), values,  null, null);

                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                        notifyDataSetChanged();

                        break;
                }

                return true;
            }
        });

        view.setTag(id);
    }
}
*/
