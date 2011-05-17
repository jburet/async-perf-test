package fr.jbu.asyncperf.core.injector

case class InjectorResult[REQ <: Request, RESP <: Response](request: REQ, response: RESP, startNanoTime: Long, endNanoTime: Long) {


}