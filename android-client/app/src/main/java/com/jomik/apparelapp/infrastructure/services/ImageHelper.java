package com.jomik.apparelapp.infrastructure.services;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

/**
 * Created by Joe Deluca on 8/10/2016.
 */
public class ImageHelper {

    public static void setImageUri(SimpleDraweeView simpleDraweeView, String path) {
        simpleDraweeView.setImageURI(android.net.Uri.parse("file://" + new File(path).toString()));
    }

    public static void setFacebookProfileImageUri(SimpleDraweeView simpleDraweeView, String facebookId) {
        simpleDraweeView.setImageURI(Uri.parse(String.format("https://graph.facebook.com/%s/picture?type=large", facebookId)));
    }
}
