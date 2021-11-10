package com.walksocket.md.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdFile;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MdServerRequest {

  private HttpExchange exchange;

  private Map<String, List<String>> headers;

  private Map<String, List<String>> queryParams;

  private Map<String, List<String>> bodyParams;

  public MdServerRequest(HttpExchange exchange) {
    this.exchange = exchange;
  }

  private void parseHeader() {
    if (!MdUtils.isNullOrEmpty(headers)) {
      return;
    }

    headers = new HashMap<>();
    Headers pairs = exchange.getRequestHeaders();
    if (pairs == null || pairs.size() == 0) {
      return;
    }
    for (Map.Entry<String, List<String>> pair : pairs.entrySet()) {
      queryParams.put(pair.getKey(), pair.getValue());
    }
  }

  private void parseQuery() {
    if (!MdUtils.isNullOrEmpty(queryParams)) {
      return;
    }

    queryParams = new HashMap<>();
    String[] pairs = exchange.getRequestURI().getRawQuery().split("&");
    if (MdUtils.isNullOrEmpty(pairs)) {
      return;
    }
    for (String pair : pairs) {
      String[] kv = pair.split("=");
      if (MdUtils.isNullOrEmpty(kv)) {
        continue;
      }
      String k = kv[0];
      String v = kv[1];

      if (!queryParams.containsKey(k)) {
        queryParams.put(k, new ArrayList<>());
      }
      queryParams.get(k).add(MdServerCodec.urlDecode(v));
    }
  }

  private void parseBody() {
    if (!MdUtils.isNullOrEmpty(bodyParams)) {
      return;
    }

    queryParams = new HashMap<>();
    String[] pairs = new String[0];
    try {
      pairs = MdFile.readString(exchange.getRequestBody()).split("&");
    } catch (IOException e) {
      MdLogger.error(e);
    }
    if (MdUtils.isNullOrEmpty(pairs)) {
      return;
    }
    for (String pair : pairs) {
      String[] kv = pair.split("=");
      if (MdUtils.isNullOrEmpty(kv)) {
        continue;
      }
      String k = kv[0];
      String v = kv[1];

      if (!bodyParams.containsKey(k)) {
        bodyParams.put(k, new ArrayList<>());
      }
      bodyParams.get(k).add(MdServerCodec.urlDecode(v));
    }
  }

  /**
   * is post.
   * @return if post, true
   */
  public boolean isPost() {
    return exchange.getRequestMethod().equals("POST");
  }

  /**
   * is get.
   * @return if get, true
   */
  public boolean isGet() {
    return exchange.getRequestMethod().equals("GET");
  }

  /**
   * is put.
   * @return if put, true
   */
  public boolean isPut() {
    return exchange.getRequestMethod().equals("PUT");
  }

  /**
   * is delete.
   * @return if delete, true
   */
  public boolean isDelete() {
    return exchange.getRequestMethod().equals("DELETE");
  }

  public String getPath() {
    return exchange.getRequestURI().getPath();
  }

  public String getHeader(String name) {
    parseHeader();
    if (!headers.containsKey(name)) {
      return "";
    }
    return headers.get(name).get(0);
  }

  public List<String> getHeaders(String name) {
    parseQuery();
    if (!headers.containsKey(name)) {
      return new ArrayList<>();
    }
    return headers.get(name);
  }

  public String getQueryParam(String name) {
    parseQuery();
    if (!queryParams.containsKey(name)) {
      return "";
    }
    return queryParams.get(name).get(0);
  }

  public List<String> getQueryParams(String name) {
    parseQuery();
    if (!queryParams.containsKey(name)) {
      return new ArrayList<>();
    }
    return queryParams.get(name);
  }

  public String getBodyParam(String name) {
    parseBody();
    if (!bodyParams.containsKey(name)) {
      return "";
    }
    return bodyParams.get(name).get(0);
  }

  public List<String> getBodyParams(String name) {
    parseBody();
    if (!bodyParams.containsKey(name)) {
      return new ArrayList<>();
    }
    return bodyParams.get(name);
  }

  public String getRawQuery() {
    return exchange.getRequestURI().getRawQuery();
  }

  public String getRawBody() {
    try {
      return MdFile.readString(exchange.getRequestBody());
    } catch (IOException e) {
      MdLogger.error(e);
    }
    return "";
  }
}
