package fr.jbu.injector.core.client.test

import fr.jbu.injector.core.client.{Transaction, AuditedRequestActor}

class TestClient extends AuditedRequestActor[TestRequest, TestResponse] {

  def makeCall(request: TestRequest, startNanoTime:Long) = {
    // wait some time and send callback
    // Create response
    val response:TestResponse = new TestResponse()
    endCall(request, response, startNanoTime)
  }
}