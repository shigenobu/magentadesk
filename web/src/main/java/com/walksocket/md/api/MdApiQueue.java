package com.walksocket.md.api;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * api queue.
 */
public class MdApiQueue {

  /**
   * queue.
   */
  private final ConcurrentLinkedQueue<MdApiQueueMessage> queue = new ConcurrentLinkedQueue<>();

  /**
   * add.
   *
   * @param message message
   */
  public void add(MdApiQueueMessage message) {
    queue.add(message);
  }

  /**
   * poll.
   *
   * @return message
   */
  public MdApiQueueMessage poll() {
    return queue.poll();
  }
}
