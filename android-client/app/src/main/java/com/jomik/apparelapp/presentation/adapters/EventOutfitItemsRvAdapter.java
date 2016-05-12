package com.jomik.apparelapp.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.domain.entities.usereventoutfit.UserEventOutfit;

import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class EventOutfitItemsRvAdapter extends RecyclerView.Adapter<EventOutfitItemsRvAdapter.ViewHolder>{

    private final List<Item> items;
    private Context context;

    public EventOutfitItemsRvAdapter(List<Item> items) {
        this.items = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event_outfit_item_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        /*holder.title.setText(userEventOutfits.get(i).getTitle());
        holder.location.setText(events.get(i).getLocation());*/
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
       /* TextView title;
        TextView location;
        ImageView btnMenu;*/

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
           /* title = (TextView)itemView.findViewById(R.id.title);
            location = (TextView)itemView.findViewById(R.id.location);
            btnMenu = (ImageView) itemView.findViewById(R.id.btnMenu);*/
        }
    }
}
