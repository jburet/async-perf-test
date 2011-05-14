package fr.jbu.injector.core.client.http

import fr.jbu.injector.core.client.Response


case class HttpResponse(body:String, header:HttpHeader) extends Response