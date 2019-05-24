package io.github.dmitrib.ext

import java.util.concurrent.atomic.AtomicInteger

class LocalTimeSequence(val nodeId: Int) {
  private val localSeq = new AtomicInteger

  def next = {
    val seq = localSeq.incrementAndGet
    val localTime = System.currentTimeMillis
    val res = (localTime << 16) | ((nodeId % 256) << 8) | (seq % 256)
    res
  }
}