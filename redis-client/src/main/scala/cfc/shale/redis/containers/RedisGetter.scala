package cfc.shale.redis.containers

import cfc.shale.redis.Redis
import cfc.shale.redis.commands.RedisCommand

import scalaz.Reducer
import scalaz.concurrent.Task

trait RedisGetter[+A] { self =>

  def getCommand: RedisCommand[A]

  def task(implicit redis: Redis): Task[A] =
    getCommand.task

  def map[B](f: A => B): RedisGetter[B] =
    RedisGetter.command(getCommand.map(f))
  
  def flatMap[B](f: A => RedisGetter[B]): RedisGetter[B] =
    RedisGetter.command(getCommand.flatMap(f.andThen(_.getCommand)))
}

object RedisGetter {

  /** Create a `RedisCommand` that will evaluate `a` when the task runs. */
  def apply[A](a: => A): RedisCommand[A] =
    new RedisCommand[A] {
      def task(implicit redis: Redis) = Task(a)
    }

  def command[A](command: RedisCommand[A]) = new RedisGetter[A] {
    override def getCommand: RedisCommand[A] = command
  }

  def task[A](f: Redis => Task[A]): RedisGetter[A] =
    RedisGetter.command[A](RedisCommand.task(f))

  /** Convert a strict value to a `RedisCommand`. */
  def now[A](value: A) = new RedisGetter[A] {
    override def getCommand = RedisCommand.now(value)
  }

  def gatherUnordered[A](getters: Seq[RedisGetter[A]],
      exceptionCancels: Boolean = false) =
    RedisGetter.task(implicit redis => Task.gatherUnordered(
      getters.map(_.task), exceptionCancels = exceptionCancels))

  def reduceUnordered[A, M](tasks: Seq[Task[A]],
      exceptionCancels: Boolean = false)(implicit R: Reducer[A, M]) =
    RedisGetter.task(_ => Task.reduceUnordered(
      tasks, exceptionCancels = exceptionCancels))
}
