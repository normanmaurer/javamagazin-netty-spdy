package me.normanmaurer.javamagazin.netty.example.spdy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jetty.npn.NextProtoNego.ServerProvider;

/**
 * {@link ServerProvider} implementation die SPDY2, SPDY3
 * HTTP1.1 und HTTP1.0 unterstuetzt.
 * 
 * @author Norman Maurer <norman@apache.org>
 *
 */
class ProviderImpl implements ServerProvider {

    private String protocol;

    @Override
    public void protocolSelected(String protocol) {
        this. protocol = protocol;
    }

    @Override
    public List<String> protocols() {
        return Collections.unmodifiableList(Arrays.asList("spdy/2", "spdy/3", "http/1.1"));
    }

    @Override
    public void unsupported() {
        // als standard protocol HTTP 1.1 verwenden
        protocol = "http/1.1";
    }

    /**
     * Gebe ausgewaehltes Protokoll zurueck
     * 
     * @return protokoll
     */
    public String getSelectedProtocol() {
        return protocol;
    }

}
