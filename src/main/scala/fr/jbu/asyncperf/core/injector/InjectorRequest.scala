package fr.jbu.asyncperf.core.injector

case class InjectorRequest[REQ <: Request](userActorId: akka.actor.Uuid, internalRequest: REQ) {
}