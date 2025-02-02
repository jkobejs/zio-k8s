package com.coralogix.zio.k8s.quicklens

import com.coralogix.zio.k8s.client.model.Optional
import com.coralogix.zio.k8s.client.model.Optional._
import com.softwaremill.quicklens._
import zio.test.environment.TestEnvironment
import zio.test.Assertion._
import zio.test._

object QuicklensOptionalSpec extends DefaultRunnableSpec {

  case class X(inner: Optional[Y])
  case class Y(leaf: Optional[Int])

  override def spec: ZSpec[TestEnvironment, Any] =
    suite("Quicklens support")(
      test("works on optionals") {
        val a = X(Y(None))
        val f = modify[X, Optional[Int]](_: X)(_.inner.each.leaf).setTo(5)
        val g = modify(_: X)(_.inner.each.leaf.each)(_ + 1)
        val b = g(f(a))

        assert(b)(equalTo(X(Y(6))))
      }
    )
}
