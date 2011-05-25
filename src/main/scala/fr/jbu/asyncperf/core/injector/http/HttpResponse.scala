package fr.jbu.asyncperf.core.injector.http

import fr.jbu.asyncperf.core.injector.Response
import java.nio.ByteBuffer
import xml.{Unparsed, Node}

case class HttpResponse(code: Int, body: ByteBuffer, uri: String, header: HttpHeader) extends Response {

  def toXML: scala.xml.Elem =
    <http-response>
      <code>
        {code}
      </code>
      <uri>
        {uri}
      </uri>
      <header>
        {header}
      </header>
      <body>
        {PCDATA(new String(body.array()))}
      </body>
    </http-response>


}

object PCDATA {
  def apply(in: String): Node = Unparsed("<![CDATA[" + in + "]]>")
}