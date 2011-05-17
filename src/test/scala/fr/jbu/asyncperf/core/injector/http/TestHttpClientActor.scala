package fr.jbu.asyncperf.core.injector.http

import akka.testkit.TestKit
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{WordSpec, BeforeAndAfterAll}
import akka.actor.Actor._
import akka.util.duration._
import fr.jbu.asyncperf.core.injector.{InjectorRequest, InjectorResult}

class TestHttpClientActor extends WordSpec with BeforeAndAfterAll with ShouldMatchers with TestKit {
  val httpClient: HttpClient = MockHttpClient
  val httpClientActorRef = actorOf(new HttpClientActor(httpClient)).start

  override protected def afterAll(): scala.Unit = {
    stopTestActor
    httpClientActorRef.stop
  }

  "An HttpClientActor" should {
    "Send a transaction with HttpResponse" in {
      val request: InjectorRequest[HttpRequest] =new InjectorRequest[HttpRequest](testActor.uuid, new HttpRequest("http://localhost/use_mock_client"))

      within(100 millis) {
        httpClientActorRef ! request
        receiveWhile(500 millis) {
          case transaction: InjectorResult[HttpRequest, HttpResponse] => {
            transaction.response.body should equal ("test body")
          }
        }
      }
    }
  }

  object MockHttpClient extends HttpClient{

    def sendRequest(request: InjectorRequest[HttpRequest], startNanoTime:Long, callback: (InjectorRequest[HttpRequest], HttpResponse, Long) => Unit) = {
      callback(request, new HttpResponse("test body", new HttpHeader), System.nanoTime)
    }
  }
}