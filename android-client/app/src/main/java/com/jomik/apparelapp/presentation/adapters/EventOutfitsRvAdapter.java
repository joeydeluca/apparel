package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.EventGuestOutfit;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class EventOutfitsRvAdapter extends RecyclerView.Adapter<EventOutfitsRvAdapter.ViewHolder>{

    private final List<EventGuestOutfit> eventGuestOutfits;
    private Context context;

    public EventOutfitsRvAdapter(List<EventGuestOutfit> eventGuestOutfits) {
        this.eventGuestOutfits = eventGuestOutfits;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event_outfit_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        EventGuestOutfit eventGuestOutfit = eventGuestOutfits.get(i);
        holder.txtOutfitDescription.setText(eventGuestOutfit.getDescription());
        holder.txtUsername.setText(eventGuestOutfit.getEventGuest().getGuest().getName());
        ImageHelper.setFacebookProfileImageUri(holder.profileImage, eventGuestOutfit.getEventGuest().getGuest().getFacebookId());

        LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(llm);

        List<Item> items = eventGuestOutfits.get(i).getItems();
        EventOutfitItemsRvAdapter adapter = new EventOutfitItemsRvAdapter(items);
        holder.recyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return eventGuestOutfits.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        RecyclerView recyclerView;
        SimpleDraweeView profileImage;
        TextView txtOutfitDescription;
        TextView txtUsername;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv);
            txtOutfitDescription = (TextView) itemView.findViewById(R.id.txtOutfitDescription);
            txtUsername = (TextView) itemView.findViewById(R.id.txtUsername);
            profileImage = (SimpleDraweeView) itemView.findViewById(R.id.profileImage);
        }
    }
}
