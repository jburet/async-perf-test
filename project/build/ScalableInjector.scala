import sbt.{ProjectInfo, DefaultProject}

class ScalableInjector(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {

  val typedActor = akkaModule("actor")
  val testKit = akkaModule("testkit")
  val scalatest = "org.scalatest" % "scalatest_2.9.0" % "1.4.1"
  val httpcoreNio = "org.apache.httpcomponents" % "httpcore-nio" % "4.1"
  val easymock = "org.easymock" % "easymock" % "3.0"
  val asyncHttpClient = "com.ning" % "async-http-client" % "1.6.2"

}

