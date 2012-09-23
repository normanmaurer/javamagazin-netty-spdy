package me.normanmaurer.javamagazin.netty.example.spdy;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

/**
 * {@link SimpleChannelUpstreamHandler} der einen {@link HttpRequest} entgegen nimmt und ein
 * {@link HttpResponse} zurueck and den Client schickt.
 * 
 * @author Norman Maurer <norman@apache.org>
 *
 */
public class HttpRequestHandler extends SimpleChannelUpstreamHandler {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request = (HttpRequest) e.getMessage();

        // Senden eines CONTINUE EXPECTED wenn dieser erwartet wird.
        if (HttpHeaders.is100ContinueExpected(request)) {
            send100Continue(e);
        }

        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        ChannelBuffer buf = ChannelBuffers.copiedBuffer(getContent(), CharsetUtil.ISO_8859_1);
        response.setContent(buf);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");

        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        if (keepAlive) {
            // Hinzufuegen der HTTP header die fuer keep-alive erforderlich sind
            response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, response.getContent().readableBytes());
            response.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ChannelFuture future = ctx.getChannel().write(response);
        if (keepAlive) {
            // Falls kein keep-alive aktiv ist muss der Channel geschlossen werden sobald
            // der HTTPResponse zum Client geschickt wurde.
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    // Der Inhalt der zurueck-zusendenen Seite
    protected String getContent() {
        return "Serve via HTTP\r\n";
    }

    private static void send100Continue(MessageEvent e) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        e.getChannel().write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
