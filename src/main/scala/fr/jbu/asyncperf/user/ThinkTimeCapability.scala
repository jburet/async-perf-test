package fr.jbu.asyncperf.user

import action.EndOfThinktime
import fr.jbu.asyncperf.util.Slf4jLogger
import java.util.concurrent.TimeUnit
import akka.actor.{ActorRef, Scheduler}

trait ThinkTimeCapability extends Slf4jLogger {

  protected val userActorRef: Option[ActorRef]

  protected var processingStarted: Boolean

  protected def startOrResumeUser

  /**
   * Request a non blocking thinktime. When timeout endOfThinkTimeCallback must be called
   */
  def thinkTimeRequest(thinkTime: Int, timeUnit: TimeUnit) {
    // Stop processing
    userActorRef match {
      case Some(userActor) => {
        processingStarted = false
        // Do the thinktime
        Scheduler.scheduleOnce(userActor, EndOfThinktime, thinkTime, timeUnit)
      }
      case None => warn("No userActor ref defined")
    }

  }


  protected def endOfThinkTimeCallback() {
    processingStarted = true
    startOrResumeUser
  }


}