package fr.jbu.asyncperf.user

object UserCommand {

  abstract sealed class UserCommand

  case object START extends UserCommand


}