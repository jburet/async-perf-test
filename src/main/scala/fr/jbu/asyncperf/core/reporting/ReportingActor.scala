package fr.jbu.asyncperf.core.reporting

import akka.actor.Actor

class ReportingActor(reportingImpls: List[Reporting]) extends Actor {
  protected def receive = {
    case _ => println("Reporting not implemented")
  }
}