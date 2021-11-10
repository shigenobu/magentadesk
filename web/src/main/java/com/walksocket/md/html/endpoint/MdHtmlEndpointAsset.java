package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdFile;
import com.walksocket.md.MdLogger;
import com.walksocket.md.server.MdServeUtils;

import java.io.*;

/**
 * html endpoint asset.
 */
public class MdHtmlEndpointAsset extends MdHtmlEndpointAbstract {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    init(exchange);
    MdLogger.trace(getClass().getSimpleName() + ":" + request.getPath());

    int status = 200;
    byte[] data = null;
    String path = request.getPath();

    String assetPath = MdServeUtils.ltrim(path, '/');
    if (path.equals("/favicon.ico")) {
      assetPath = "asset/favicon.ico";
    }
    try (InputStream in = MdHtmlEndpointAsset.class.getClassLoader().getResourceAsStream(assetPath)) {
      data = MdFile.readByteArray(in);
    } catch (FileNotFoundException e) {
      MdLogger.error(e);
      status = 404;
    }

    response.setStatus(status);
    response.setContentType(MdServeUtils.getContentType(MdServeUtils.getExtension(assetPath)));
    response.setCacheControl("public, max-age=600");
    response.send(data);
  }
}
