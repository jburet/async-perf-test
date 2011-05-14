package fr.jbu.injector.core.client

import akka.actor.Actor
import akka.actor.ActorRegistry

abstract class AuditedRequestActor[REQ <% Request, RESP <% Response] extends Actor {

  protected def makeCall(request: REQ, startNanoTime:Long)

  protected def endCall(request: REQ, response: RESP, startNanoTime:Long) = {
    // send response to actor creating request
    val userActor = Actor.registry.actorFor(request.userActorId)
    userActor.get ! new Transaction(request, response, startNanoTime, System.nanoTime)
  }


  protected def receive = {
    // TODO Verify actor can manage message
    case request: REQ => {
      // and make call
      makeCall(request, System.nanoTime)
    }
    //FIXME change RuntimeException with specifique Exception
    case _ => throw new RuntimeException("Message type not managed")
  }

}
