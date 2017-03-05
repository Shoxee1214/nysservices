package bootstrap.liftweb

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.Logging

/**
 * @author shahroz
 */
object SparkFactory extends Logging {

  val conf = new SparkConf(true).set("spark.cassandra.connection.host", "0.0.0.0")
  val sc = new SparkContext("local[*]", "NYC Bike Rentals", conf)
  val sqlContext = new org.apache.spark.sql.SQLContext(sc)
  import sqlContext.implicits._

  val dynamicDF = sqlContext
    .read
    .format("org.apache.spark.sql.cassandra")
    .options(Map("table" -> "NYCDynamicData", "keyspace" -> "NYCBikesData"))
    .load().cache()

  val staticDF = sqlContext
    .read
    .format("org.apache.spark.sql.cassandra")
    .options(Map("table" -> "NYCStaticData", "keyspace" -> "NYCBikesData"))
    .load().cache()

  dynamicDF.registerTempTable("dynamicTable")
  staticDF.registerTempTable("staticTable")
  

}