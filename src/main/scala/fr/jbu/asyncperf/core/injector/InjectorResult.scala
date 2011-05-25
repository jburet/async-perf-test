package fr.jbu.asyncperf.core.injector

case class InjectorResult[+REQ <: Request, +RESP <: Option[Response]](request: REQ, response: RESP, startNanoTime: Long, endNanoTime: Long) {

  def toXML =
  <InjectorResult>
    <request>
      {request.toXML}
    </request>
    <response>
      {response.get.toXML}
    </response>
    <start>{startNanoTime}</start>
    <stop>{endNanoTime}</stop>
  </InjectorResult>

}