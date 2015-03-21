package cfc.shale.interval

import scala.concurrent.duration._
import java.util.Date

class TestInterval extends org.scalatest.FreeSpec {

  "Interval" - {

    "Last was a long time ago" in assert (
      timeToNextInterval(
        interval=100.millis,
        lastOption=Some(new Date(0)),
        now=new Date(5000)
      ) === Duration.Zero
    )

    "Last was very recent" in assert (
      timeToNextInterval(
        interval=100.millis,
        lastOption=Some(new Date(4997)),
        now=new Date(5000)
      ) === 97.millis
    )

    "No last event" in assert (
      timeToNextInterval(
        interval=100.millis,
        lastOption=None,
        now=new Date(5000)
      ) === Duration.Zero
    )
  }
}
