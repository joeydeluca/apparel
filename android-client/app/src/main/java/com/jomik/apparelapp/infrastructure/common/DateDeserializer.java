package com.jomik.apparelapp.infrastructure.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Joe Deluca on 10/12/2016.
 */

public class DateDeserializer  extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String dateAsString = jsonparser.getText();
        try {
            Date date = SqlHelper.dateFormatForDb.parse(dateAsString);
            return date;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}