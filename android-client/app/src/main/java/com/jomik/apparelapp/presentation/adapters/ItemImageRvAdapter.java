package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class ItemImageRvAdapter extends RecyclerView.Adapter<ItemImageRvAdapter.ItemImageViewHolder>{

    private final List<Item> items;
    private Context context;

    public ItemImageRvAdapter(List<Item> items) {
        this.items = items;
    }


    @Override
    public ItemImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_selection_result_list, viewGroup, false);
        ItemImageViewHolder pvh = new ItemImageViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ItemImageViewHolder holder, final int i) {
        Item item = items.get(i);
        ImageHelper.setImageUri(holder.image, item.getPhotoPathSmall());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ItemImageViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;

        ItemImageViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView)itemView.findViewById(R.id.image);
        }
    }
}
