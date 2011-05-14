package fr.jbu.injector.core.client.test

import fr.jbu.injector.core.client.Request

case class TestRequest(userActorId:akka.actor.Uuid) extends Request{
  val value = "REQUEST"
}