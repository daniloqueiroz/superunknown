package server;

import org.eclipse.jetty.server.Server;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class Runner {

    protected Server server;
    private HandlersBuilder handlersBuilder;

    public Runner(Configuration config, HandlersBuilder handlersBuilder) {
        this.handlersBuilder = handlersBuilder;
        int port = config.getPort();
        this.server = new Server(port);
    }

    protected Runner(HandlersBuilder handlersBuilder, Server server) {
        this.handlersBuilder = handlersBuilder;
        this.server = server;
    }

    /**
     * Stops the running server
     * 
     * @throws Exception
     */
    public void stop() throws Exception {
        if (this.server.isRunning()) {
            this.server.stop();
        } else {
            throw new IllegalStateException("Server isn't running");
        }
    }

    public void start() throws Exception {
        if (!this.server.isRunning()) {
            this.server.setHandler(this.handlersBuilder.createHandler());
            this.server.start();
        } else {
            throw new IllegalStateException("Server is already running");
        }
    }

    public void join() throws InterruptedException {
        this.server.join();
    }

    /*
     * Static Methods - main method and auxiliary methods
     */

    private static Runner getRunner() {
        Configuration config = new Configuration();
        HandlersBuilder handlersBuilder = new HandlersBuilder(config);
        return new Runner(config, handlersBuilder);
    }

    public static void main(String[] args) throws Exception {
        Runner runner = getRunner();
        runner.start();
        runner.join();
    }
}