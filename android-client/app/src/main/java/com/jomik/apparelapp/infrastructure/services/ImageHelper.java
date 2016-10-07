package com.jomik.apparelapp.infrastructure.services;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jomik.apparelapp.domain.entities.Photo;
import com.jomik.apparelapp.infrastructure.rest.RestService;

import java.io.File;

/**
 * Created by Joe Deluca on 8/10/2016.
 */
public class ImageHelper {

    public static void setImageUri(SimpleDraweeView simpleDraweeView, Photo photo) {
        if(photo.getPhotoPath() != null) {
            File file = new File(photo.getPhotoPath());
            if(file.exists()) {
                simpleDraweeView.setImageURI(Uri.parse("file://" + file.toString()));
                return;
            }
        }

        simpleDraweeView.setImageURI(Uri.parse(RestService.BASE_URL + "/photos/" + photo.getUuid()));
    }

    public static void setFacebookProfileImageUri(SimpleDraweeView simpleDraweeView, String facebookId) {
        simpleDraweeView.setImageURI(Uri.parse(String.format("https://graph.facebook.com/%s/picture?type=large", facebookId)));
    }
}
