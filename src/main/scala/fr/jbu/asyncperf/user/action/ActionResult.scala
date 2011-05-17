package fr.jbu.asyncperf.user.action


object ActionResult {
  abstract sealed class ActionResult

  // Result for synchronous action (match ...)
  case object Success extends ActionResult

  case object Error extends ActionResult

  case object Match extends ActionResult

  case object NotMatch extends ActionResult

  // Result for asynchronous action (http internalRequest...)
  case object Send extends ActionResult
}
