package cfc.shale.redis_client

import org.joda.time.Duration

sealed trait DurationInt
sealed trait DurationIntOkay extends DurationInt
case class TooLarge(duration: Duration) extends DurationInt
case class MillisDuration(millis: Int) extends DurationIntOkay
case class SecondsDuration(value: Int) extends DurationIntOkay

object DurationInt {

  def apply(duration: Duration): DurationInt = {
    val millis = duration.getMillis
    toIntOption(millis) match {
      case Some(millisInt) =>
        MillisDuration(millisInt)
      case None =>
        val seconds = (millis / 1000.0).round
        toIntOption(seconds) match {
          case Some(secondsInt) => SecondsDuration(secondsInt)
          case None => TooLarge(duration)
        }
    }
  }

  def orThrow(duration: Duration): DurationIntOkay =
    apply(duration) match {
      case x: DurationIntOkay => x
      case _: TooLarge =>
        throw new IllegalArgumentException(s"Too large: $duration")
    }

  private def toIntOption(x: Long): Option[Int] =
    if (x >= Int.MinValue && x <= Int.MaxValue)
      Some(x.toInt)
    else
      None
}
