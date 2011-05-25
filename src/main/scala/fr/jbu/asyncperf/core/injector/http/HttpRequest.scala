package fr.jbu.asyncperf.core.injector.http

import fr.jbu.asyncperf.core.injector.Request
import java.net.URI

case class HttpRequest(requestUri:URI, httpMethod:HttpMethod) extends Request{

  def this(requestUri:URI) = {this(requestUri, HttpMethodEnum.GET)}

  override def toXML:scala.xml.Elem =
        <http-request>
          <request-uri>{requestUri.toString}</request-uri>
          <http-method>{httpMethod}</http-method>
        </http-request>




}