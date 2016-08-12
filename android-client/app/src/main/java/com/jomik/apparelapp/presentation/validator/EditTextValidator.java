package com.jomik.apparelapp.presentation.validator;

import android.widget.EditText;

/**
 * Created by Joe Deluca on 7/16/2016.
 */
public class EditTextValidator implements Validator<EditText> {

    private static Validator instance;

    private EditTextValidator() {}

    @Override
    public boolean validate(EditText editText) {
        if(editText.getText().toString() == null || editText.getText().toString().trim().isEmpty()) {
            editText.setError("Required");
            return false;
        }

        return true;
    }

    public static Validator<EditText> getInstance() {
        if(instance == null) {
            instance = new EditTextValidator();
        }
        return instance;
    }
}
