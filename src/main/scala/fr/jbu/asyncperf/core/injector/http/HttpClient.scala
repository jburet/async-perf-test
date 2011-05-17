package fr.jbu.asyncperf.core.injector.http

import fr.jbu.asyncperf.core.injector.InjectorRequest

trait HttpClient{

  def sendRequest(request:InjectorRequest[HttpRequest], startNanoTime:Long, callback: (InjectorRequest[HttpRequest],HttpResponse,Long) => Unit)

}