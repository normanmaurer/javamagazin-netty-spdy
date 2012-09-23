package me.normanmaurer.javamagazin.netty.example.spdy;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * {@link HttpRequestHandler} der eine {@link HttpResponse} fuer {@link HttpRequest} and den Client
 * zurueck schreibt wenn SPDY verwendet wurde.
 * 
 * @author Norman Maurer <norman@apache.org>
 *
 */
public class SpdyHttpRequestHandler extends HttpRequestHandler {

    @Override
    protected String getContent() {
        return "Serve via SPDY\r\n";
    }

}
