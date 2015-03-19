package cfc.shale.redis.commands

import cfc.shale.redis.Redis

import scalaz.Reducer
import scalaz.concurrent.Task

trait RedisCommand[+A] { self =>

  def task(implicit redis: Redis): Task[A]

  def map[B](f: A => B): RedisCommand[B] =
    new RedisCommand[B] {
      override def task(implicit redis: Redis) = self.task.map(f)
    }

  def flatMap[B](f: A => RedisCommand[B]): RedisCommand[B] =
    new RedisCommand[B] {
      override def task(implicit redis: Redis) =
        self.task.flatMap(a => f(a).task)
    }
}

object RedisCommand {

  def now[A](value: A): RedisCommand[A] =
    new RedisCommand[A] {
      def task(implicit redis: Redis) = Task.now(value)
    }

  def gatherUnordered[A](commands: Seq[RedisCommand[A]],
      exceptionCancels: Boolean = false) =
    new RedisCommand[List[A]] {
      override def task(implicit redis: Redis) =
        Task.gatherUnordered(
          commands.map(_.task),
          exceptionCancels = exceptionCancels
        )
    }

  def reduceUnordered[A, M](tasks: Seq[Task[A]],
      exceptionCancels: Boolean = false)(implicit R: Reducer[A, M]) =
    new RedisCommand[M] {
      override def task(implicit redis: Redis) =
        Task.reduceUnordered(
          tasks,
          exceptionCancels = exceptionCancels
        )
    }
}
