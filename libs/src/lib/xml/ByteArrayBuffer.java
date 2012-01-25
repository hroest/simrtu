/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.xml;

import java.io.IOException;
import java.io.OutputStream;

//-----------------------------------------------------------------------------
// Class
//-----------------------------------------------------------------------------

/**
 * <p>
 * A replacement for {@link java.io.ByteArrayOutputStream}. A large part of
 * the functionnal code is copied from the Sun CLDC implementation of the
 * class {@link java.io.ByteArrayOutputStream}
 * </p>
 *
 * <p>
 * The following optimizations have been made :
 *
 * <ul>
 * <li>
 * The       method <tt>toByteArray()</tt> has been removed to avoid large
 * memory usage.
 * </li>
 * <li>
 * Methods   are NOT synchronized : it is up to the user to take care of that.
 * </li>
 * </ul>
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com"> Alexis BIETTI </a>
 * @version 1.0
 */
public class ByteArrayBuffer extends OutputStream {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------

  /** The default capacity */
  public static final int DEFAULT_CAPACITY = 32;

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** The buffer where data is stored. */
  public byte[] buf = null;

  /** The number of valid bytes in the buffer. */
  public int count = 0;

  /** Flag indicating whether the stream has been closed. */
  public boolean isClosed = false;

  //---------------------------------------------------------------------------
  // Constructors
  //---------------------------------------------------------------------------

  /**
   * Creates a new byte array output stream. The buffer capacity is initially
   * 32 bytes, though its size increases if necessary.
   */
  public ByteArrayBuffer() {
    this(DEFAULT_CAPACITY);
  }

  /**
   * Creates a new byte array output stream, with a buffer capacity of the
   * specified size, in bytes.
   *
   * @param size the initial size.
   *
   * @exception IllegalArgumentException if size is negative.
   */
  public ByteArrayBuffer(int initialCapacity) {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException();
    }

    buf = new byte[initialCapacity];
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * Check to make sure that the stream has not been closed
   */
  private void ensureOpen() {
    if (isClosed) {
      throw new RuntimeException("Writing to closed ByteArrayBuffer");
    }
  }

  /**
   * Writes the specified byte to this byte array output stream.
   *
   * @param b the byte to be written.
   */
  public void write(int b) {
    ensureOpen();

    int newcount = count + 1;

    if (newcount > buf.length) {
      byte[] newbuf = new byte[Math.max(buf.length << 1, newcount)];
      System.arraycopy(buf, 0, newbuf, 0, count);
      buf = newbuf;
    }

    buf[count]   = (byte) b;
    count        = newcount;
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array starting at
   * offset <code>off</code> to this byte array output stream.
   *
   * @param b the data.
   * @param off the start offset in the data.
   * @param len the number of bytes to write.
   */
  public void write(byte[] b, int off, int len) {
    ensureOpen();

    if (
      (off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)
          || ((off + len) < 0)) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return;
    }

    int newcount = count + len;

    if (newcount > buf.length) {
      byte[] newbuf = new byte[Math.max(buf.length << 1, newcount)];
      System.arraycopy(buf, 0, newbuf, 0, count);
      buf = newbuf;
    }

    System.arraycopy(b, off, buf, count, len);
    count = newcount;
  }

  /**
   * Resets the <code>count</code> field of this byte array output stream to
   * zero, so that all currently accumulated output in the output stream is
   * discarded. The output stream can be used again, reusing the already
   * allocated buffer space.
   *
   * @see java.io.ByteArrayInputStream#count
   */
  public void reset() {
    ensureOpen();
    count = 0;
  }

  /**
   * Clear this object so that it can be reused for a new usage.
   */
  public void reuse() {
    this.isClosed = false;
    reset();
  }

  /**
   * Write some bytes contained in this buffer to the specified {@link
   * java.io.OutputStream}
   *
   * @param dest a destination {@link java.io.OutputStream}
   * @param off start index of the buffer to write
   * @param len number of bytes to write to the output
   *
   * @throws IOException if an I/O exception occurs in the {@link
   *         java.io.OutputStream}
   * @throws IndexOutOfBoundsException if <tt> off </tt> or <tt> len </tt> is
   *         invalid for the current state of the buffer.
   */
  public void output(OutputStream dest, int off, int len)
    throws IOException {
    dest.write(buf, off, len);
  }

  /**
   * Write all the bytes contained in this buffer to the specified {@link
   * java.io.OutputStream}
   *
   * @param dest a destination {@link java.io.OutputStream}
   *
   * @throws IOException if an I/O exception occurs in the {@link
   *         java.io.OutputStream}
   */
  public void output(OutputStream dest) throws IOException {
    output(dest, 0, count);
  }

  /**
   * Converts the buffer's contents into a string, translating bytes into
   * characters according to the platform's default character encoding.
   *
   * @return String translated from the buffer's contents.
   *
   * @since JDK1.1
   */
  public String toString() {
    char[] c = new char[this.count];

    for (int i = count; --i >= 0;) {
      c[i] = (char) this.buf[i];
    }

    return new String(c, 0, this.count);
  }

  /**
   * Closes this output stream and releases any system resources associated
   * with this stream. A closed stream cannot perform output operations and
   * cannot be reopened.
   */
  public void close() throws IOException {
    isClosed = true;
  }

  /**
   * Get the byte at the specified index.
   *
   * @param index index in the buffer
   *
   * @return the byte at the specified index
   *
   * @throws ArrayIndexOutOfBounds
   */
  public byte getByteAt(int index) {
    return buf[index];
  }

  /**
   * Return the byte at the specified index, starting from the last byte. E.g:
   * <code>getByteAtFromEnd(0)</code> will return the last byte of this
   * buffer.
   *
   * @param indexFromEnd
   *
   * @return byte
   */
  public byte getByteAtFromEnd(int indexFromEnd) {
    return buf[count - 1 - indexFromEnd];
  }

  /**
   * Returns the number of valid bytes in this buffer.
   *
   * @return the number of valid bytes in this buffer.
   */
  public int size() {
    return count;
  }

  /**
   * Return the current capacity of this buffer.
   *
   * @return int
   */
  public int capacity() {
    return buf.length;
  }
}

