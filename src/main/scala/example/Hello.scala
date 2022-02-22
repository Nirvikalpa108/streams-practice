package example

import cats.effect.IO
import fs2.{INothing, Pure, Stream}

import scala.util.control.NonFatal

//TODO write test for runningSum
// more operations after runningSum

object Hello {
  def main(args: Array[String]): Unit = {
    val stream = Stream(1,2,3,4,5)

    val programme = for {
      n <- runningMean(stream)
      _ <- Stream.eval(IO(println(n)))
    } yield ()

    programme.compile.drain.unsafeRunSync()
    //println(tmp)
  }

  def runningSum(ns: Stream[IO, Int]): Stream[IO, Int] = ns.scan(0) { (acc, n) =>
    n + acc
  }

  def runningMedian(ns: Stream[IO, Int]): Stream[IO, Int] = ns.scan(0) { (acc, n) =>
    ???
  }

  def runningMean(ns: Stream[IO, Int]): Stream[IO, Double] = {
    ns.scan((0, 0)) { case ((sum, count), n) =>
      (n + sum, 1 + count)
    }.map { case (sum, count) => sum/count}

    for {
      (sum, count) <- ns.scan((0, 0)) { case ((sum, count), n) => (n + sum, 1 + count)}
    } yield if (count == 0) 0 else sum / count
  }

  // https://fs2.io/#/guide?id=table-of-contents
  // A Stream[F,O] represents a discrete stream of O values which may request evaluation of F effects.
  // We'll call F the effect type and O the output type.
  val s0: Stream[Pure, INothing] = Stream.empty
  val s1: Stream[Pure, Int] = Stream.emit(1) //effect type is Pure, which means it does not require evaluation of any effects to produce its output.
  val s2: Stream[Pure, Int] = Stream(1,2,3)
  val s3: Stream[Pure, Int] = Stream.emits(List(1,2,3))

  // Streams that don't use any effects are called pure streams.
  // Pure streams can be converted to a List or Vector:
  s1.toList
  s1.toVector

  // Streams have a lot of useful list like functions:
  val s4 = Stream(1,2)
  val s5 = Stream(3,4)
  (s4 ++ s5).toList
  s4.map(_ + 1).toList
  s4.filter(_ % 2 == 0).toList
  s4.fold(0)(_ + _).toList

  // FS2 streams can also include evaluation of effects:
  import cats.effect.IO
  // IO is an effect time that has no side effects,
  // and Stream.eval doesn't do anything at the time of creation,
  // it's a description of what needs to happen when the stream is eventually interpreted.
  val effect: Stream[IO, Int] = Stream.eval(IO {println("I am being run"); 1 + 1})
  // here is the signature of eval:
  def eval[F[_],A](f: F[A]): Stream[F,A] = ???

  //mapping from a domain to a co-domain
  //ANY string to ANY int
  def totalFunction(s: String): Int = ???
  //total function - defined for all inputs
  //we use defs for total functions, so we don't have to write the below
  val anotherTotalFunction: String => Int = ???
  //partial function - not defined for all inputs
  def squareRoot(i: Int): Int = i match {
    case 4 => 2
    case 9 => 3
    case 16 => 4
    case _ => 0
  }
  // use vals for partial functions
  val partialSquareRoot: PartialFunction[Int, Int] = {
    case 4 => 2
    case 9 => 3
    case 16 => 4
  }
  partialSquareRoot.orElse()
  // partial functions are not used that much in Scala, except for exception handling
  // eg handle a runtime exception or a certain type of exception
  // try catch is a good example of a partial function
  try {
    "abc".toInt
  } catch { case NonFatal(e) =>
    ???
  }
}

