package server;

import static java.lang.Integer.parseInt;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import server.Configuration.Key;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class ConfigurationTest {

    private Configuration config = new Configuration();

    @Test
    public void checkDefaultPort() {
        assertEquals(parseInt(Key.SERVER_PORT.getDefaultValue()), this.config.getServerPort());
    }

    @Test
    public void checkPort() {
        System.setProperty(Key.SERVER_PORT.getKey(), "80");
        assertEquals(80, this.config.getServerPort());
    }
}
