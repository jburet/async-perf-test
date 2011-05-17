package fr.jbu.asyncperf.user.action

import fr.jbu.asyncperf.user.User
import fr.jbu.asyncperf.core.injector.http.HttpRequest

case class HttpCallAction(httpRequest:HttpRequest) extends Action {

  def execute(user: User) = {
    user.sendHttpRequest(this)
  }
}