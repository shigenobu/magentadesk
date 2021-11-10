package com.walksocket.md.server;

/**
 * server utils.
 */
public class MdServeUtils {

  /**
   * get content type.
   * @param extension extension
   * @return content type
   */
  public static String getContentType(String extension) {
    String contentType = "application/octet-stream";
    if (extension.equals("ico")) {
      contentType = "image/x-icon";
    } else if (extension.equals("js")) {
      contentType = "text/javascript";
    } else if (extension.equals("css")) {
      contentType = "text/css";
    } else if (extension.equals("jpg") || extension.equals("jpeg")) {
      contentType = "image/jpeg";
    } else if (extension.equals("png")) {
      contentType = "image/png";
    } else if (extension.equals("gif")) {
      contentType = "image/gif";
    } else if (extension.equals("svg")) {
      contentType = "image/svg+xml";
    }
    return contentType;
  }

  /**
   * get extension.
   * @param path path
   * @return extension
   */
  public static String getExtension(String path) {
    String extension = "";
    int point = path.lastIndexOf(".");
    if (point != -1) {
      extension = path.substring(point + 1).toLowerCase();
    }
    return extension;
  }

  /**
   * ltrim.
   * @param value value
   * @param trim trim char
   * @return trim value
   */
  public static String ltrim(String value, char trim) {
    int len= value.length();
    int i = 0;
    char[] c = value.toCharArray();
    while ((i < len) && (c[i] <= trim)) {
      i++;
    }
    return (i > 0 ? value.substring(i) : value);
  }
}
