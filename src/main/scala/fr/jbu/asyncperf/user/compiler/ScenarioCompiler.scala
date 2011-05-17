package fr.jbu.asyncperf.user.compiler

import java.io.PrintWriter
import tools.nsc.Settings
import tools.nsc.interpreter.{Results, IMain}

class ScenarioCompiler {

  val out = new java.io.StringWriter()
  val settings: Settings = new Settings()
  settings.usejavacp.value = true

  val interpreter = new IMain(settings, new PrintWriter(out))

  def interpret(scalaCode: String) = {
    interpreter.interpret(scalaCode) match {
      case Results.Success => {
        println("Interpreting ok")
      }
      case _ => {
        println("Error in interpreting code")
      }
    }
  }

}