package fr.jbu.asyncperf.core.reporting

import akka.actor.Actor


class Dump {

}

class DumpActor extends Dump with Actor {
  protected def receive = {
    case _ => println("Reporting not implemented")
  }
}