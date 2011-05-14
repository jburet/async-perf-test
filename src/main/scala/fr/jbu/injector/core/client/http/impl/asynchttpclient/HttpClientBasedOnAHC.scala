package fr.jbu.injector.core.client.http.impl.asynchttpclient

import java.util.concurrent.Future
import com.ning.http.client.{Response, AsyncCompletionHandler, AsyncHttpClient}
import fr.jbu.injector.core.client.http.{HttpHeader, HttpResponse, HttpRequest, HttpClient}

class HttpClientBasedOnAHC extends HttpClient {

  val asyncHttpClient: AsyncHttpClient = new AsyncHttpClient

  def sendRequest(request: HttpRequest, startNanoTime:Long, callback: (HttpRequest, HttpResponse, Long) => Unit) = {

    val f: Future[Unit] = asyncHttpClient.prepareGet(request.requestUri).execute(new AsyncCompletionHandler[Unit] {
      def onCompleted(ahcResponse: Response) {
        val httpResponse: HttpResponse = new HttpResponse(ahcResponse.getResponseBody, new HttpHeader)
        callback(request, httpResponse, startNanoTime)
      }
    })

  }
}