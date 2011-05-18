package fr.jbu.asyncperf.core.injector.http.impl.asynchttpclient

import java.util.concurrent.Future
import fr.jbu.asyncperf.core.injector.http.{HttpHeader, HttpResponse, HttpRequest, HttpClient}
import fr.jbu.asyncperf.core.injector.InjectorRequest
import com.ning.http.client.{FluentCaseInsensitiveStringsMap, Response, AsyncCompletionHandler, AsyncHttpClient}
import collection.mutable.HashMap
import scala.collection.JavaConversions

class HttpClientBasedOnAHC extends HttpClient {

  val asyncHttpClient: AsyncHttpClient = new AsyncHttpClient

  def sendRequest(request: InjectorRequest[HttpRequest], startNanoTime: Long, callback: (InjectorRequest[HttpRequest], Option[HttpResponse], Long) => Unit) = {

    val f: Future[Unit] = asyncHttpClient.prepareGet(request.internalRequest.requestUri).execute(new AsyncCompletionHandler[Unit] {
      def onCompleted(ahcResponse: Response) {
        val httpResponse: HttpResponse = new HttpResponse(ahcResponse.getStatusCode, ahcResponse.getUri.toString, ahcResponse.getResponseBody, new HttpHeader(convertHeader(ahcResponse.getHeaders)))

        callback(request, Some(httpResponse), startNanoTime)
      }
    })

  }

  private def convertHeader(ahcHeader: FluentCaseInsensitiveStringsMap): collection.mutable.Map[String, String] = {
    val resMap: collection.mutable.Map[String, String] = new HashMap[String, String]()
    for (headerKey <- JavaConversions.iterableAsScalaIterable(ahcHeader.keySet)) {
      resMap += headerKey -> JavaConversions.asScalaBuffer(ahcHeader.get(headerKey)).reduceLeft(_ + "; " + _)
    }
    resMap
  }

}