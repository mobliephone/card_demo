package com.framework.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 13971 on 2017/7/13.
 */

public class DateAdapter implements JsonDeserializer<Date> {
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return df.parse(json.getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
    }
}
