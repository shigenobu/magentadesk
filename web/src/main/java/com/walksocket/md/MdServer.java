package com.walksocket.md;

import com.sun.net.httpserver.HttpServer;
import com.walksocket.md.api.MdApiQueue;
import com.walksocket.md.api.endpoint.MdApiEndpointCheck;
import com.walksocket.md.api.endpoint.MdApiEndpointReserve;
import com.walksocket.md.exception.MdExceptionErrorLocalDatabase;
import com.walksocket.md.html.endpoint.MdHtmlEndpointAsset;
import com.walksocket.md.html.endpoint.MdHtmlEndpointCheck;
import com.walksocket.md.html.endpoint.MdHtmlEndpointDiffConfig;
import com.walksocket.md.html.endpoint.MdHtmlEndpointIndex;
import com.walksocket.md.html.endpoint.MdHtmlEndpointJson;
import com.walksocket.md.html.endpoint.MdHtmlEndpointPreset;
import com.walksocket.md.html.endpoint.MdHtmlEndpointProject;
import com.walksocket.md.html.endpoint.MdHtmlEndpointReserve;
import com.walksocket.md.html.endpoint.MdHtmlEndpointSyncConfig;
import com.walksocket.md.service.MdServiceProcessing;
import com.walksocket.md.service.MdServiceReserved;
import com.walksocket.md.sqlite.MdSqliteConnection;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * server.
 */
public class MdServer implements AutoCloseable {

  /**
   * host.
   */
  private final String host;

  /**
   * port.
   */
  private final int port;

  /**
   * server.
   */
  private HttpServer server;

  /**
   * service message reserved.
   */
  private ScheduledExecutorService serviceMessageReserved;

  /**
   * service message processing.
   */
  private ScheduledExecutorService serviceMessageProcessing;

  /**
   * service server.
   */
  private ExecutorService serviceServer;

  /**
   * constructor.
   *
   * @param host host
   * @param port port
   */
  public MdServer(String host, int port) {
    this.host = host;
    this.port = port;
  }

  /**
   * start.
   *
   * @throws Exception several error
   */
  public void start() throws Exception {
    try (MdSqliteConnection con = new MdSqliteConnection()) {
      // begin
      con.begin();

      // ddl
      initDdl(con);

      // commit
      con.commit();
    } catch (Exception e) {
      MdLogger.error(e);
      throw new MdExceptionErrorLocalDatabase();
    }

    // create queue
    MdApiQueue queue = new MdApiQueue();

    // init reserved message service
    serviceMessageReserved = Executors.newSingleThreadScheduledExecutor();
    serviceMessageReserved.scheduleAtFixedRate(
        new MdServiceReserved(queue),
        3,
        3,
        TimeUnit.SECONDS);

    // init processing message service
    serviceMessageProcessing = Executors.newSingleThreadScheduledExecutor();
    serviceMessageProcessing.scheduleAtFixedRate(
        new MdServiceProcessing(queue),
        3,
        3,
        TimeUnit.SECONDS);

    // init server service
    serviceServer = Executors.newSingleThreadExecutor();
    serviceServer.submit(() -> {
      try {
        server = HttpServer.create(new InetSocketAddress(host, port), 64);

        // -----
        // endpoints
        // api
        server.createContext("/api/diff/reserve.json", new MdApiEndpointReserve());
        server.createContext("/api/diff/check.json", new MdApiEndpointCheck());
        server.createContext("/api/sync/reserve.json", new MdApiEndpointReserve());
        server.createContext("/api/sync/check.json", new MdApiEndpointCheck());
        server.createContext("/api/maintenance/reserve.json", new MdApiEndpointReserve());
        server.createContext("/api/maintenance/check.json", new MdApiEndpointCheck());

        // html
        server.createContext("/favicon.ico", new MdHtmlEndpointAsset());
        server.createContext("/asset/", new MdHtmlEndpointAsset());
        server.createContext("/diffConfig/", new MdHtmlEndpointDiffConfig());
        server.createContext("/syncConfig/", new MdHtmlEndpointSyncConfig());
        server.createContext("/preset/", new MdHtmlEndpointPreset());
        server.createContext("/project/", new MdHtmlEndpointProject());
        server.createContext("/json/", new MdHtmlEndpointJson());
        server.createContext("/reserve/", new MdHtmlEndpointReserve());
        server.createContext("/check/", new MdHtmlEndpointCheck());
        server.createContext("/", new MdHtmlEndpointIndex());
        // -----

        server.start();
        MdLogger.trace(String.format("listening on %s:%s", host, port));
      } catch (IOException e) {
        MdLogger.error(e);
      }
    });
  }

  /**
   * init dd.
   *
   * @param con connection
   * @throws SQLException sql error
   */
  private void initDdl(MdSqliteConnection con) throws SQLException {
    String sql;

    // -----
    // execution
    // create table execution
    sql = """
        create table if not exists execution (
          executionId text,
          mode text,
          state text,
          input text,
          output text,
          created integer,
          primary key(executionId)
        )""";
    con.execute(sql);

    // create index on execution
    sql = "create index if not exists execution_idx01 on execution (created)";
    con.execute(sql);

    // -----
    // diffConfig
    // create table diffConfig
    sql = """
        create table if not exists diffConfig (
            diffConfigId integer primary key autoincrement,
            title text,
            explanation text,
            option text,
            conditions text,
            relations text
        )""";
    con.execute(sql);

    // -----
    // syncConfig
    // create table syncConfig
    sql = """
        create table if not exists syncConfig (
            syncConfigId integer primary key autoincrement,
            title text,
            explanation text,
            commandsBeforeCommit text,
            commandsAfterCommit text,
            httpCallbackBeforeCommit text,
            httpCallbackAfterCommit text
        )""";
    con.execute(sql);

    // -----
    // preset
    // create table preset
    sql = """
        create table if not exists preset (
            presetId integer primary key autoincrement,
            title text,
            explanation text,
            diffConfigId integer,
            syncConfigId integer
        )""";
    con.execute(sql);

    // create index on preset
    sql = "create index if not exists preset_idx01 on preset (diffConfigId)";
    con.execute(sql);
    sql = "create index if not exists preset_idx02 on preset (syncConfigId)";
    con.execute(sql);

    // -----
    // project
    // create table project
    sql = """
        create table if not exists project (
            projectId integer primary key autoincrement,
            title text,
            explanation text,
            host text,
            port integer,
            user text,
            pass text,
            charset text,
            dbType text,
            baseDatabase text,
            compareDatabase text
        )""";
    con.execute(sql);

    // -----
    // projectPreset
    // create table projectPreset
    sql = """
        create table if not exists projectPreset (
            projectId integer,
            presetId integer,
            no integer,
            primary key (projectId, presetId)
        )""";
    con.execute(sql);

    // create index on projectPreset
    sql = "create index if not exists projectPreset_idx01 on projectPreset (presetId)";
    con.execute(sql);
  }

  /**
   * wait for.
   */
  public void waitFor() {
    try {
      while (true) {
        Thread.sleep(1000);
      }
    } catch (InterruptedException e) {
      MdLogger.error(e);
    }
  }

  /**
   * stop message service.
   */
  public void stopMessageService() {
    if (serviceMessageReserved != null) {
      serviceMessageReserved.shutdownNow();
    }
    serviceMessageReserved = null;
    if (serviceMessageProcessing != null) {
      serviceMessageProcessing.shutdownNow();
    }
    serviceMessageProcessing = null;
  }

  /**
   * shutdown.
   */
  public void shutdown() {
    if (serviceMessageReserved != null) {
      serviceMessageReserved.shutdownNow();
    }
    serviceMessageReserved = null;
    if (serviceMessageProcessing != null) {
      serviceMessageProcessing.shutdownNow();
    }
    serviceMessageProcessing = null;

    if (server != null) {
      server.stop(5);
    }
    server = null;

    if (serviceServer != null) {
      serviceServer.shutdownNow();
    }
    serviceServer = null;
  }

  @Override
  public void close() throws Exception {
    shutdown();
  }
}
