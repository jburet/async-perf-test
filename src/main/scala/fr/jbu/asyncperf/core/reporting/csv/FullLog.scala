package fr.jbu.asyncperf.core.reporting.csv

import fr.jbu.asyncperf.core.reporting.Reporting
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import fr.jbu.asyncperf.core.injector.{RequestType, Response, Request, InjectorResult}
import java.nio.file.{StandardOpenOption, Paths, Path}
import fr.jbu.asyncperf.core.injector.http.HttpMethod.HttpMethod
import fr.jbu.asyncperf.core.injector.http.{HttpMethod, HttpResponse, HttpRequest}

/**
 *
 * THIS CLASS IS NOT THREADSAFE. ONE INSTANCE MUST BE USED ONLY BY ONE REPORTING ACTOR
 *
 * Log all trasction in a file with a csv like format
 * Format
 * HTTP: TIMESTAMP,REQUEST_TYPE,HTTP_METHOD,REQUEST_URI,QUERY,STATUS_CODE,RESPONSE_TIME
 */
class FullLog(csvFilePath: String) extends Reporting {

  // Date format for timestamp
  val dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS")
  val csvLogSeparator = ",".getBytes
  val filePath: Path = Paths.get(csvFilePath)
  filePath.exists match {
    case false => {

    }
    case true => {
      filePath.delete
    }
  }
  val channel = filePath.newByteChannel(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)
  val buffer: ByteBuffer = ByteBuffer.allocate(1024)

  def logTransaction(injectorResult: InjectorResult[Request, Option[Response]]) = {
    fillBufferWithPerfReport(buffer, injectorResult)
    buffer.flip
    while (buffer.hasRemaining()) {
      channel.write(buffer);
    }
    buffer.clear
  }

  def endAndCloseReport() {
    while (buffer.hasRemaining()) {
      channel.write(buffer);
    }
    channel.close
  }

  private def fillBufferWithPerfReport(buffer: ByteBuffer, injectorResult: InjectorResult[Request, Option[Response]]): ByteBuffer = {
    buffer.put(dateFormat.format(injectorResult.request.timestamp).getBytes)
    buffer.put(csvLogSeparator)
    injectorResult.request match {
      case httpRequest: HttpRequest => {
        buffer.put(RequestType.http.getBytes)
        buffer.put(csvLogSeparator)
        buffer.put(getHttpMethodLogName(httpRequest.httpMethod).getBytes)
        buffer.put(csvLogSeparator)
        buffer.put(httpRequest.requestUri.getBytes)
        buffer.put(csvLogSeparator)
        buffer.put(httpRequest.queryParam.foldLeft("")((res, entry) => res + entry).getBytes)
        buffer.put(csvLogSeparator)
      }
      case _ => {
        buffer.put(RequestType.unknown.getBytes)
        buffer.put(csvLogSeparator)
      }
    }
    injectorResult.response match {
      case Some(httpResponse: HttpResponse) => {
        buffer.put(httpResponse.code.toString.getBytes)
        buffer.put(csvLogSeparator)
      }
      case _ => {
        // Do nothing
      }
    }
    buffer.put(((injectorResult.endNanoTime - injectorResult.startNanoTime) / 1000 / 1000).toString.getBytes)
    buffer.put("\n".getBytes)
  }

  private def getHttpMethodLogName(httpMethod: HttpMethod): String = {
    httpMethod match {
      case HttpMethod.GET => {
        "GET"
      }
      case HttpMethod.POST => {
        "POST"
      }
      case HttpMethod.HEAD => {
        "HEAD"
      }
      case HttpMethod.PUT => {
        "PUT"
      }
      case HttpMethod.DELETE => {
        "DELETE"
      }
      case _ => {
        ""
      }
    }
  }

}