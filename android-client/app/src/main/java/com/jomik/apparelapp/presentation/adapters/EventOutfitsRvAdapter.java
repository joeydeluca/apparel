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
import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.domain.entities.usereventoutfit.UserEventOutfit;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class EventOutfitsRvAdapter extends RecyclerView.Adapter<EventOutfitsRvAdapter.ViewHolder>{

    private final List<UserEventOutfit> userEventOutfits;
    private Context context;

    public EventOutfitsRvAdapter(List<UserEventOutfit> userEventOutfits) {
        this.userEventOutfits = userEventOutfits;
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
        UserEventOutfit userEventOutfit = userEventOutfits.get(i);
        holder.txtOutfitDescription.setText(userEventOutfit.getDescription());
        holder.txtUsername.setText(userEventOutfit.getUser().getName());
        ImageHelper.setFacebookProfileImageUri(holder.profileImage, userEventOutfit.getUser().getFacebookId());

        LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(llm);

        EventOutfitItemsRvAdapter adapter = new EventOutfitItemsRvAdapter(userEventOutfits.get(i).getItems());
        holder.recyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return userEventOutfits.size();
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
