package fr.jbu.injector.core.client.http

import akka.testkit.TestKit
import impl.asynchttpclient.HttpClientBasedOnAHC
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{WordSpec, BeforeAndAfterAll}
import akka.actor.Actor._
import akka.util.duration._
import fr.jbu.injector.core.client.Transaction
import scala.Predef._

class TestHttpClientActorOnNetty extends WordSpec with BeforeAndAfterAll with ShouldMatchers with TestKit {
  val httpClient: HttpClient = new HttpClientBasedOnAHC
  val httpClientActorRef = actorOf(new HttpClientActor(httpClient)).start

  override protected def afterAll(): scala.Unit = {
    stopTestActor
    httpClientActorRef.stop
  }

  "An HttpClientActor on netty" should {
    "Send a transaction with HttpResponse" in {
      val request: HttpRequest = new HttpRequest(testActor.uuid, "http://localhost:8080/static/html/index.html")
      var responseBody: String = ""
      within(1200 millis) {
        httpClientActorRef ! request
        receiveWhile(1000 millis) {
          case transaction: Transaction[HttpRequest, HttpResponse] => {
            responseBody = transaction.response.body
          }
        }
      }
      responseBody.length should be > (0)
    }
  }
}