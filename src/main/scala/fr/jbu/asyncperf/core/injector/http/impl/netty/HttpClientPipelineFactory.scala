package fr.jbu.asyncperf.core.injector.http.impl.netty

import org.jboss.netty.channel.{ChannelPipeline, ChannelPipelineFactory}
import org.jboss.netty.handler.codec.http.{HttpChunkAggregator, HttpContentDecompressor, HttpClientCodec}
import org.jboss.netty.channel.Channels._
import org.jboss.netty.handler.connection.ConnectionPerIpLimitUpstreamHandler
;

class HttpClientPipelineFactory(ssl: Boolean, handler: HttpResponseHandler) extends ChannelPipelineFactory {

  def getPipeline(): ChannelPipeline = {
    val intPipeline: ChannelPipeline = pipeline;

    // Enable HTTPS if necessary.
    // TODO

    intPipeline.addLast("codec", new HttpClientCodec());
    intPipeline.addLast("inflater", new HttpContentDecompressor());
    intPipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
    intPipeline.addLast("handler", handler);
    return intPipeline;
  }


}