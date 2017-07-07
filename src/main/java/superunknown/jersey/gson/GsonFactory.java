package superunknown.jersey.gson;

import java.time.Instant;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FunctionalInterface
public interface GsonFactory {

    Gson create();

    public static Gson defaultGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }
}
