package superunknown.jersey;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.model.ModelProcessor;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.glassfish.jersey.server.model.ResourceModel;

public class JsonModelProcessor implements ModelProcessor {

    @Override
    public ResourceModel processResourceModel(ResourceModel resourceModel, Configuration configuration) {
        ResourceModel.Builder modelBuilder = new ResourceModel.Builder(false);
        for (Resource r: resourceModel.getResources()) {
            final Resource.Builder resourceBuilder = Resource.builder();
            for (ResourceMethod m: r.getAllMethods()) {
                resourceBuilder
                        .addMethod(m)
                        .suspended(60, TimeUnit.SECONDS)
                        .consumes(MediaType.APPLICATION_JSON)
                        .produces(MediaType.APPLICATION_JSON);
            }
            modelBuilder.addResource(resourceBuilder.build());
        }
        return modelBuilder.build();
    }

    @Override
    public ResourceModel processSubResource(ResourceModel subResourceModel, Configuration configuration) {
        return subResourceModel;
    }

}
