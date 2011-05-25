package fr.jbu.asyncperf.core.injector.http.impl.netty

import fr.jbu.asyncperf.core.injector.InjectorRequest
import org.jboss.netty.bootstrap.ClientBootstrap
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory
import java.util.concurrent.Executors
import java.net.InetSocketAddress
import org.jboss.netty.channel.{ChannelFutureListener, ChannelFuture}
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, DefaultHttpRequest}
import fr.jbu.asyncperf.core.injector.http.{HttpMethodEnum, HttpResponse, HttpRequest, HttpClient}
import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor

class HttpClientBasedOnNetty extends HttpClient {

  val bootstrap: ClientBootstrap = new ClientBootstrap(
    new NioClientSocketChannelFactory(new MemoryAwareThreadPoolExecutor(4, 10000000, 10000000), new MemoryAwareThreadPoolExecutor(4, 10000000, 10000000)));

  // Set up the event pipeline factory.
  bootstrap.setPipelineFactory(new HttpClientPipelineFactory(false, new HttpResponseHandler));

  def sendRequest(request: InjectorRequest[HttpRequest], startNanoTime: Long, callback: (InjectorRequest[HttpRequest], Option[HttpResponse], Long) => Unit) = {
    val channelFuture: ChannelFuture = bootstrap.connect(new InetSocketAddress(request.internalRequest.requestUri.getHost, request.internalRequest.requestUri.getPort))
    channelFuture.addListener(new ChannelFutureListener {

      def operationComplete(cf: ChannelFuture) {
        if (cf.isSuccess) {
          // Create netty request
          val nettyRequest = convertRequest(request.internalRequest)
          val channel = cf.getChannel
          // Put request and callback in channel locale context for contruct result on callback
          ChannelLocaleContext.getContext.set(channel, (request, startNanoTime, callback))
          channel.write(nettyRequest)
        } else {
          // Cannot connect to serveur
        }
      }
    })
  }

  def convertMethod(httpMethod: fr.jbu.asyncperf.core.injector.http.HttpMethod): org.jboss.netty.handler.codec.http.HttpMethod = {
    httpMethod match {
      case HttpMethodEnum.GET => {
        org.jboss.netty.handler.codec.http.HttpMethod.GET
      }
      case HttpMethodEnum.POST => {
        org.jboss.netty.handler.codec.http.HttpMethod.POST
      }
    }
  }

  def convertRequest(httpRequest: HttpRequest): org.jboss.netty.handler.codec.http.HttpRequest = {

    val nhr: org.jboss.netty.handler.codec.http.HttpRequest = new DefaultHttpRequest(
      HttpVersion.HTTP_1_1,
      convertMethod(httpRequest.httpMethod),
      httpRequest.requestUri.toASCIIString)
    nhr
  }
}