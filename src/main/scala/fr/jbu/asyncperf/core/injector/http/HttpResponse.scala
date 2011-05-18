package fr.jbu.asyncperf.core.injector.http

import fr.jbu.asyncperf.core.injector.Response


case class HttpResponse(code: Int, body:String, uri:String, header:HttpHeader) extends Response