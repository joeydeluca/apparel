package com.jomik.apparelapp.presentation.validator;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Joe Deluca on 7/16/2016.
 */
public class ValidatorFactory {

    public static Validator getValidator(View view) {
        if(view instanceof EditText) {
            return EditTextValidator.getInstance();
        }
        else if(view instanceof Spinner) {
           return SpinnerValidator.getInstance();
        }

        throw new IllegalArgumentException("No validator found for "
                + view.getClass().getSimpleName().toString());
    }
}
