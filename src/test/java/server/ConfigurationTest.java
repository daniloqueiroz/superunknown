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
        assertEquals(parseInt(Key.PORT.getDefaultValue()), this.config.getPort());
    }

    @Test
    public void checkPort() {
        System.setProperty(Key.PORT.getKey(), "80");
        assertEquals(80, this.config.getPort());
    }
}
