package com.walksocket.md.web.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdFile;
import com.walksocket.md.MdJson;
import com.walksocket.md.MdUtils;
import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.input.MdInputDiff;

import java.io.IOException;
import java.io.OutputStream;

/**
 * web endpoint api diff reserve.
 */
public class MdWebEndpointApiDiffReserve extends MdWebEndpointAbstract {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    // init
    String body = "";
    String contentType = "application/json; encoding=UTF8";

    // check
    String inputJson = MdFile.readString(exchange.getRequestBody());
    MdInputDiff inputDiff = MdJson.toObject(inputJson, MdInputDiff.class);
    if (inputDiff == null) {
      // bad request
      body = "bad request";
      exchange.getResponseHeaders().set("Content-Length", String.valueOf(body.length()));
      exchange.getResponseHeaders().set("Content-Type", contentType);
      exchange.sendResponseHeaders(400, body.length());
      OutputStream os = exchange.getResponseBody();
      os.write(body.getBytes());
      os.close();
      return;
    }
    try {
      inputDiff.validate();
    } catch (MdExceptionAbstract me) {
      // bad request
      body = "bad request";
      exchange.getResponseHeaders().set("Content-Length", String.valueOf(body.length()));
      exchange.getResponseHeaders().set("Content-Type", contentType);
      exchange.sendResponseHeaders(400, body.length());
      OutputStream os = exchange.getResponseBody();
      os.write(body.getBytes());
      os.close();
      return;
    }

    // executionId
    String executionId = MdUtils.randomString();

    // ok
    body = "ok";
    exchange.getResponseHeaders().set("Content-Length", String.valueOf(body.length()));
    exchange.getResponseHeaders().set("Content-Type", contentType);
    exchange.sendResponseHeaders(200, body.length());
    OutputStream os = exchange.getResponseBody();
    os.write(body.getBytes());
    os.close();
    return;
  }
}
