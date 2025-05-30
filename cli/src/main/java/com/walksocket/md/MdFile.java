package com.walksocket.md;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;

/**
 * file.
 */
public class MdFile {

  /**
   * constructor.
   */
  private MdFile() {
  }

  /**
   * read string from input stream.
   *
   * @param in input stream
   * @return string
   * @throws IOException close error
   */
  public static String readString(InputStream in) throws IOException {
    byte[] data = readByteArray(in);
    return new String(data, StandardCharsets.UTF_8);
  }

  /**
   * read byte from input stream.
   *
   * @param in input stream
   * @return byte array
   * @throws IOException close error
   */
  public static byte[] readByteArray(InputStream in) throws IOException {
    byte[] data;
    try (ReadableByteChannel channel = Channels.newChannel(in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      int bufferLen = 2048;
      ByteBuffer buffer = ByteBuffer.allocate(bufferLen);
      int len;
      while ((len = channel.read(buffer)) >= 0) {
        baos.write(buffer.array(), 0, len);
        ((Buffer) buffer).clear();
      }
      data = baos.toByteArray();
    }
    return data;
  }

  /**
   * write string to file.
   *
   * @param path file path
   * @param data data
   * @throws IOException close error
   */
  public static void writeString(String path, String data) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(path);
        FileChannel channel = fos.getChannel()) {
      ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
      channel.write(buffer);
    }
  }
}
