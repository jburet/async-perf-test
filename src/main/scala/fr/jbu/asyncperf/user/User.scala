package fr.jbu.asyncperf.user

import action.Action
import action.ActionResult.ActionResult
import collection.mutable.ListBuffer
import fr.jbu.asyncperf.core.injector.{InjectorResult, Request, Response}
import akka.actor.{ActorRef, Actor}

abstract class User(functionSequence: ListBuffer[(User) => Action]) extends HttpCapability {

  /**
   * Index of next function to execute
   */
  private var nextFunctionIndex: Int = 0

  protected var dumpActorRef: ActorRef

  protected val dumpEnabled: Boolean

  protected var reportingActorRef: ActorRef

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
  var lastTransaction: InjectorResult[Request, Response] = null

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

  /**
   * Perform
   */
  protected def endTransaction(transaction: InjectorResult[Request, Response]) {
    // Keep reference on last transaction. Can be usefull for action with branche (if ... else ... )
    lastTransaction = transaction
    // Dump transaction if necessary
    if (dumpEnabled) {
      dumpActorRef ! transaction
    }
    // Send result to report
    reportingActorRef ! transaction
  }

}

/**
 * Actor representing a user.
 * This implementation call in sequence different injector client implementation
 */
class SequentialUserActor(functionSequence: ListBuffer[(User) => Action], _dumpActorRef: ActorRef, _reportingActorRef: ActorRef, _httpClient: ActorRef, _dumpEnabled: Boolean) extends User(functionSequence) with Actor {

  protected def startUser = {
    // Get next function and execute
    while (hasNextFunction) {
      val nextAction: Action = getNextFunction.apply(this)
      // Execute next action
      lastActionResult = nextAction.execute(this)
    }

  }

  protected def receive = {
    // Manage command
    case UserCommand.START => {
      startUser
    }

    // Manage message
    case transaction: InjectorResult[Request, Response] => {
      // Receive a response from a previous request
      endTransaction(transaction)
    }

  }

  protected val dumpEnabled = _dumpEnabled

  protected val nbOfExecutionBeforeEnd = 1

  // Http capabality impl
  protected def uuid = self.uuid

  protected var dumpActorRef: ActorRef = _dumpActorRef

  protected var reportingActorRef: ActorRef = _reportingActorRef

  protected var httpClient: ActorRef = _httpClient


}