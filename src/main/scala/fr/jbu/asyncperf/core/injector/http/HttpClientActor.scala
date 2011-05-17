package fr.jbu.asyncperf.core.injector.http

import fr.jbu.asyncperf.core.injector.{InjectorRequest, AuditedClientActor}

class HttpClientActor(httpClient:HttpClient) extends AuditedClientActor[HttpRequest, HttpResponse] {

  protected def makeCall(request: InjectorRequest[HttpRequest], startNanoTime: Long) = {
    httpClient.sendRequest(request, startNanoTime, receiveResponse)
  }

  protected def receiveResponse(request:InjectorRequest[HttpRequest],response:HttpResponse,startNanoTime:Long){
    endCall(request, response, startNanoTime)
  }
}