package fr.jbu.asyncperf.core.injector.http

import fr.jbu.asyncperf.core.injector.Response


case class HttpResponse(body:String, header:HttpHeader) extends Response