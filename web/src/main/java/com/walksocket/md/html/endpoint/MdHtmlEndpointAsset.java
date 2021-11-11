package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdFile;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.server.MdServeUtils;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * html endpoint asset or favicon.ico.
 */
public class MdHtmlEndpointAsset extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    MdHtmlStatus status = MdHtmlStatus.OK;
    byte[] data = null;
    String path = request.getPath();

    String assetPath = MdServeUtils.ltrim(path, '/');
    if (path.equals("/favicon.ico")) {
      assetPath = "asset/favicon.ico";
    }
    if (MdUtils.isNullOrEmpty(getBasePath())) {
      try (InputStream in = MdHtmlEndpointAsset.class.getClassLoader().getResourceAsStream(assetPath)) {
        data = MdFile.readByteArray(in);
      } catch (FileNotFoundException e) {
        MdLogger.error(e);
        status = MdHtmlStatus.NOT_FOUND;
      }
    } else {
      try (InputStream in = new FileInputStream(new File(getBasePath(), assetPath))) {
        data = MdFile.readByteArray(in);
      } catch (FileNotFoundException e) {
        MdLogger.error(e);
        status = MdHtmlStatus.NOT_FOUND;
      }
    }

    response.setStatus(status.getStatus());
    response.setContentType(MdServeUtils.getContentType(MdServeUtils.getExtension(assetPath)));
    response.setCacheControl("public, max-age=600");
    response.setBody(data);
    response.send();
  }
}
