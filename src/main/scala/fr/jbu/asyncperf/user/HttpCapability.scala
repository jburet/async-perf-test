package fr.jbu.asyncperf.user

import action.ActionResult.ActionResult
import action.{ActionResult, HttpCallAction}
import fr.jbu.asyncperf.core.injector.http.{HttpRequest, HttpClientActor}
import akka.actor.ActorRef
import fr.jbu.asyncperf.core.injector.InjectorRequest
import fr.jbu.asyncperf.util.Slf4jLogger

trait HttpCapability extends Slf4jLogger {

  protected val httpClient: Option[ActorRef]

  protected def uuid: akka.actor.Uuid

  def sendHttpRequest(action: HttpCallAction): ActionResult = {
    // Transform HttpCallAction to HttpRequest and send it
    httpClient match {
      case None => {
        warn("No reference to http client actor")
        ActionResult.Error
      }
      case Some(hc) => {
        hc.sendOneWay(new InjectorRequest[HttpRequest](uuid, action.httpRequest))
        ActionResult.Send
      }
    }

  }

}