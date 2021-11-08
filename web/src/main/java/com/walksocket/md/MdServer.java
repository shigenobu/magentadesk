package com.walksocket.md;

import com.sun.net.httpserver.HttpServer;
import com.walksocket.md.exception.MdExceptionErrorLocalDatabase;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.api.MdApiQueue;
import com.walksocket.md.api.endpoint.MdApiEndpointCheck;
import com.walksocket.md.api.endpoint.MdApiEndpointReserve;
import com.walksocket.md.api.service.MdApiServiceProcessing;
import com.walksocket.md.api.service.MdApiServiceReserved;

import java.io.IOException;
import java.net.InetSocketAddress;
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
    MdApiQueue queue = new MdApiQueue();

    // init reserved message service
    serviceMessageReserved = Executors.newSingleThreadScheduledExecutor();
    serviceMessageReserved.scheduleAtFixedRate(
        new MdApiServiceReserved(queue),
        0,
        5,
        TimeUnit.SECONDS);

    // init processing message service
    serviceMessageProcessing = Executors.newSingleThreadScheduledExecutor();
    serviceMessageProcessing.scheduleAtFixedRate(
        new MdApiServiceProcessing(queue),
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
          server.createContext("/api/diff/reserve.json", new MdApiEndpointReserve());
          server.createContext("/api/diff/check.json", new MdApiEndpointCheck());
          server.createContext("/api/sync/reserve.json", new MdApiEndpointReserve());
          server.createContext("/api/sync/check.json", new MdApiEndpointCheck());
          server.createContext("/api/maintenance/reserve.json", new MdApiEndpointReserve());
          server.createContext("/api/maintenance/check.json", new MdApiEndpointCheck());
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
      while (true) {
        Thread.sleep(1000);
      }
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
