package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdFile;
import com.walksocket.md.MdLogger;

import java.io.*;

public class MdHtmlEndpointAsset extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdLogger.trace(getClass().getSimpleName() + ":" + exchange.getRequestURI());

    int len = 0;
    int status = 200;
    byte[] data = null;
    String path = exchange.getRequestURI().getPath();

    String assetPath = ltrim(path, '/');
    if (path.equals("/favicon.ico")) {
      assetPath = "asset/favicon.ico";
    }
    try (InputStream in = MdHtmlEndpointAsset.class.getClassLoader().getResourceAsStream(assetPath)) {
      data = MdFile.readByteArray(in);
      len = data.length;
    } catch (FileNotFoundException e) {
      MdLogger.error(e);
      status = 404;
    }

    exchange.sendResponseHeaders(status, len);
    exchange.getResponseHeaders().set("Content-Length", String.valueOf(len));
    exchange.getResponseHeaders().set("Content-Type", getContentType(assetPath));
    exchange.getResponseHeaders().set("Connection", "close");

    MdLogger.trace(String.format("%s", status));
    MdLogger.trace(String.format("Content-Length: %s", len));
    MdLogger.trace(String.format("Content-Type: %s", getContentType(assetPath)));
    MdLogger.trace(String.format("Connection: %s", "close"));

    OutputStream os = exchange.getResponseBody();
    if (data != null) {
      MdLogger.trace(data);
      os.write(data);
    }
    os.close();
  }

  private String getContentType(String path) {
    String contentType = "application/octet-stream";

    String extension = getExtension(path);
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

  private static String getExtension(String path) {
    String extension = "";
    int point = path.lastIndexOf(".");
    if (point != -1) {
      extension = path.substring(point + 1).toLowerCase();
    }
    return extension;
  }

  private static String ltrim(String value, char trim) {
    int len= value.length();
    int i = 0;
    char[] c = value.toCharArray();
    while ((i < len) && (c[i] <= trim)) {
      i++;
    }
    return (i > 0 ? value.substring(i) : value);
  }
}
