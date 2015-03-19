package cfc.shale.redis.containers

import scalaz.BijectionT.Bijection

trait RedisContainer[A]
    extends RedisGetter[A] with RedisSetter[A] { self =>

  def biject[B](implicit bijection: Bijection[A, B]): RedisContainer[B] =
    new RedisContainer[B] {

      override def getCommand =
        self.getCommand.map(bijection.to)

      override def setCommand(value: B) =
        self.setCommand(bijection.from(value))
    }
}
