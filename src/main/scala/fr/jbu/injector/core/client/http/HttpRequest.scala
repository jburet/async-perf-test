package fr.jbu.injector.core.client.http

import fr.jbu.injector.core.client.Request

case class HttpRequest(userActorId:akka.actor.Uuid, requestUri:String) extends Request{


}