package com.jomik.apparelapp.presentation.validator;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.kbeanie.multipicker.api.entity.ChosenImage;

/**
 * Created by Joe Deluca on 8/9/2016.
 */
public class ImageValidator {

    public static boolean validate(Context context, Uri chosenImage) {
        if(chosenImage == null) {
            Toast.makeText(context, "Please select image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
