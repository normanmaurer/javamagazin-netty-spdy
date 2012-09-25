package me.normanmaurer.javamagazin.netty.example.spdy;

import javax.net.ssl.SSLEngine;

import org.eclipse.jetty.npn.NextProtoNego;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.handler.codec.spdy.SpdyOrHttpChooser;

/**
 * {@link SpdyOrHttpChooser} der {@link NextProtoNego} verwendet um festzustellen
 * ob SPDY oder HTTP verwendet werden soll.
 * 
 * @author Norman Maurer <norman@apache.org>
 *
 */
public class SpdyOrHttpChooserImpl extends SpdyOrHttpChooser {

    public SpdyOrHttpChooserImpl(int maxSpdyContentLength, int maxHttpContentLength) {
        super(maxSpdyContentLength, maxHttpContentLength);
    }

    @Override
    protected SelectedProtocol getProtocol(SSLEngine engine) {
        // Anhand des ausgewaehlten Protokolls wird das richtige ausgewaehlt.
        ProviderImpl provider = (ProviderImpl) NextProtoNego.get(engine);
        String protocol = provider.getSelectedProtocol();
        if (protocol == null) {
            return SelectedProtocol.None;
        }
        switch (protocol) {
        case "spdy/2":
            return SelectedProtocol.SpdyVersion2;
        case "spdy/3":
            return SelectedProtocol.SpdyVersion3;
        case "http/1.1":
            return SelectedProtocol.HttpVersion1_1;
        case "http/1.0":
            return SelectedProtocol.HttpVersion1_0;
        default:
            return SelectedProtocol.None;
        }
    }

    @Override
    protected ChannelUpstreamHandler createHttpRequestHandlerForHttp() {
        return new HttpRequestHandler();
    }

    @Override
    protected ChannelUpstreamHandler createHttpRequestHandlerForSpdy() {
        return new SpdyHttpRequestHandler();
    }
}
