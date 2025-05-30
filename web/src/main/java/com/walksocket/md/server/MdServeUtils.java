package com.walksocket.md.server;

/**
 * server utils.
 */
public class MdServeUtils {

  /**
   * get content type.
   *
   * @param extension extension
   * @return content type
   */
  public static String getContentType(String extension) {
    String contentType = switch (extension) {
      case "ico" -> "image/x-icon";
      case "js" -> "text/javascript";
      case "css" -> "text/css";
      case "jpg", "jpeg" -> "image/jpeg";
      case "png" -> "image/png";
      case "gif" -> "image/gif";
      case "svg" -> "image/svg+xml";
      default -> "application/octet-stream";
    };
    return contentType;
  }

  /**
   * get extension.
   *
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
   *
   * @param value value
   * @param trim  trim char
   * @return trim value
   */
  public static String ltrim(String value, char trim) {
    int len = value.length();
    int i = 0;
    char[] c = value.toCharArray();
    while ((i < len) && (c[i] <= trim)) {
      i++;
    }
    return (i > 0 ? value.substring(i) : value);
  }
}
