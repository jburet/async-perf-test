package fr.jbu.asyncperf.user.action

import org.scalatest.matchers.ShouldMatchers
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import java.util.concurrent.TimeUnit
import fr.jbu.asyncperf.user.User
import fr.jbu.asyncperf.core.injector.InjectorRequest
import fr.jbu.asyncperf.core.injector.http.HttpRequest
import akka.util.duration._
import collection.mutable.ListBuffer

class TestThinkTimeAction extends FunSuite with BeforeAndAfterAll with ShouldMatchers with TestKit {

  test("When an thinkTimeAction of 5s is executed user receive End of think time after 5s") {
    val thinkTimeAction: ThinkTimeAction = new ThinkTimeAction(5, TimeUnit.SECONDS)
    var receiveEndOfThink: Boolean = false
    within(5000 millis, 10000 millis) {
      thinkTimeAction.execute(new User(new ListBuffer[(User) => Action]) {
        protected val nbOfExecutionBeforeEnd = 1
        protected val userActorRef = Some(testActor)
        protected val reportingActorRef = None
        protected val dumpEnabled = false
        protected val dumpActorRef = None

        protected def uuid = new akka.actor.Uuid

        protected def httpClient = None
      })
      receiveWhile(7000 millis) {

        case EndOfThinktime => {
          receiveEndOfThink = true
        }
        case _ =>
          println("Unknown message")
      }
    }
    receiveEndOfThink should equal(true)
  }
}