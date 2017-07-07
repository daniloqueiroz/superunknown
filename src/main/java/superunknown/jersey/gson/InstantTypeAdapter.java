package superunknown.jersey.gson;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

final class InstantTypeAdapter extends TypeAdapter<Instant> {

    private final DateTimeFormatter dtFormat = DateTimeFormatter.ISO_INSTANT;

    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(dtFormat.format(value));
        }
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        Instant result;
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            result = null;
        } else {
            TemporalAccessor date = dtFormat.parse(in.nextString());
            result = Instant.from(date);
        }

        return result;
    }
}