package fr.jbu.asyncperf.user.api

import akka.actor.Actor


class UserBuilder {

  def withScenario(scenario: Scenario): UserBuilder = {
    this
  }

  def withBaseIdentifiant(baseIdentifiant: String): UserBuilder = {
    this
  }

  def buildActor(nbActor: Int): Array[Actor] = null
  
}