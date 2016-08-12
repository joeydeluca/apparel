package com.jomik.apparelapp.presentation.validator;

import android.view.View;

/**
 * Created by Joe Deluca on 7/16/2016.
 */
public class FormValidator {

    public static boolean validate(View... views) {
        boolean validateSuccessful = true;

        for(View view : views) {
            Validator validator = ValidatorFactory.getValidator(view);
            if(!validator.validate(view)) {
                validateSuccessful = false; // return false of at least one validator failed
            }
        }

        return validateSuccessful;
    }
}