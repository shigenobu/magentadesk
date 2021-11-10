package com.walksocket.md.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdFile;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;

import java.io.IOException;
import java.util.*;

public class MdServerRequest {

  private HttpExchange exchange;

  private Map<String, List<String>> headers;

  /**
   * lower headers.
   */
  private Map<String, List<String>> lowerHeaders = new LinkedHashMap<>();

  private Map<String, List<String>> queryParams;

  private Map<String, List<String>> bodyParams;

  public MdServerRequest(HttpExchange exchange) {
    this.exchange = exchange;
  }

  private void parseHeader() {
    if (headers != null) {
      return;
    }

    headers = new HashMap<>();
    Headers pairs = exchange.getRequestHeaders();
    if (pairs == null || pairs.size() == 0) {
      return;
    }
    for (Map.Entry<String, List<String>> pair : pairs.entrySet()) {
      headers.put(pair.getKey(), pair.getValue());
      lowerHeaders.put(pair.getKey().toLowerCase(Locale.ROOT), pair.getValue());
    }
  }

  private void parseQuery() {
    if (queryParams != null) {
      return;
    }

    queryParams = new HashMap<>();
    if (MdUtils.isNullOrEmpty(exchange.getRequestURI().getRawQuery())) {
      return;
    }
    String[] pairs = exchange.getRequestURI().getRawQuery().split("&");
    for (String pair : pairs) {
      String[] kv = pair.split("=");
      if (MdUtils.isNullOrEmpty(kv)) {
        continue;
      }
      String k = kv[0];
      String v = "";
      if (kv.length > 1) {
        v = kv[1];
      }

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

    bodyParams = new HashMap<>();
    String[] pairs = null;
    try {
      pairs = MdFile.readString(exchange.getRequestBody()).split("&");
    } catch (Exception e) {
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
    List<String> values = getHeaders(name);
    if (MdUtils.isNullOrEmpty(values)) {
      return "";
    }
    return values.get(0);
  }

  public List<String> getHeaders(String name) {
    parseHeader();
    name = name.toLowerCase(Locale.ROOT); // to lower
    if (!lowerHeaders.containsKey(name)) {
      return new ArrayList<>();
    }
    return lowerHeaders.get(name);
  }

  public String getQueryParam(String name) {
    List<String> values = getQueryParams(name);
    if (MdUtils.isNullOrEmpty(values)) {
      return "";
    }
    return values.get(0);
  }

  public List<String> getQueryParams(String name) {
    parseQuery();
    if (!queryParams.containsKey(name)) {
      return new ArrayList<>();
    }
    return queryParams.get(name);
  }

  public String getBodyParam(String name) {
    List<String> values = getBodyParams(name);
    if (MdUtils.isNullOrEmpty(values)) {
      return "";
    }
    return values.get(0);
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
