package superunknown;

import java.util.concurrent.CompletableFuture;

import javax.ws.rs.container.AsyncResponse;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RestResource {
    public static <T> void dispatch(AsyncResponse async, CompletableFuture<T> result) {
        result.thenAccept(resp -> async.resume(resp));
    }
}
