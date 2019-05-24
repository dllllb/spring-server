package io.github.dmitrib.ext

import org.junit.Test

class LocalTimeSequienceTest {
  @Test def testSimple() {
    val s = new LocalTimeSequence(12)
    println(s.next)
    println(s.next)
  }
}
