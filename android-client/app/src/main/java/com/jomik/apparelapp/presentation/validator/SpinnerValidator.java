package com.jomik.apparelapp.presentation.validator;

import android.widget.Spinner;

/**
 * Created by Joe Deluca on 7/16/2016.
 */
public class SpinnerValidator implements Validator<Spinner> {

    private static Validator instance;
    private static final String PLEASE_CHOOSE = "Please Choose";

    private SpinnerValidator() {}

    public boolean validate(Spinner spinner) {
        if(PLEASE_CHOOSE.equals(spinner.getSelectedItem().toString())) {
            spinner.setPrompt("Required");
            return false;
        }

        return true;
    }

    public static Validator<Spinner> getInstance() {
        if(instance == null) {
            instance = new SpinnerValidator();
        }
        return instance;
    }

}
