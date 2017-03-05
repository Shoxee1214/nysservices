package bootstrap.liftweb

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.Req
import net.liftweb.http.S
import net.liftweb.http.PlainTextResponse

object WebServices extends RestHelper {

  serve {
    case Get("api" :: "overallstats" :: Nil, req) => for {
      tempHolder <- S.param("tempHolder") ?~ "tempHolder parameter not found."
    } yield PlainTextResponse(ServicesHelper.overallStats())
  }

  serve {
    case Get("api" :: "stationsstats" :: Nil, req) => for {
      stationsID <- S.param("stationid") ?~ "stationid parameter not found."
    } yield PlainTextResponse(ServicesHelper.stationStats(stationsID.toString))
  }

  serve {
    case Get("api" :: "stationswithcapacity" :: Nil, req) => for {
      numBikes <- S.param("numBikes") ?~ "numBIkes parameter not found."
    } yield PlainTextResponse(ServicesHelper.stationsWithCapacity(numBikes.toInt))
  }

  serve {
    case Get("api" :: "monthlystats" :: Nil, req) => for {
      month <- S.param("month") ?~ "month parameter not found."
    } yield PlainTextResponse(ServicesHelper.monthlyStats(month.toInt))
  }

  serve {
    case Get("api" :: "popularstation" :: Nil, req) => for {
      month <- S.param("month") ?~ "month parameter not found."
    } yield PlainTextResponse(ServicesHelper.popularStation(month.toInt))
  }

}
