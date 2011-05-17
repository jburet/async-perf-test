package fr.jbu.asyncperf.user

import action.ActionResult.ActionResult
import action.{ActionResult, HttpCallAction}
import fr.jbu.asyncperf.core.injector.http.{HttpRequest, HttpClientActor}
import akka.actor.ActorRef
import fr.jbu.asyncperf.core.injector.InjectorRequest

trait HttpCapability {

  protected def httpClient: ActorRef

  protected def uuid: akka.actor.Uuid

  def sendHttpRequest(action: HttpCallAction): ActionResult = {
    // Transform HttpCallAction to HttpRequest and send it
    httpClient.sendOneWay(new InjectorRequest[HttpRequest](uuid, action.httpRequest))
    ActionResult.Send
  }

}