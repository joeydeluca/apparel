package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class ItemsCursorAdapter extends CursorAdapter {

    public ItemsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_item_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.clothing_item_list_item_name);
        textView.setText(SqlHelper.getString(cursor, ApparelContract.Items.NAME, DbSchema.PREFIX_TBL_ITEMS));

        textView = (TextView) view.findViewById(R.id.clothing_item_list_item_description);
        textView.setText(SqlHelper.getString(cursor, ApparelContract.Items.DESCRIPTION, DbSchema.PREFIX_TBL_ITEMS));

        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);

        String photoPath = SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS);
        String photoUuid = SqlHelper.getString(cursor, ApparelContract.Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS);
        if(photoPath != null) {
            ImageHelper.setImageUri(draweeView, photoPath, photoUuid);
        }

        view.setTag(cursor.getLong(cursor.getColumnIndexOrThrow(ApparelContract.Items._ID)));
    }
}
