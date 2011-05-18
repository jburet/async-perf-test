package fr.jbu.asyncperf.core.reporting.memory

import fr.jbu.asyncperf.core.reporting.Reporting
import fr.jbu.asyncperf.core.injector.{Response, Request, InjectorResult}
import collection.mutable.HashMap

/**
 * Make consolidation on n last second.
 * 
 * In HTTP
 * HttpMethod + query -> response time
 * HttpMethod + query -> throughput
 * HttpMethod + query -> status code
 *
 * Keep in memory latest (configure number) consolidation
 */
class ConsolidatedResponseTime extends Reporting {

  val currentTimeConsolidation = new HashMap[String, Int]
  val currentThroughputConsolidation = new HashMap[String, Int]
  val currentStatusConsolidation = new HashMap[String, Int]

  def logTransaction(transaction: InjectorResult[Request, Option[Response]]) = {
    
  }
}