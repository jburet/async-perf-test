package fr.jbu.asyncperf.user.action

import fr.jbu.asyncperf.user.User
import fr.jbu.asyncperf.core.injector.InjectorResult
import fr.jbu.asyncperf.core.injector.http.{HttpResponse, HttpRequest}

class HttpStatusCodeMatchAction(result: InjectorResult[HttpRequest, Option[HttpResponse]], exceptedStatusCode: Int) extends Action {
  def execute(user: User) = {
    result.response match {
      case None => ActionResult.NotMatch
      case Some(response) => if (response.code == exceptedStatusCode) ActionResult.Match else ActionResult.NotMatch
    }

  }
}
