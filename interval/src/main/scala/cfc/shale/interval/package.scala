package cfc.shale

import java.util.Date

import scala.concurrent.duration._

package object interval {

  def timeToNextInterval(interval: FiniteDuration,
      lastOption: Option[Date], now: Date = new Date()): FiniteDuration =
    (
      for (last <- lastOption) yield {
        val sinceLast = (now.getTime - last.getTime).millis
        Seq(interval - sinceLast, Duration.Zero).max
      }
    ).getOrElse(Duration.Zero)
}
