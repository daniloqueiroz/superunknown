package superunknown.jersey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import superunknown.Log;
import superunknown.jersey.gson.GsonFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonProvider implements MessageBodyWriter<Object>, MessageBodyReader<Object> {

    private Charset charset = StandardCharsets.UTF_8;
    private GsonFactory factory;

    public GsonProvider(GsonFactory factory) {
        this.factory = factory;
    }

    private Gson gson() {
        return this.factory.create();
    }

    @Override
    public boolean isReadable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(
            final Class<Object> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, String> httpHeaders,
            final InputStream entityStream) throws IOException {

        final Type jsonType = type.equals(genericType)? type: genericType;
        try (final Reader reader = new InputStreamReader(entityStream, charset)) {
            return gson().fromJson(reader, jsonType);
        } catch (JsonSyntaxException e) {
            Log.error("Unable to parse json to object", e);
            throw new WebApplicationException(e.getMessage(), e, Response.Status.BAD_REQUEST);
        }
    }

    @Override
    public boolean isWriteable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(
            final Object object,
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(
            final Object object,
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, Object> httpHeaders,
            final OutputStream entityStream) throws IOException{

        final Type jsonType = type.equals(genericType)? type: genericType;
        try (final Writer writer = new OutputStreamWriter(entityStream, charset)) {
            gson().toJson(object, jsonType, writer);
        }
    }
}
