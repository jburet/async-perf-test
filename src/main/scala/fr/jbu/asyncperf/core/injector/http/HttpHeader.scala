package fr.jbu.asyncperf.core.injector.http

class HttpHeader(headerMap: collection.mutable.Map[String, String]) {
  def header(headerKey: String): Option[String] = {
    headerMap.get(headerKey)
  }

      def toXML =
        {headerMap}
}