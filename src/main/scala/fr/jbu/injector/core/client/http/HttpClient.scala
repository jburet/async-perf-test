package fr.jbu.injector.core.client.http

trait HttpClient{

  def sendRequest(request:HttpRequest, startNanoTime:Long, callback: (HttpRequest,HttpResponse,Long) => Unit)

}