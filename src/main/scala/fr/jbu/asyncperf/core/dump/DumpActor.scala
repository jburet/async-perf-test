package fr.jbu.asyncperf.core.dump

import akka.actor.Actor

class DumpActor(dumpImplementation:List[Dump]) extends Actor {
  protected def receive = {
    case _ => println("Reporting not implemented")
  }
}