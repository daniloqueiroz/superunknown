package superunknown.jersey.gson;

import java.util.Date;

import org.jvnet.hk2.annotations.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FunctionalInterface
public interface GsonFactory {

    Gson create();

    public static Gson defaultGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Optional.class, new OptionalTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }
}
