package fr.jbu.asyncperf.core.dump

import fr.jbu.asyncperf.core.injector.{InjectorResult, Response, Request}

trait Dump {

  def dumpTransaction(transaction: InjectorResult[Request, Option[Response]])

}