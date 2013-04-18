package server;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.resetAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Server.class)
public class RunnerTest {

    private Server serverMock = createMock(Server.class);
    private HandlersBuilder handlersBuilderMock = createMock(HandlersBuilder.class);
    private Runner runner;

    public RunnerTest() {
        this.runner = new Runner(this.handlersBuilderMock, this.serverMock);
    }

    @Before
    public void setUp() {
        resetAll();
    }

    @Test
    public void stopsRunningServer() throws Exception {
        expect(this.serverMock.isRunning()).andReturn(true);
        this.serverMock.stop();
        replayAll();
        this.runner.stop();
        verifyAll();
    }

    @Test(expected = IllegalStateException.class)
    public void triesToStopNotRunningServer() throws Exception {
        expect(this.serverMock.isRunning()).andReturn(false);
        replayAll();
        this.runner.stop();
        verifyAll();
    }

    @Test(expected = IllegalStateException.class)
    public void triesToStartRunningServer() throws Exception {
        expect(this.serverMock.isRunning()).andReturn(true);
        replayAll();
        this.runner.start();
        verifyAll();
    }

    @Test
    public void startRunningServer() throws Exception {
        Handler handler = new RequestLogHandler();
        expect(this.serverMock.isRunning()).andReturn(false);
        expect(this.handlersBuilderMock.createHandler()).andReturn(handler);
        this.serverMock.setHandler(handler);
        this.serverMock.start();
        replayAll();
        this.runner.start();
        verifyAll();
    }

    @Test
    public void waitsForServer() throws Exception {
        this.serverMock.join();
        replayAll();
        this.runner.join();
        verifyAll();
    }
}
