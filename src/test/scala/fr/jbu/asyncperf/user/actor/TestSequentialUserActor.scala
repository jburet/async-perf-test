package fr.jbu.asyncperf.user.actor

import akka.actor.Actor
import akka.testkit.TestKit
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import fr.jbu.asyncperf.core.injector.http._
import akka.util.duration._
import fr.jbu.asyncperf.user.action.{Action, HttpCallAction}
import collection.mutable.ListBuffer
import fr.jbu.asyncperf.registry.ActorIdentifier
import fr.jbu.asyncperf.user.{UserCommand, User, SequentialUserActor}
import fr.jbu.asyncperf.core.injector.{InjectorRequest, InjectorResult}

class TestSequentialUserActor extends FunSuite with BeforeAndAfterAll with ShouldMatchers with TestKit {

  val httpClient: HttpClient = MockHttpClient
  val httpClientActor = Actor.actorOf(new HttpClientActor(httpClient))
  httpClientActor.id = ActorIdentifier.HTTP_CLIENT
  httpClientActor.start

  val dumpActorRef = Actor.actorOf(MockDumpActor)
  dumpActorRef.id = ActorIdentifier.DUMP
  dumpActorRef.start

  val reportingActorRef = Actor.actorOf(MockReportingActor)
  reportingActorRef.id = ActorIdentifier.REPORTING
  reportingActorRef.start

  override protected def afterAll(): scala.Unit = {
    stopTestActor
    dumpActorRef.stop
    reportingActorRef.stop
  }

  test("Given user actor with one HttpCallAction When started Then HttpClientActor should receive one message with good uri") {
    var exceptedUri: String = ""
    val sequence: ListBuffer[(User) => Action] = new ListBuffer[(User) => Action]()
    sequence += ((user: User) => new HttpCallAction(new HttpRequest("http://localhost:8080/")))
    val userActorRef = Actor.actorOf(new SequentialUserActor(sequence, dumpActorRef, reportingActorRef, testActor, true)).start
    userActorRef ! UserCommand.START
    within(1000 millis) {
      receiveWhile(1000 millis) {
        case request: InjectorRequest[HttpRequest] => {
          exceptedUri = request.internalRequest.requestUri
        }
      }
    }
    exceptedUri should equal("http://localhost:8080/")
    userActorRef.stop
  }


  test("Given user actor with dump actived and one HttpCallAction When started Then user should dump transaction") {
    var exceptedResponseBody: String = ""
    val sequence: ListBuffer[(User) => Action] = new ListBuffer[(User) => Action]()
    sequence += ((user: User) => new HttpCallAction(new HttpRequest("http://localhost:8080/")))
    val userActorRef = Actor.actorOf(new SequentialUserActor(sequence, testActor, reportingActorRef, httpClientActor, true)).start
    userActorRef ! UserCommand.START
    within(1000 millis) {
      receiveWhile(1000 millis) {
        case transaction: InjectorResult[HttpRequest, HttpResponse] => {
          exceptedResponseBody = transaction.response.body
        }
      }
    }
    exceptedResponseBody should equal("test body")
    userActorRef.stop
  }

  test("Given user actor with dump deactived and one HttpCallAction When started Then user should not dump transaction") {
    var exceptedResponseBody: String = ""
    val sequence: ListBuffer[(User) => Action] = new ListBuffer[(User) => Action]()
    sequence += ((user: User) => new HttpCallAction(new HttpRequest("http://localhost:8080/")))
    val userActorRef = Actor.actorOf(new SequentialUserActor(sequence, testActor, reportingActorRef, httpClientActor, false)).start
    userActorRef ! UserCommand.START
    within(1000 millis) {
      receiveWhile(1000 millis) {
        case transaction: InjectorResult[HttpRequest, HttpResponse] => {
          exceptedResponseBody = transaction.response.body
        }
      }
    }
    exceptedResponseBody should equal("")
    userActorRef.stop
  }


  test("Given user actor with two HttpCallAction When started Then HttpClientActor should receive two message with good uri") {
    var exceptedUri1: String = ""
    var nbRequest:Int = 0
    val sequence: ListBuffer[(User) => Action] = new ListBuffer[(User) => Action]()
    sequence += ((user: User) => new HttpCallAction(new HttpRequest("http://localhost:8080/")))
    sequence += ((user: User) => new HttpCallAction(new HttpRequest("http://localhost:8080/")))
    val userActorRef = Actor.actorOf(new SequentialUserActor(sequence, dumpActorRef, reportingActorRef, testActor, true)).start
    userActorRef ! UserCommand.START
    within(1000 millis) {
      receiveWhile(1000 millis) {
        case request: InjectorRequest[HttpRequest] => {
          exceptedUri1 = request.internalRequest.requestUri
          nbRequest += 1
        }
      }
    }
    exceptedUri1 should equal("http://localhost:8080/")
    nbRequest should equal(2)
    userActorRef.stop
  }

  object MockHttpClient extends HttpClient {

    def sendRequest(request: InjectorRequest[HttpRequest], startNanoTime: Long, callback: (InjectorRequest[HttpRequest], HttpResponse, Long) => Unit) = {
      callback(request, new HttpResponse("test body", new HttpHeader), System.nanoTime)
    }
  }

  object MockDumpActor extends Actor {

    var lastMessage: InjectorResult[HttpRequest, HttpResponse] = null

    protected def receive = {
      case transaction: InjectorResult[HttpRequest, HttpResponse] => lastMessage = transaction
    }
  }

  object MockReportingActor extends Actor {
    var lastMessage: InjectorResult[HttpRequest, HttpResponse] = null

    protected def receive = {
      case transaction: InjectorResult[HttpRequest, HttpResponse] => lastMessage = transaction
    }
  }


}