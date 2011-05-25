package fr.jbu.asyncperf.core.injector.http.impl.netty

import org.jboss.netty.channel.{MessageEvent, ChannelHandlerContext, SimpleChannelUpstreamHandler}
import org.jboss.netty.handler.codec.http.HttpResponse

class HttpResponseHandler extends SimpleChannelUpstreamHandler {

  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) = {
    e.getMessage match {
      case response: HttpResponse => {
        val result = new fr.jbu.asyncperf.core.injector.http.HttpResponse(response.getStatus.getCode, response.getContent.toByteBuffer, "", null)
        val tuple = ChannelLocaleContext.getContext.get(ctx.getChannel)
        ctx.getChannel.close
        tuple._3.apply(tuple._1, Some(result), tuple._2)
      }
      case _ => {
        None
      }
    }
  }
}