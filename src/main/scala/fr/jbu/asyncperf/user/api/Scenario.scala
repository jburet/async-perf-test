package fr.jbu.asyncperf.user.api

import collection.mutable.ListBuffer
import fr.jbu.asyncperf.user.User
import fr.jbu.asyncperf.core.injector.http.HttpRequest
import java.net.URI
import java.util.concurrent.TimeUnit
import scala.Function1
import fr.jbu.asyncperf.user.action.{ThinkTimeAction, HttpCallAction, Action}
import util.Random


abstract class Scenario {

  def getFunctionSequence: ListBuffer[(User) => Action]

  // Common function

  // ---------------------- HTTP --------------------//
  def httpGet(host: String, port: Int, path: String, queryParam: Option[Map[String, String]]): (User) => Action = {
    new Function1[User, Action] {
      def apply(user: User) = {
        new HttpCallAction(new HttpRequest(new URI("http", null, host, port, path, queryMapToString(queryParam), null)))
      }
    }
  }

  def httpGet(host: String, port: Int, path: String): (User) => Action = {
    httpGet(host, port, path, None)
  }


  //---------------------- SLEEP ---------------------//
  def wait(time: Int, timeUnit: TimeUnit): (User) => Action = {
    new Function1[User, Action] {
      def apply(v1: User) = {
        new ThinkTimeAction(time, timeUnit)
      }
    }
  }

  def wait(min: Int, max: Int, timeUnit: TimeUnit): (User) => Action = {
    new Function1[User, Action] {
      def apply(v1: User) = {
        new ThinkTimeAction((Random.nextFloat * (max - min)).intValue, timeUnit)
      }
    }
  }

  // Internal helper function
  protected def queryMapToString(queryMap: Option[Map[String, String]]): String = {
    queryMap match {
      case Some(map) => {
        map.foldLeft("")((res, entry) => res + entry)
      }
      case None => {
        ""
      }
    }
  }

}