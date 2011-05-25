package org.jboss.netty.handler.connection;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.*;

/**
 * {@link ChannelUpstreamHandler} which limit connections per IP
 * <p/>
 * This handler must be used as singleton when adding it to the {@link ChannelPipeline} to work correctly
 */
@ChannelHandler.Sharable
public class ConnectionPerIpLimitUpstreamHandler extends SimpleChannelUpstreamHandler {

    private final ConcurrentMap<String, AtomicInteger> connections = new ConcurrentHashMap<String, AtomicInteger>();
    private final int maxConnectionsPerIp;

    public ConnectionPerIpLimitUpstreamHandler(int maxConnectionsPerIp) {
        this.maxConnectionsPerIp = maxConnectionsPerIp;
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        if (maxConnectionsPerIp > 0) {
            InetSocketAddress remoteAddress = (InetSocketAddress) ctx.getChannel().getRemoteAddress();
            String remoteIp = remoteAddress.getAddress().getHostAddress();
            AtomicInteger atomicCount = connections.get(remoteIp);
            if (atomicCount == null) {
                atomicCount = new AtomicInteger(1);
                AtomicInteger oldAtomicCount = connections.putIfAbsent(remoteIp, atomicCount);
                // if another thread put a new counter for this ip, we must use the other one.
                if (oldAtomicCount != null) {
                    atomicCount = oldAtomicCount;
                }
            } else {
                Integer count = atomicCount.incrementAndGet();
                if (count > maxConnectionsPerIp) {
                    ctx.getChannel().close();
                    atomicCount.decrementAndGet();
                }
            }

        }

        super.channelOpen(ctx, e);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (maxConnectionsPerIp > 0) {
            InetSocketAddress remoteAddress = (InetSocketAddress) ctx.getChannel().getRemoteAddress();
            String remoteIp = remoteAddress.getAddress().getHostAddress();
            AtomicInteger atomicCount = connections.get(remoteIp);
            if (atomicCount != null) {
            	atomicCount.decrementAndGet();
            }
        }
        super.channelClosed(ctx, e);
    }
}