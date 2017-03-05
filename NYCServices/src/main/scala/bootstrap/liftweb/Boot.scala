package bootstrap.liftweb

import org.apache.log4j.Logger
import net.liftweb.http._
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

class Boot extends Bootable {
  private lazy val logger = Logger.getLogger(this.getClass.getName)

  def boot {

    val sc = SparkFactory.sc
    val sqlContext = SparkFactory.sqlContext
    val sdf = SparkFactory.dynamicDF
    val ddf = SparkFactory.staticDF

    // Binding Service as a Restful API
    LiftRules.statelessDispatchTable.append(WebServices);
    // resolve the trailing slash issue
    LiftRules.statelessRewrite.prepend({
      case RewriteRequest(ParsePath(path, _, _, true), _, _) if path.last == "index" => RewriteResponse(path.init)
    })

    logger.info("Booting webservices...")
    logger.info("Starting job: NYC...")

  }
}