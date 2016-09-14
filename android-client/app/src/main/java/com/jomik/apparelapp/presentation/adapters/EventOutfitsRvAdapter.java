package com.jomik.apparelapp.presentation.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.domain.entities.EventGuestOutfit;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.infrastructure.services.ImageHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class EventOutfitsRvAdapter extends RecyclerView.Adapter<EventOutfitsRvAdapter.ViewHolder>{

    private final List<EventGuestOutfit> eventGuestOutfits;
    private Context context;
    private Activity activity;

    public EventOutfitsRvAdapter(List<EventGuestOutfit> eventGuestOutfits, Activity activity) {
        this.eventGuestOutfits = eventGuestOutfits;
        this.activity = activity;
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

        final List<Item> items = eventGuestOutfits.get(i).getItems();
        EventOutfitItemsRvAdapter adapter = new EventOutfitItemsRvAdapter(items);
        holder.recyclerView.setAdapter(adapter);

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items != null && items.size() > 0) {
                    List<SharePhoto> sharePhotos = new ArrayList<SharePhoto>();
                    for (Item item : items) {
                        if (sharePhotos.size() >= 6) break; // Only upload 6 photos max to facebook
                        if (item.getPhoto() == null) continue;
                        Uri uri = android.net.Uri.parse("file://" + new File(item.getPhoto().getPhotoPath()).toString());
                        SharePhoto photo = new SharePhoto.Builder().setImageUrl(uri).build();
                        sharePhotos.add(photo);
                    }
                    SharePhotoContent content = new SharePhotoContent.Builder().addPhotos(sharePhotos).build();
                    ShareDialog shareDialog = new ShareDialog(activity);
                    CallbackManager callbackManager = CallbackManager.Factory.create();
                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            Toast.makeText(context, "Share Successful!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(context, "Share cancelled", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Toast.makeText(context, "Failed to share", Toast.LENGTH_SHORT).show();
                        }
                    });
                    shareDialog.show(activity, content);

                } else {
                    Toast.makeText(context, "No clothing to share!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog(activity);

                ShareLinkContent.Builder shareLinkContentBuilder = new ShareLinkContent.Builder();

                if(items != null && items.size() > 0) {
                    Uri uri = android.net.Uri.parse("file://" + new File(items.get(0).getPhoto().getPhotoPath()).toString());
                    shareLinkContentBuilder.setImageUrl(uri);
                }

                shareLinkContentBuilder
                        .setContentTitle("I like an outfit on Apparel App")
                        .setContentDescription("Apparel is an app that lets you showcase what you are wearing and when you are wearing it!");

                CallbackManager callbackManager = CallbackManager.Factory.create();
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(context, "Thanks for liking!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(context, "Like cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(context, "Failed to like", Toast.LENGTH_SHORT).show();
                    }
                });
                shareDialog.show(activity, shareLinkContentBuilder.build());

            }
        });*/


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
        TextView btnShare;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv);
            txtOutfitDescription = (TextView) itemView.findViewById(R.id.txtOutfitDescription);
            txtUsername = (TextView) itemView.findViewById(R.id.txtUsername);
            profileImage = (SimpleDraweeView) itemView.findViewById(R.id.profileImage);
            btnShare = (TextView) itemView.findViewById(R.id.btnShare);
        }
    }
}
