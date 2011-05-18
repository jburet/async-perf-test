package fr.jbu.asyncperf.core.injector

import akka.actor.Actor

abstract class AuditedClientActor[REQ <: Request, RESP <: Option[Response]] extends Actor {

  protected def makeCall(request: InjectorRequest[REQ], startNanoTime:Long)

  protected def endCall(request: InjectorRequest[REQ], response: RESP, startNanoTime:Long) = {
    // send response to actor creating request
    val userActor = Actor.registry.actorFor(request.userActorId)
    userActor.get ! new InjectorResult(request.internalRequest, response, startNanoTime, System.nanoTime)
  }


  protected def receive = {
    // TODO Verify actor can manage message
    case request: InjectorRequest[REQ] => {
      // and make call
      makeCall(request, System.nanoTime)
    }
    //FIXME change RuntimeException with specifique Exception
    case _ => throw new RuntimeException("Message type not managed")
  }

}
