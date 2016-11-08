package com.wehelp.wehelp.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Rael on 30/10/2016.
 */
public class GsonUTCDateAdapter implements JsonSerializer<Date>,JsonDeserializer<Date> {

    private final List<DateFormat> datesFormats = new ArrayList<DateFormat>();

    public GsonUTCDateAdapter() {
        datesFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US));      //This is the format I need
        datesFormats.add(new SimpleDateFormat("yyyy-MM-dd", Locale.US));
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));                               //This is the key line which converts the date to UTC which cannot be accessed with the default serializer
    }

    @Override public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        for (DateFormat dateFormat : datesFormats ) {
            try {
                dateFormat.parse(date.toString());
                return new JsonPrimitive(dateFormat.format(date));
            } catch (ParseException e) {
            }
        }
        throw new JsonParseException("Erro ao serializar data");
    }

    @Override public synchronized Date deserialize(JsonElement jsonElement,Type type,JsonDeserializationContext jsonDeserializationContext) {
        for (DateFormat dateFormat : datesFormats ) {
            try {
                return dateFormat.parse(jsonElement.getAsString());
            } catch (ParseException e) {
            }
        }
        throw new JsonParseException("Erro ao desserializar data");
    }
}
