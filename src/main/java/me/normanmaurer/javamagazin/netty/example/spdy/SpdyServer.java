package me.normanmaurer.javamagazin.netty.example.spdy;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class SpdyServer {

    private final int port;

    public SpdyServer(int port) {
        this.port = port;
    }

    public void startUp() {
        ChannelFactory factory = new NioServerSocketChannelFactory();
        ServerBootstrap sb = new ServerBootstrap(factory);
        sb.setPipelineFactory(new SpdyChannelPipelineFactory());
        sb.bind(new InetSocketAddress(port));
    }

    public static void main(String args[]) {
        int port;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8888;
        }
        new SpdyServer(port).startUp();
    }
}
