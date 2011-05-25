package fr.jbu.asyncperf.user

object UserCommand {

  abstract sealed class UserCommand

  case object START extends UserCommand
  case object END_OF_ACTION extends UserCommand


}