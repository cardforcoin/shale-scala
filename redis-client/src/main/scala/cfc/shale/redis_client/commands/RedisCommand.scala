package cfc.shale.redis_client.commands

import cfc.shale.redis_client.Redis

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

  /** Create a `RedisCommand` that will evaluate `a` when the task runs. */
  def apply[A](a: => A): RedisCommand[A] = task(_ => Task(a))

  def task[A](f: Redis => Task[A]): RedisCommand[A] =
    new RedisCommand[A] {
      override def task(implicit redis: Redis) = f(redis)
    }

  /** Convert a strict value to a `RedisCommand`. */
  def now[A](value: A): RedisCommand[A] = task(_ => Task.now(value))

  def gatherUnordered[A](commands: Seq[RedisCommand[A]]): RedisCommand[List[A]] =
    RedisCommand.task[List[A]](implicit redis => Task.gatherUnordered(
      commands.map(_.task), exceptionCancels = true))

  def reduceUnordered[A, M](commands: Seq[RedisCommand[A]])
      (implicit R: Reducer[A, M]): RedisCommand[M] =
    RedisCommand.task(implicit redis => Task.reduceUnordered(
      commands.map(_.task), exceptionCancels = true))

  def reduceUnorderedList[A](commands: Seq[RedisCommand[A]])
      (implicit R: Reducer[A, List[A]]): RedisCommand[List[A]] =
    RedisCommand.task(implicit redis => Task.reduceUnordered(
      commands.map(_.task), exceptionCancels = true))
}
