package fr.jbu.asyncperf.user.action

import fr.jbu.asyncperf.user.User
import java.util.concurrent.TimeUnit


class ThinkTimeAction(thinkTime: Int, timeUnit:TimeUnit) extends Action {
  def execute(user: User) = {
    user.thinkTimeRequest(thinkTime, timeUnit)
    ActionResult.Send
  }
}

object EndOfThinktime {

}