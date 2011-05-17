package fr.jbu.asyncperf.core.reporting

import akka.actor.Actor


class Reporting {

}

class ReportingActor extends Reporting with Actor {
  protected def receive = {
    case _ => println("Reporting not implemented")
  }
}