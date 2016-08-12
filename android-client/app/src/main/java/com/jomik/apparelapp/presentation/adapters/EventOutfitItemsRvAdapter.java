package com.jomik.apparelapp.presentation.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class EventOutfitItemsRvAdapter extends RecyclerView.Adapter<EventOutfitItemsRvAdapter.ViewHolder>{

    private final List<Item> items;

    public EventOutfitItemsRvAdapter(List<Item> items) {
        this.items = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event_outfit_item_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        Item item = items.get(i);
        ImageHelper.setImageUri(holder.simpleDraweeView, item.getPhotoPath(), item.getPhotoUuid());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        SimpleDraweeView simpleDraweeView;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.image);
        }
    }
}
