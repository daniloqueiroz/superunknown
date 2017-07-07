package superunknown.jersey.gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import superunknown.Log;

public class OptionalTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getRawType() == Optional.class && (typeToken.getType() instanceof ParameterizedType)) {
            final Type type = typeToken.getType();
            final Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
            final TypeAdapter<T> elementAdapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(elementType));

            return new OptionalTypeAdapter(elementAdapter);
        } else {
            Log.info("{}", typeToken);
            return null;
        }
    }

    public static class OptionalTypeAdapter<T> extends TypeAdapter<Optional<T>> {
        protected final TypeAdapter<T> elementAdapter;

        protected OptionalTypeAdapter(final TypeAdapter<T> elementAdapter) {
            this.elementAdapter = elementAdapter;
        }

        public void write(JsonWriter out, final Optional<T> value) throws IOException {
            if (value != null && value.isPresent()) {
                elementAdapter.write(out, value.get());
            } else {
                out.nullValue();
            }
        }

        public Optional<T> read(JsonReader in) throws IOException {
            Optional<T> result;
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                result = Optional.empty();
            } else {
                result = Optional.of(elementAdapter.read(in));
            }
            return result;
        }
    }
}
