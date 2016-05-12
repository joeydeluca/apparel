package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.usereventoutfit.UserEventOutfit;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.event.EventsRepository;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;
import com.jomik.apparelapp.presentation.activities.EditEventActivity;
import com.jomik.apparelapp.presentation.activities.ViewEventActivity;

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
        /*holder.title.setText(userEventOutfits.get(i).getTitle());
        holder.location.setText(events.get(i).getLocation());*/

        LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(llm);

        EventOutfitItemsRvAdapter adapter = new EventOutfitItemsRvAdapter(userEventOutfits.get(i).getOutfit().getItems());
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
       /* TextView title;
        TextView location;
        ImageView btnMenu;*/

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv);
           /* title = (TextView)itemView.findViewById(R.id.title);
            location = (TextView)itemView.findViewById(R.id.location);
            btnMenu = (ImageView) itemView.findViewById(R.id.btnMenu);*/
        }
    }
}
