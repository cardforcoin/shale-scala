package cfc.shale.redis.containers

import cfc.shale.redis.Redis
import cfc.shale.redis.commands.RedisCommand

import scala.language.{higherKinds, implicitConversions}
import scalaz.Reducer
import scalaz.concurrent.Task

trait RedisGetter[+A] { self =>

  def getCommand: RedisCommand[A]

  def task(implicit redis: Redis): Task[A] =
    getCommand.task

  def map[B](f: A => B): RedisGetter[B] =
    RedisGetter(getCommand.map(f))
  
  def flatMap[B](f: A => RedisGetter[B]): RedisGetter[B] =
    RedisGetter(getCommand.flatMap(f.andThen(_.getCommand)))
}

object RedisGetter {

  def apply[A](command: RedisCommand[A]) = new RedisGetter[A] {
    override def getCommand: RedisCommand[A] = command
  }

  def gatherUnordered[A](getters: Seq[RedisGetter[A]],
      exceptionCancels: Boolean = false) =
    RedisGetter[List[A]](
      new RedisCommand[List[A]] {
        override def task(implicit redis: Redis) =
          Task.gatherUnordered(
            getters.map(_.task),
            exceptionCancels = exceptionCancels
          )
      }
    )

  def reduceUnordered[A, M](tasks: Seq[Task[A]],
      exceptionCancels: Boolean = false)(implicit R: Reducer[A, M]) =
    RedisGetter[M](
      new RedisCommand[M] {
        override def task(implicit redis: Redis) =
          Task.reduceUnordered(
            tasks,
            exceptionCancels = exceptionCancels
          )
      }
    )
}
