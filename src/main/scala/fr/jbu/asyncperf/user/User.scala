package fr.jbu.asyncperf.user

import action.ActionResult.ActionResult
import action.{EndOfThinktime, Action}
import collection.mutable.ListBuffer
import fr.jbu.asyncperf.core.injector.{InjectorResult, Request, Response}
import fr.jbu.asyncperf.util.Slf4jLogger
import akka.actor.{ActorRef, Actor}

abstract class User(functionSequence: ListBuffer[(User) => Action]) extends HttpCapability with ThinkTimeCapability with Slf4jLogger {

  /**
   * Index of next function to execute
   */
  private var nextFunctionIndex: Int = 0

  protected var processingStarted: Boolean = true

  protected val dumpActorRef: Option[ActorRef]

  protected val dumpEnabled: Boolean

  protected val reportingActorRef: Option[ActorRef]

  protected val userActorRef: Option[ActorRef]

  /**
   * Nb of time user execute sequence function
   * <0 infinite
   */
  protected val nbOfExecutionBeforeEnd: Int

  /**
   * Nb of time user has executed sequence function
   */
  protected var nbOfTimeSequenceExecuted: Int = 0

  /**
   * Last transaction executed
   */
  var lastTransaction: InjectorResult[Request, Option[Response]] = null

  /**
   * Last action result
   */
  var lastActionResult: ActionResult = null

  /**
   * Return function to perform
   */
  protected def getNextFunction = {
    val f: (User) => Action = functionSequence(nextFunctionIndex)
    nextFunctionIndex = (nextFunctionIndex + 1) % functionSequence.length
    // If all function executed increment nbTime sequence executed
    if (nextFunctionIndex == 0) {
      nbOfTimeSequenceExecuted += 1
    }
    f
  }

  protected def hasNextFunction: Boolean = {
    // If other execution
    if (nbOfTimeSequenceExecuted == nbOfExecutionBeforeEnd) {
      false
    } else {
      true
    }
  }

  protected def startOrResumeUser = {
    // Get next function and execute
    if (hasNextFunction && processingStarted) {
      val nextAction: Action = getNextFunction.apply(this)
      // Execute next action
      lastActionResult = nextAction.execute(this)
    }

  }

  /**
   * Perform
   */
  protected def endTransaction(result: InjectorResult[Request, Option[Response]]) {
    // Keep reference on last transaction. Can be usefull for action with branche (if ... else ... )
    lastTransaction = result
    // Dump transaction if necessary
    if (dumpEnabled) {
      dumpActorRef match {
        case Some(dar) => dar ! result
        case None => warn("No dump actor")
      }
    }
    // Send result to report
    reportingActorRef match {
      case Some(rar) => rar ! result
      case None => warn("No reporting actor")
    }
  }

}

/**
 * Actor representing a user.
 * This implementation call in sequence different injector client implementation
 */
class SequentialUserActor(functionSequence: ListBuffer[(User) => Action], _dumpActorRef: Option[ActorRef], _reportingActorRef: Option[ActorRef], _httpClient: Option[ActorRef], _dumpEnabled: Boolean, _nbOfExecutionBeforeEnd:Int) extends User(functionSequence) with Actor {

  def this(functionSequence: ListBuffer[(User) => Action], _dumpActorRef: Option[ActorRef], _reportingActorRef: Option[ActorRef], _httpClient: Option[ActorRef]) = {this(functionSequence, _dumpActorRef, _reportingActorRef, _httpClient, false, 1)}

  protected val userActorRef: Option[ActorRef] = optionSelf

  protected def receive = {
    // Manage command
    case UserCommand.START => {
      startOrResumeUser
    }

      // Manage command
    case UserCommand.END_OF_ACTION => {
      startOrResumeUser
    }

    // Manage message
    case result: InjectorResult[Request, Option[Response]] => {
      // Receive a response from a previous request
      endTransaction(result)
      startOrResumeUser
    }

    // End of thinktime
    case EndOfThinktime => {
      endOfThinkTimeCallback
    }

  }

  protected val dumpEnabled = _dumpEnabled

  protected val nbOfExecutionBeforeEnd = _nbOfExecutionBeforeEnd

  // Http capabality impl
  protected def uuid = self.uuid

  protected val dumpActorRef: Option[ActorRef] = _dumpActorRef

  protected val reportingActorRef: Option[ActorRef] = _reportingActorRef

  protected val httpClient: Option[ActorRef] = _httpClient

}