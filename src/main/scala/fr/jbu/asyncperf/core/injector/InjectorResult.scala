package fr.jbu.asyncperf.core.injector

case class InjectorResult[+REQ <: Request, +RESP <: Option[Response]](request: REQ, response: RESP, startNanoTime: Long, endNanoTime: Long) {


}