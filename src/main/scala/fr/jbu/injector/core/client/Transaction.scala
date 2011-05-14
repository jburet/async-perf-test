package fr.jbu.injector.core.client

import java.util.UUID

case class Transaction[REQ <% Request, RESP <% Response](request: REQ, response: RESP, startNanoTime: Long, endNanoTime: Long) {

  val identifier = UUID.randomUUID.toString
  

}