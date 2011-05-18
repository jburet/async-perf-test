import sbt._

class ScalableInjector(info: ProjectInfo) extends DefaultProject(info) with AkkaProject with BasicScalaIntegrationTesting {

  val typedActor = akkaModule("actor")
  val testKit = akkaModule("testkit")
  val scalatest = "org.scalatest" % "scalatest_2.9.0" % "1.4.1"
  val httpcoreNio = "org.apache.httpcomponents" % "httpcore-nio" % "4.1"
  val easymock = "org.easymock" % "easymock" % "3.0"
  val asyncHttpClient = "com.ning" % "async-http-client" % "1.6.2"
  val logback = "ch.qos.logback" % "logback-classic" % "0.9.28" % "runtime"

}

