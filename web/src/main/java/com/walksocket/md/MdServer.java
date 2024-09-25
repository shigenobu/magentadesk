package com.walksocket.md;

import com.sun.net.httpserver.HttpServer;
import com.walksocket.md.exception.MdExceptionErrorLocalDatabase;
import com.walksocket.md.html.endpoint.*;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.api.MdApiQueue;
import com.walksocket.md.api.endpoint.MdApiEndpointCheck;
import com.walksocket.md.api.endpoint.MdApiEndpointReserve;
import com.walksocket.md.service.MdServiceProcessing;
import com.walksocket.md.service.MdServiceReserved;

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
  private String host;

  /**
   * port.
   */
  private int port;

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
   * @param host host
   * @param port port
   */
  public MdServer(String host, int port) {
    this.host = host;
    this.port = port;
  }

  /**
   * start.
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
    serviceServer.submit(new Runnable() {
      @Override
      public void run() {
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
      }
    });
  }

  /**
   * init dd.
   * @param con connection
   * @throws SQLException sql error
   */
  private void initDdl(MdSqliteConnection con) throws SQLException {
    String sql = "";

    // -----
    // execution
    // create table execution
    sql = "create table if not exists execution (\n" +
        "  executionId text,\n" +
        "  mode text,\n" +
        "  state text,\n" +
        "  input text,\n" +
        "  output text,\n" +
        "  created integer,\n" +
        "  primary key(executionId)\n" +
        ")";
    con.execute(sql);

    // create index on execution
    sql = "create index if not exists execution_idx01 on execution (created)";
    con.execute(sql);

    // -----
    // diffConfig
    // create table diffConfig
    sql = "create table if not exists diffConfig (\n" +
        "    diffConfigId integer primary key autoincrement,\n" +
        "    title text,\n" +
        "    explanation text,\n" +
        "    option text,\n" +
        "    conditions text,\n" +
        "    relations text\n" +
        ")";
    con.execute(sql);

    // -----
    // syncConfig
    // create table syncConfig
    sql = "create table if not exists syncConfig (\n" +
        "    syncConfigId integer primary key autoincrement,\n" +
        "    title text,\n" +
        "    explanation text,\n" +
        "    commandsBeforeCommit text,\n" +
        "    commandsAfterCommit text,\n" +
        "    httpCallbackBeforeCommit text,\n" +
        "    httpCallbackAfterCommit text\n" +
        ")";
    con.execute(sql);

    // -----
    // preset
    // create table preset
    sql = "create table if not exists preset (\n" +
        "    presetId integer primary key autoincrement,\n" +
        "    title text,\n" +
        "    explanation text,\n" +
        "    diffConfigId integer,\n" +
        "    syncConfigId integer\n" +
        ")";
    con.execute(sql);

    // create index on preset
    sql = "create index if not exists preset_idx01 on preset (diffConfigId)";
    con.execute(sql);
    sql = "create index if not exists preset_idx02 on preset (syncConfigId)";
    con.execute(sql);

    // -----
    // project
    // create table project
    sql = "create table if not exists project (\n" +
        "    projectId integer primary key autoincrement,\n" +
        "    title text,\n" +
        "    explanation text,\n" +
        "    host text,\n" +
        "    port integer,\n" +
        "    user text,\n" +
        "    pass text,\n" +
        "    charset text,\n" +
        "    dbType text,\n" +
        "    baseDatabase text,\n" +
        "    compareDatabase text\n" +
        ")";
    con.execute(sql);

    // -----
    // projectPreset
    // create table projectPreset
    sql = "create table if not exists projectPreset (\n" +
        "    projectId integer,\n" +
        "    presetId integer,\n" +
        "    no integer,\n" +
        "    primary key (projectId, presetId)\n" +
        ")";
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
