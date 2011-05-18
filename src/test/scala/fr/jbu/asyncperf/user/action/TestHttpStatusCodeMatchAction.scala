package fr.jbu.asyncperf.user.action

import org.scalatest.FunSuite
import fr.jbu.asyncperf.core.injector.InjectorResult
import fr.jbu.asyncperf.user.User
import collection.mutable.ListBuffer
import org.scalatest.matchers.ShouldMatchers
import fr.jbu.asyncperf.core.injector.http.{HttpHeader, HttpRequest, HttpResponse}


class TestHttpStatusCodeMatchAction extends FunSuite with ShouldMatchers {


  test("Given a HttpStatusCodeMatchAction when match with no response should not match") {
    val testedAction: HttpStatusCodeMatchAction = new HttpStatusCodeMatchAction(new InjectorResult[HttpRequest, Option[HttpResponse]](new HttpRequest("uri-test"), None, 0, 0), 200)
    val res = testedAction.execute(MockUser)
    res should equal(ActionResult.NotMatch)
  }

  test("Given a HttpStatusCodeMatchAction when match with different status should return not match") {
    val resp: HttpResponse = new HttpResponse(200, "body", "uri", new HttpHeader(scala.collection.mutable.Map.empty[String, String]))
    val testedAction: HttpStatusCodeMatchAction = new HttpStatusCodeMatchAction(new InjectorResult[HttpRequest, Option[HttpResponse]](new HttpRequest("uri-test"), Some(resp), 0, 0), 200)
    val res = testedAction.execute(MockUser)
    res should equal(ActionResult.Match)
  }

  test("Given a HttpStatusCodeMatchAction when match with same status object should return match") {
    val resp: HttpResponse = new HttpResponse(404, "", "uri", new HttpHeader(scala.collection.mutable.Map.empty[String, String]))
    val testedAction: HttpStatusCodeMatchAction = new HttpStatusCodeMatchAction(new InjectorResult[HttpRequest, Option[HttpResponse]](new HttpRequest("uri-test"), Some(resp), 0, 0), 200)
    val res = testedAction.execute(MockUser)
    res should equal(ActionResult.NotMatch)
  }
}

object MockUser extends User(new ListBuffer[(User) => Action]) {
  protected val nbOfExecutionBeforeEnd = 1
  protected val reportingActorRef = None
  protected val dumpEnabled = true
  protected val dumpActorRef = None

  protected val uuid = new akka.actor.Uuid()

  protected val httpClient = None
  protected val userActorRef = None
}