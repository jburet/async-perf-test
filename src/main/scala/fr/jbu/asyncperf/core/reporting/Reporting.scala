package fr.jbu.asyncperf.core.reporting

import fr.jbu.asyncperf.core.injector.{Response, Request, InjectorResult}

trait Reporting {

  def logTransaction(transaction: InjectorResult[Request, Option[Response]])

}