package fr.jbu.asyncperf.core.injector.http

import fr.jbu.asyncperf.core.injector.Request
import fr.jbu.asyncperf.core.injector.http.HttpMethod.HttpMethod

case class HttpRequest(requestUri:String, httpMethod:HttpMethod, queryParam:Map[String, String]) extends Request{

  def this(requestUri:String, httpMethod:HttpMethod) = {this(requestUri, httpMethod, Map())}
  
  def this(requestUri:String) = {this(requestUri, HttpMethod.GET, Map())}

}