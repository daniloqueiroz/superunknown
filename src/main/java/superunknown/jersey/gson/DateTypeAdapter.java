package superunknown.jersey.gson;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

final class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    /**
     * https://www.w3.org/TR/NOTE-datetime
     */
    public static final String W3C_DATETIME = "yyyy-MM-dd'T'HH:mm:ssZ";

    private final DateFormat dtFormat;

    public DateTypeAdapter() {
        this(W3C_DATETIME);
    }

    public DateTypeAdapter(String format) {
        this(format, Locale.US);
    }

    public DateTypeAdapter(String format, Locale locale) {
        this.dtFormat = new SimpleDateFormat(format, Locale.US);
    }

    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        String dateFormatAsString = dtFormat.format(src);
        return new JsonPrimitive(dateFormatAsString);
    }

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        Date date = deserializeToDate(json);
        if (typeOfT == Date.class) {
            return date;
        } else if (typeOfT == Timestamp.class) {
            return new Timestamp(date.getTime());
        } else {
            throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
        }
    }

    private Date deserializeToDate(JsonElement json) {
        try {
            return dtFormat.parse(json.getAsString());
        } catch (ParseException e) {
            throw new JsonSyntaxException(json.getAsString(), e);
        }
    }
}