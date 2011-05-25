package fr.jbu.asyncperf.util.nio

import java.io.Writer
import java.nio.ByteBuffer


class ByteBufferWriter(byteBuffer: ByteBuffer) extends Writer {
  def close() {}

  def flush() {}

  def write(cbuf: Array[Char], off: Int, len: Int) {
    // If remaining < length to write
    if (byteBuffer.asCharBuffer.remaining < len) {
      byteBuffer.asCharBuffer.put(cbuf, off, byteBuffer.asCharBuffer.remaining)
    } else {
      byteBuffer.asCharBuffer.put(cbuf, off, len)
    }
  }
}