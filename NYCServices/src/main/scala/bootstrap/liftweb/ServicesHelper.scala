package bootstrap.liftweb
import java.util.Date
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SQLConf
import play.api.libs.json.Json

object ServicesHelper {

  def overallStats(): String = {

    val df = SparkFactory.sqlContext.sql("select sum(num_bikes_available) as Bikes_Available, sum(num_docks_available) as Docks_Available from dynamicTable")
    df.toJSON.collect().mkString("")

  }

  def stationStats(stationID: String): String = {

    val df = SparkFactory.sqlContext.sql("select station_id, num_bikes_available, num_docks_available from dynamicTable where station_id = '" + stationID + "'")
    df.toJSON.collect().mkString("")

  }

  def stationsWithCapacity(numBikes: Int): String = {

    val df = SparkFactory.sqlContext.sql("select station_id from dynamicTable where num_bikes_available >= " + numBikes)
    df.toJSON.collect().mkString("")

  }

  def monthlyStats(month: Int): String = {

    SparkFactory.sqlContext.sql(
      """select s.station_id, s.capacity, Month(d.last_reported) as Month, d.num_bikes_available, d.num_bikes_disabled 
        from staticTable s join dynamicTable d on s.station_id = d.station_id""")
      .registerTempTable("joinedData")

    val resultDF = SparkFactory.sqlContext.sql("""select Month, Sum(num_bikes_disabled) as Disabled_Bikes, 
      Sum(capacity-(num_bikes_available+num_bikes_disabled)) as Bike_Rides from joinedData where Month = """ + month + """ group by Month """)

    resultDF.toJSON.collect().mkString("")
  }

  def popularStation(month: Int): String = {

    val joinedData = SparkFactory.sqlContext.sql(
      """select s.station_id, s.capacity, Month(d.last_reported) as Month, d.num_bikes_available, d.num_bikes_disabled, s.capacity-(d.num_bikes_available+d.num_bikes_disabled) as Bike_Rides 
        from staticTable s join dynamicTable d on s.station_id = d.station_id""")

    joinedData.registerTempTable("joinedData")
    joinedData.show()

    val resultDF = SparkFactory.sqlContext.sql("""select station_id from joinedData where Month = """ + month + """ order by Bike_Rides desc""").head()

    return Json.stringify(Json.toJson(Map("station_id: " -> resultDF.toSeq(0).toString())))

  }

}