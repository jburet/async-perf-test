package fr.jbu.asyncperf.core.reporting

import akka.actor.Actor
import fr.jbu.asyncperf.util.Slf4jLogger
import fr.jbu.asyncperf.core.injector.{Response, Request, InjectorResult}

class ReportingActor(reportingImpls: List[Reporting]) extends Actor with Slf4jLogger {
  protected def receive = {
    case message: InjectorResult[Request, Option[Response]] => {
      for (r <- reportingImpls) {
        r.logTransaction(message)
      }
    }
    case _ => info("Reporting not implemented for this message type")
  }
}