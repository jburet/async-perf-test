package fr.jbu.asyncperf.core.injector.http.impl.asynchttpclient

import java.util.concurrent.Future
import com.ning.http.client.{Response, AsyncCompletionHandler, AsyncHttpClient}
import fr.jbu.asyncperf.core.injector.http.{HttpHeader, HttpResponse, HttpRequest, HttpClient}
import fr.jbu.asyncperf.core.injector.InjectorRequest

class HttpClientBasedOnAHC extends HttpClient {

  val asyncHttpClient: AsyncHttpClient = new AsyncHttpClient

  def sendRequest(request: InjectorRequest[HttpRequest], startNanoTime:Long, callback: (InjectorRequest[HttpRequest], HttpResponse, Long) => Unit) = {

    val f: Future[Unit] = asyncHttpClient.prepareGet(request.internalRequest.requestUri).execute(new AsyncCompletionHandler[Unit] {
      def onCompleted(ahcResponse: Response) {
        val httpResponse: HttpResponse = new HttpResponse(ahcResponse.getResponseBody, new HttpHeader)
        callback(request, httpResponse, startNanoTime)
      }
    })

  }
}