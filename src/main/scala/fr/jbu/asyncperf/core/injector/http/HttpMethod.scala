package fr.jbu.asyncperf.core.injector.http

class HttpMethod {

}

object HttpMethodEnum {

  case object GET extends HttpMethod

  case object POST extends HttpMethod

  case object PUT extends HttpMethod

  case object HEAD extends HttpMethod

  case object DELETE extends HttpMethod

}