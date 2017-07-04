package superunknown.heartbeat;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Heartbeat {
    @NonNull
    private String name;
    private boolean isHealthy = false;
    private String message = "";
}