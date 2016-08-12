package com.jomik.apparelapp.presentation.validator;

import android.view.View;

/**
 * Created by Joe Deluca on 7/16/2016.
 *
 * A generic interface for validating input type Views such as EditTexts and Spinners.
 * This may be a bit over engineered, let's see how it work in practice.
 */
public interface Validator<T> {
    /**
     * Perform validation on a view
     * @param t
     * @return a flag indicating if the validation passed or not
     */
    boolean validate(T t);

    /**
     * Message to display on the View if validations fail
     */
    String requiredFieldMessage = "Required";
}
