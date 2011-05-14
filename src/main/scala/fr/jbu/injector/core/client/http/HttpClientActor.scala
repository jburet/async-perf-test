package fr.jbu.injector.core.client.http

import fr.jbu.injector.core.client.{AuditedRequestActor}

class HttpClientActor(httpClient:HttpClient) extends AuditedRequestActor[HttpRequest, HttpResponse] {

  protected def makeCall(request: HttpRequest, startNanoTime: Long) = {
    httpClient.sendRequest(request, startNanoTime, receiveResponse)
  }

  protected def receiveResponse(request:HttpRequest,response:HttpResponse,startNanoTime:Long){
    endCall(request, response, startNanoTime)
  }
}