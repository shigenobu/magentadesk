package com.walksocket.md.server;

import com.walksocket.md.MdLogger;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;

/**
 * server codec.
 */
public class MdServerCodec {

  /**
   * url encode.
   *
   * @param target string
   * @return encoded string
   */
  public static String urlEncode(String target) {
    if (target == null) {
      return "";
    }
    URLCodec codec = new URLCodec();
    try {
      return codec.encode(target, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      MdLogger.error(e);
    }
    return "";
  }

  /**
   * url decode.
   *
   * @param target string
   * @return decoded string
   */
  public static String urlDecode(String target) {
    if (target == null) {
      return "";
    }
    URLCodec codec = new URLCodec();
    try {
      return codec.decode(target, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException | DecoderException e) {
      MdLogger.error(e);
    }
    return "";
  }
}
