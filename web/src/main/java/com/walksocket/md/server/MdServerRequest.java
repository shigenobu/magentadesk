package com.walksocket.md.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdFile;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MdServerRequest {

  /**
   * exchange.
   */
  private final HttpExchange exchange;

  /**
   * headers.
   */
  private Map<String, List<String>> headers;

  /**
   * lower headers.
   */
  private final Map<String, List<String>> lowerHeaders = new LinkedHashMap<>();

  /**
   * query params.
   */
  private Map<String, List<String>> queryParams;

  /**
   * body params.
   */
  private Map<String, List<String>> bodyParams;

  /**
   * constructor.
   *
   * @param exchange exchange
   */
  public MdServerRequest(HttpExchange exchange) {
    this.exchange = exchange;
  }

  /**
   * parse header.
   */
  private void parseHeader() {
    if (headers != null) {
      return;
    }

    headers = new HashMap<>();
    Headers pairs = exchange.getRequestHeaders();
    if (pairs == null || pairs.isEmpty()) {
      return;
    }
    for (Map.Entry<String, List<String>> pair : pairs.entrySet()) {
      headers.put(pair.getKey(), pair.getValue());
      lowerHeaders.put(pair.getKey().toLowerCase(Locale.ROOT), pair.getValue());
    }
  }

  /**
   * parse query.
   */
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
      String k = MdServerCodec.urlDecode(kv[0]);
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

  /**
   * parse body.
   */
  private void parseBody() {
    if (bodyParams != null) {
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
      String k = MdServerCodec.urlDecode(kv[0]);
      String v = "";
      if (kv.length > 1) {
        v = kv[1];
      }

      if (!bodyParams.containsKey(k)) {
        bodyParams.put(k, new ArrayList<>());
      }
      bodyParams.get(k).add(MdServerCodec.urlDecode(v));
    }
  }

  /**
   * is post.
   *
   * @return if post, true
   */
  public boolean isPost() {
    return exchange.getRequestMethod().equals("POST");
  }

  /**
   * is get.
   *
   * @return if get, true
   */
  public boolean isGet() {
    return exchange.getRequestMethod().equals("GET");
  }

  /**
   * is put.
   *
   * @return if put, true
   */
  public boolean isPut() {
    return exchange.getRequestMethod().equals("PUT");
  }

  /**
   * is delete.
   *
   * @return if delete, true
   */
  public boolean isDelete() {
    return exchange.getRequestMethod().equals("DELETE");
  }

  /**
   * get path.
   *
   * @return path
   */
  public String getPath() {
    return exchange.getRequestURI().getPath();
  }

  /**
   * get header.
   *
   * @param name name
   * @return value
   */
  public String getHeader(String name) {
    List<String> values = getHeaders(name);
    if (MdUtils.isNullOrEmpty(values)) {
      return "";
    }
    return values.get(0);
  }

  /**
   * get headers.
   *
   * @param name name
   * @return values
   */
  public List<String> getHeaders(String name) {
    parseHeader();
    name = name.toLowerCase(Locale.ROOT); // to lower
    if (!lowerHeaders.containsKey(name)) {
      return new ArrayList<>();
    }
    return lowerHeaders.get(name);
  }

  /**
   * get query param.
   *
   * @param name name
   * @return value
   */
  public String getQueryParam(String name) {
    List<String> values = getQueryParams(name);
    if (MdUtils.isNullOrEmpty(values)) {
      return "";
    }
    return values.get(0);
  }

  /**
   * get query params.
   *
   * @param name name
   * @return values
   */
  public List<String> getQueryParams(String name) {
    parseQuery();
    if (!queryParams.containsKey(name)) {
      return new ArrayList<>();
    }
    return queryParams.get(name);
  }

  /**
   * get body param.
   *
   * @param name name
   * @return value
   */
  public String getBodyParam(String name) {
    List<String> values = getBodyParams(name);
    if (MdUtils.isNullOrEmpty(values)) {
      return "";
    }
    return values.get(0);
  }

  /**
   * get body params.
   *
   * @param name name
   * @return values
   */
  public List<String> getBodyParams(String name) {
    parseBody();
    if (!bodyParams.containsKey(name)) {
      return new ArrayList<>();
    }
    return bodyParams.get(name);
  }

  /**
   * get raw query.
   *
   * @return raw query string
   */
  public String getRawQuery() {
    return exchange.getRequestURI().getRawQuery();
  }

  /**
   * get raw body.
   *
   * @return raw body string
   */
  public String getRawBody() {
    try {
      return MdFile.readString(exchange.getRequestBody());
    } catch (IOException e) {
      MdLogger.error(e);
    }
    return "";
  }
}
