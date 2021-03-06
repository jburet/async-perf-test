package fr.jbu.asyncperf.core.reporting.csv

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import fr.jbu.asyncperf.core.injector.InjectorResult
import fr.jbu.asyncperf.core.injector.http.{HttpHeader, HttpRequest, HttpResponse}
import collection.mutable.HashMap
import java.nio.file.{Paths, Files}
import java.net.URI
import java.nio.ByteBuffer

class TestCsvFullLog extends FunSuite with ShouldMatchers {

  test("log a result with httprequest and httpresponse") {
    val filePath = "src/test/target/testCsvLogger.csv"
    Paths.get(filePath).deleteIfExists
    val logger: FullLog = new FullLog(filePath)
    // Create a mock result
    val injectorResult = new InjectorResult[HttpRequest, Option[HttpResponse]](
      new HttpRequest(new URI("http://test.fr/test")),
      Some(new HttpResponse(200, ByteBuffer.wrap("La belle page de test".getBytes), "http://test.fr/test", new HttpHeader(new HashMap[String, String]))),
      0, 1000000000)
    logger.logTransaction(injectorResult)
    logger.endAndCloseReport
    // assert too few. Only file is present and not null
    Paths.get(filePath).exists should equal (true)
  }

}