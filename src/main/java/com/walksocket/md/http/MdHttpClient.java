package com.walksocket.md.http;

import com.walksocket.md.MdLogger;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * http client.
 */
public class MdHttpClient {

  /**
   * default timeout seconds.
   */
  public static final int DEFAULT_TIMEOUT = 30;

  /**
   * line break.
   */
  private static final String LINE_BREAK = "\r\n";

  /**
   * user agent.
   */
  private static final String USER_AGENT = "magentadesk-http-client";

  /**
   * url.
   */
  private String url;

  /**
   * timeout seconds.
   */
  private int timeout;

  /**
   * constructor.
   * @param url url
   * @param timeout timeout seconds
   */
  public MdHttpClient(String url, int timeout) {
    this.url = url;
    this.timeout = timeout;
  }

  /**
   * do post
   * @param body body string.
   * @return response
   */
  public MdHttpClientResponse doPost(String body) {
    String responseEncoding = null;
    int responseStatus = 0;
    Map<String, List<String>> responseHeaders = new LinkedHashMap<String, List<String>>();
    byte[] responseBody = null;

    HttpURLConnection con = null;
    OutputStreamWriter osw = null;

    try {
      // connect
      con = getConnection(url);
      con.setDoOutput(true);
      con.setConnectTimeout(timeout);

      // request -----
      // header
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-type", "application/json; charset=UTF8");
      con.addRequestProperty("User-Agent", USER_AGENT);

      // body
      osw = new OutputStreamWriter(con.getOutputStream());
      osw.write(body + LINE_BREAK);
      osw.flush();

      // response -----
      // status
      responseStatus = con.getResponseCode();

      // encoding
      responseEncoding = getResponseEncoding(con);

      // header
      responseHeaders = con.getHeaderFields();

      // body
      responseBody = getResponseBody(con);

    } catch (Exception e) {
      MdLogger.error(e);
    } finally {
      if (osw != null) {
        try {
          osw.close();
        } catch (Exception e) {
          MdLogger.error(e);
        }
      }
      if (con != null) {
        con.disconnect();
      }
    }
    return new MdHttpClientResponse(responseEncoding, responseStatus, responseHeaders, responseBody);
  }

  /**
   * get connection.
   * <pre>
   *   ignore illegal ssl.
   * </pre>
   * @param uri URI
   * @return http connection
   * @throws Exception security exception
   */
  private HttpURLConnection getConnection(String uri) throws Exception {
    HttpURLConnection urlconn = null;
    URL url = new URL(uri);
    if (url.getProtocol().equals("https")) {
      TrustManager[] tms = {new X509TrustManager() {

        @Override
        public void checkClientTrusted(
            java.security.cert.X509Certificate[] arg0, String arg1)
            throws CertificateException {
        }

        @Override
        public void checkServerTrusted(
            java.security.cert.X509Certificate[] arg0, String arg1)
            throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
        }
      }};
      SSLContext sslcontext = SSLContext.getInstance("SSL");
      sslcontext.init(null, tms, null);
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

        @Override
        public boolean verify(String arg0, SSLSession arg1) {
          return true;
        }
      });

      urlconn = (HttpsURLConnection) url.openConnection();
      ((HttpsURLConnection)urlconn).setSSLSocketFactory(sslcontext.getSocketFactory());
    } else {
      urlconn = (HttpURLConnection) url.openConnection();
    }
    return urlconn;
  }

  /**
   * get response encoding.
   * @param con http connection
   * @return response encoding
   */
  private String getResponseEncoding(HttpURLConnection con) {
    String responseEncoding = con.getContentEncoding();
    if (responseEncoding == null) {
      String contentType = con.getContentType();
      if (contentType == null) {
        return null;
      }
      String target = "charset=";
      int index = contentType.toLowerCase().indexOf(target);
      if (index >= 0) {
        responseEncoding = contentType.substring(index + target.length());
      } else {
        responseEncoding = StandardCharsets.UTF_8.name();
      }
    }
    return responseEncoding;
  }

  /**
   * get response body.
   * @param con http connection
   * @return response body
   */
  private byte[] getResponseBody(HttpURLConnection con) {
    byte[] responseBody = null;

    DataInputStream stream = null;
    ReadableByteChannel channel = null;
    ByteArrayOutputStream baos = null;
    try {
      int len = (int) con.getContentLengthLong();
      if (len >= 0) {
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
          stream = new DataInputStream(con.getInputStream());
        } else {
          stream = new DataInputStream(con.getErrorStream());
        }
        responseBody = new byte[len];
        stream.readFully(responseBody, 0, len);
      } else {
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
          channel = Channels.newChannel(con.getInputStream());
        } else {
          channel = Channels.newChannel(con.getErrorStream());
        }
        int bufferLen = 2048;

        baos = new ByteArrayOutputStream();
        ByteBuffer buffer = ByteBuffer.allocate(bufferLen);

        int readLen = 0;
        while (readLen >= 0) {
          buffer.rewind();
          readLen = channel.read(buffer);
          if (readLen > 0) {
            byte[] dataBytes = buffer.array();
            baos.write(dataBytes, 0, readLen);
          }
          buffer.clear();
        }
        responseBody = baos.toByteArray();
      }
    } catch (Exception e) {
      MdLogger.error(e);
    } finally {
      if (stream != null) {
        try {
          stream.close();
        } catch (IOException e) {
          MdLogger.error(e);
        }
      }
      if (channel != null) {
        try {
          channel.close();
        } catch (IOException e) {
          MdLogger.error(e);
        }
      }
      if (baos != null) {
        try {
          baos.close();
        } catch (IOException e) {
          MdLogger.error(e);
        }
      }
    }
    return responseBody;
  }

  /**
   * http response.
   */
  public class MdHttpClientResponse {

    /**
     * encoding.
     */
    private String encoding;

    /**
     * status.
     */
    private int status;

    /**
     * headers.
     */
    private Map<String, List<String>> headers;

    /**
     * body.
     */
    private byte[] body;

    /**
     * constructor.
     * @param encoding encoding
     * @param status status
     * @param headers headers
     * @param body body
     */
    private MdHttpClientResponse(String encoding, int status,
                          Map<String, List<String>> headers, byte[] body) {
      this.encoding = encoding;
      this.status = status;
      this.headers = headers;
      this.body = body;
    }

    /**
     * is success.
     * @return if 200 is true, else false
     */
    public boolean isSuccessful() {
      if (status == HttpURLConnection.HTTP_OK) {
        return true;
      }
      return false;
    }

    /**
     * get status.
     * @return status
     */
    public int getStatus() {
      return status;
    }

    /**
     * get header.
     * @param name header name
     * @return header value
     */
    public String getHeader(String name) {
      if (headers != null && headers.containsKey(name) && headers.get(name).size() > 0) {
        return headers.get(name).get(0);
      }
      return "";
    }

    /**
     * get headers.
     * @param name header name
     * @return header values
     */
    public List<String> getHeaders(String name) {
      if (headers != null && headers.containsKey(name)) {
        return headers.get(name);
      }
      return new ArrayList<String>();
    }

    /**
     * get body by String.
     * @return body by String
     */
    public String getBodyString() {
      String bodyString = "";
      try {
        if (encoding != null && body != null) {
          bodyString = new String(body, 0, body.length, encoding);
        }
      } catch (UnsupportedEncodingException e) {
        MdLogger.error(e);
      }
      return bodyString;
    }

    /**
     * get body by byte array.
     * @return body by byte array
     */
    public byte[] getBodyByteArray() {
      return body;
    }

    @Override
    public String toString() {
      StringBuffer buffer = new StringBuffer();

      // ----------
      // 1
      buffer.append("HTTP/1.1");
      buffer.append(" ");
      buffer.append(status);
      buffer.append(LINE_BREAK);

      // ----------
      // header
      if (headers != null) {
        headers.forEach((headerName, headerValue) -> {
          buffer.append(String.format("%s: %s", headerName, headerValue));
          buffer.append(LINE_BREAK);
        });
      }

      // ----------
      // body
      buffer.append(LINE_BREAK);
      buffer.append(getBodyString());

      return "MdHttpClientResponse{" +
          buffer.toString() +
          '}';
    }
  }
}
