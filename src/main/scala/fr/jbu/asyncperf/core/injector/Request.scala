package fr.jbu.asyncperf.core.injector

import java.util.Date

abstract class Request() {
  val timestamp: Date = new Date
}