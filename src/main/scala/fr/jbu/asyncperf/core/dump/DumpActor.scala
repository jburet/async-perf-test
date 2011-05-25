package fr.jbu.asyncperf.core.dump

import akka.actor.Actor
import fr.jbu.asyncperf.util.Slf4jLogger
import fr.jbu.asyncperf.core.injector.{Response, Request, InjectorResult}

class DumpActor(dumpImplementation: List[Dump]) extends Actor with Slf4jLogger {
  protected def receive = {
    case message: InjectorResult[Request, Option[Response]] => {
      for (r <- dumpImplementation) {
        r.dumpTransaction(message)
      }
    }
    case _ => info("Dump not implemented for this message type")
  }
}