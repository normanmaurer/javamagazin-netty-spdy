package me.normanmaurer.javamagazin.netty.example.spdy;

import javax.net.ssl.SSLEngine;

import org.eclipse.jetty.npn.NextProtoNego;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.ssl.SslHandler;

/**
 * {@link ChannelPipelineFactory} die die noetigen {@link ChannelHandler} einfuegt.
 * 
 * @author Norman Maurer <norman@apache.org>
 *
 */
public class SpdyChannelPipelineFactory implements ChannelPipelineFactory {

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = Channels.pipeline();
        SSLEngine engine = BogusSslContextFactory.getServerContext().createSSLEngine();
        engine.setUseClientMode(false);

        NextProtoNego.put(engine, new ProviderImpl());
        NextProtoNego.debug = true;

        pipeline.addLast("sslHandler", new SslHandler(engine));
        pipeline.addLast("chooser", new SpdyOrHttpChooserImpl(1024 * 1024, 1024 * 1024));
        return pipeline;
    }

}
