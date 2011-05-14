package fr.jbu.injector.core.client.test

import org.scalatest.matchers.ShouldMatchers
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, WordSpec}
import akka.actor.Actor._
import akka.util.duration._
import fr.jbu.injector.core.client.Transaction

class TestTheTestClient extends WordSpec with BeforeAndAfterAll with ShouldMatchers with TestKit {

  val testClientRef = actorOf(new TestClient).start

  override protected def afterAll(): scala.Unit = {
    stopTestActor
    testClientRef.stop
  }

  "An TestClientActor" should {
    "Send a transaction" in {
      within(100 millis) {()
        testClientRef ! new TestRequest(testActor.uuid)
        receiveWhile(500 millis) {
          case transaction: Transaction[TestRequest, TestResponse] => {
            transaction.request.value should equal ("REQUEST")
            transaction.response.value should equal ("RESPONSE")
          }
        }
      }
    }
  }
}