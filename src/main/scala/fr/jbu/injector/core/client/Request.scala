package fr.jbu.injector.core.client

import akka.actor.Actor

abstract class Request{
  def userActorId:akka.actor.Uuid
}