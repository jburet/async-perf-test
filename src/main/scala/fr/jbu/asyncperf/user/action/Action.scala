package fr.jbu.asyncperf.user.action

import fr.jbu.asyncperf.user.User
import fr.jbu.asyncperf.user.action.ActionResult.ActionResult

/**
 * Represent user action or interaction
 * Can be something like :
 * - wait 5 second.
 * - Call a http server on url : /test with a post contains data ...
 * - Parse last http response...
 *
 */
abstract class Action{

  def execute(user:User): ActionResult

}

