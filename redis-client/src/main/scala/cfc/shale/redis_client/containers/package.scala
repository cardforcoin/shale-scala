package cfc.shale.redis_client

import scalaz._
import scalaz.Scalaz._
import scalaz.BijectionT._

import scala.language.higherKinds

package object containers {

  // https://stackoverflow.com/questions/19455470/lifting-a-bijection-into-a-functor
  implicit class BijectionLifter[A, B](val bij: A <@> B) extends AnyVal {
    def liftInto[F[_]: Functor]: F[A] <@> F[B] = bijection[Id, Id, F[A], F[B]](
      _ map bij.to,
      _ map bij.from
    )
  }
}
