package com.walksocket.md.cli;

import com.sun.net.httpserver.HttpServer;
import com.walksocket.md.cli.exception.MdExceptionErrorLocalDatabase;
import com.walksocket.md.cli.sqlite.MdSqliteConnection;
import com.walksocket.md.cli.web.MdWebQueue;
import com.walksocket.md.cli.web.endpoint.MdWebEndpointApiCheck;
import com.walksocket.md.cli.web.endpoint.MdWebEndpointApiReserve;
import com.walksocket.md.cli.web.service.MdWebServiceProcessing;
import com.walksocket.md.cli.web.service.MdWebServiceReserved;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * web.
 */
public class MdWeb implements AutoCloseable {

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
  public MdWeb(String host, int port) {
    this.host = host;
    this.port = port;
  }

  /**
   * start.
   * @throws Exception several error
   */
  public void start() throws Exception {
    try (MdSqliteConnection con = new MdSqliteConnection()) {
      String sql = "";

      // begin
      con.begin();

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

      // create index on execution.created
      sql = "create index if not exists execution_idx01 on execution (created)";
      con.execute(sql);

      // commit
      con.commit();
    } catch (Exception e) {
      MdLogger.error(e);
      throw new MdExceptionErrorLocalDatabase();
    }

    // create queue
    MdWebQueue queue = new MdWebQueue();

    // init reserved message service
    serviceMessageReserved = Executors.newSingleThreadScheduledExecutor();
    serviceMessageReserved.scheduleAtFixedRate(
        new MdWebServiceReserved(queue),
        0,
        5,
        TimeUnit.SECONDS);

    // init processing message service
    serviceMessageProcessing = Executors.newSingleThreadScheduledExecutor();
    serviceMessageProcessing.scheduleAtFixedRate(
        new MdWebServiceProcessing(queue),
        0,
        5,
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
          server.createContext("/api/diff/reserve.json", new MdWebEndpointApiReserve());
          server.createContext("/api/diff/check.json", new MdWebEndpointApiCheck());
          server.createContext("/api/sync/reserve.json", new MdWebEndpointApiReserve());
          server.createContext("/api/sync/check.json", new MdWebEndpointApiCheck());
          server.createContext("/api/maintenance/reserve.json", new MdWebEndpointApiReserve());
          server.createContext("/api/maintenance/check.json", new MdWebEndpointApiCheck());
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
   * wait for.
   */
  public void waitFor() {
    try {
      serviceServer.wait();
    } catch (InterruptedException e) {
      MdLogger.error(e);
    }
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
