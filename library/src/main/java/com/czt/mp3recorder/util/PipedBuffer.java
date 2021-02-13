package com.czt.mp3recorder.util;

import java.io.*;
import java.util.Arrays;

public class PipedBuffer extends OutputStream implements Closeable, Flushable {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private PipeOnBufferInListener onBufferInListener = null;

    public PipedBuffer() throws IOException {
        PipedInputStream input = new PipedInputStream();
        inputStream = input;
        outputStream = new PipedOutputStream(input);
    }

    public PipedBuffer(int pipeSize) throws IOException {
        PipedInputStream input = new PipedInputStream(pipeSize);
        inputStream = input;
        outputStream = new PipedOutputStream(input);
    }

    public PipedBuffer(File file) throws IOException {
        outputStream = new FileOutputStream(file, true);
        inputStream = new FileInputStream(file);
    }


    private void triggerOnBufferInListener(byte[] b, int off, int len) {
        PipeOnBufferInListener listener = onBufferInListener;
        if (listener != null && len > 0) {
            listener.OnBufferIn(Arrays.copyOfRange(b, off, off + len));
        }
    }

    /**
     * Writes the specified <code>byte</code> to the piped output stream.
     * <p>
     * Implements the <code>write</code> method of <code>OutputStream</code>.
     *
     * @param b the <code>byte</code> to be written.
     * @throws IOException if the pipe is <a href=#BROKEN> broken</a> unconnected,
     *                     closed, or if an I/O error occurs.
     */
    public void write(int b) throws IOException {
        outputStream.write(b);
        byte[] ba = new byte[1];
        ba[0] = (byte) b;
        triggerOnBufferInListener(ba, 0, 1);
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this piped output stream.
     * This method blocks until all the bytes are written to the output
     * stream.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if the pipe is <a href=#BROKEN> broken</a> unconnected,
     *                     closed, or if an I/O error occurs.
     */
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
        triggerOnBufferInListener(b, off, len);
    }

    /**
     * Writes <code>b.length</code> bytes from the specified byte array
     * to this output stream. The general contract for <code>write(b)</code>
     * is that it should have exactly the same effect as the call
     * <code>write(b, 0, b.length)</code>.
     *
     * @param b the data.
     * @throws IOException if an I/O error occurs.
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
        triggerOnBufferInListener(b, 0, b.length);
    }


    /**
     * Flushes this output stream and forces any buffered output bytes
     * to be written out. The general contract of <code>flush</code> is
     * that calling it is an indication that, if any bytes previously
     * written have been buffered by the implementation of the output
     * stream, such bytes should immediately be written to their
     * intended destination.
     * <p>
     * If the intended destination of this stream is an abstraction provided by
     * the underlying operating system, for example a file, then flushing the
     * stream guarantees only that bytes previously written to the stream are
     * passed to the operating system for writing; it does not guarantee that
     * they are actually written to a physical device such as a disk drive.
     * <p>
     * The <code>flush</code> method of <code>OutputStream</code> does nothing.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void flush() throws IOException {
        outputStream.flush();
    }

    /**
     * Reads the next byte of data from this piped input stream. The
     * value byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>.
     * This method blocks until input data is available, the end of the
     * stream is detected, or an exception is thrown.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     * stream is reached.
     * @throws IOException if the pipe is unconnected,
     *                     <a href="#BROKEN"> <code>broken</code></a>, closed,
     *                     or if an I/O error occurs.
     */
//    @Override
    public int read() throws IOException {
        return inputStream.read();
    }


    /**
     * Reads up to <code>len</code> bytes of data from this piped input
     * stream into an array of bytes. Less than <code>len</code> bytes
     * will be read if the end of the data stream is reached or if
     * <code>len</code> exceeds the pipe's buffer size.
     * If <code>len </code> is zero, then no bytes are read and 0 is returned;
     * otherwise, the method blocks until at least 1 byte of input is
     * available, end of the stream has been detected, or an exception is
     * thrown.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in the destination array <code>b</code>
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or
     * <code>-1</code> if there is no more data because the end of
     * the stream has been reached.
     * @throws NullPointerException      If <code>b</code> is <code>null</code>.
     * @throws IndexOutOfBoundsException If <code>off</code> is negative,
     *                                   <code>len</code> is negative, or <code>len</code> is greater than
     *                                   <code>b.length - off</code>
     * @throws IOException               if the pipe is <a href="#BROKEN"> <code>broken</code></a>,
     *                                   unconnected,
     *                                   closed, or if an I/O error occurs.
     */
//    @Override
    public int read(byte b[], int off, int len) throws IOException {
        return inputStream.read(b, off, len);
    }

    /**
     * Reads some number of bytes from the input stream and stores them into
     * the buffer array <code>b</code>. The number of bytes actually read is
     * returned as an integer.  This method blocks until input data is
     * available, end of file is detected, or an exception is thrown.
     *
     * <p> If the length of <code>b</code> is zero, then no bytes are read and
     * <code>0</code> is returned; otherwise, there is an attempt to read at
     * least one byte. If no byte is available because the stream is at the
     * end of the file, the value <code>-1</code> is returned; otherwise, at
     * least one byte is read and stored into <code>b</code>.
     *
     * <p> The first byte read is stored into element <code>b[0]</code>, the
     * next one into <code>b[1]</code>, and so on. The number of bytes read is,
     * at most, equal to the length of <code>b</code>. Let <i>k</i> be the
     * number of bytes actually read; these bytes will be stored in elements
     * <code>b[0]</code> through <code>b[</code><i>k</i><code>-1]</code>,
     * leaving elements <code>b[</code><i>k</i><code>]</code> through
     * <code>b[b.length-1]</code> unaffected.
     *
     * <p> The <code>read(b)</code> method for class <code>InputStream</code>
     * has the same effect as: <pre><code> read(b, 0, b.length) </code></pre>
     *
     * @param b the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or
     * <code>-1</code> if there is no more data because the end of
     * the stream has been reached.
     * @throws IOException          If the first byte cannot be read for any reason
     *                              other than the end of the file, if the input stream has been closed, or
     *                              if some other I/O error occurs.
     * @throws NullPointerException if <code>b</code> is <code>null</code>.
     * @see java.io.InputStream#read(byte[], int, int)
     */
//    @Override
    public int read(byte b[]) throws IOException, NullPointerException {
        return inputStream.read(b);
    }

    //    @Override
    public void close() throws IOException {
        inputStream.close();
    }


    public PipedBuffer setOnBufferInListener(PipeOnBufferInListener onBufferInListener) {
        this.onBufferInListener = onBufferInListener;
        return this;
    }
}
