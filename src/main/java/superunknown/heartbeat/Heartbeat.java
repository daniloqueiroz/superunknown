package superunknown.heartbeat;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Heartbeat {
    @NonNull
    private String name;
    @Builder.Default
    private boolean isHealthy = false;
    @Builder.Default
    private String message = null;
}