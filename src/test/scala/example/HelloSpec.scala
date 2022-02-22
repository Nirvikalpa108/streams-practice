package example

import example.Hello.runningMean
import fs2.Stream
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HelloSpec extends AnyFlatSpec with Matchers {
  "The Hello object" should "" in {
    val stream = Stream(1,2,3,4,5)
    val result = runningMean(stream).compile.toList.unsafeRunSync()
    result shouldEqual List(0.0, 1.0, 1.0, 2.0, 2.0, 3.0)
  }
}
