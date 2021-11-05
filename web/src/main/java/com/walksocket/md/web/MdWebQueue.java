package com.walksocket.md.web;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * web queue.
 */
public class MdWebQueue {

  /**
   * queue.
   */
  private ConcurrentLinkedQueue<MdWebQueueMessage> queue = new ConcurrentLinkedQueue<>();

  /**
   * add.
   * @param message message
   */
  public void add(MdWebQueueMessage message) {
    queue.add(message);
  }

  /**
   * poll.
   * @return message
   */
  public MdWebQueueMessage poll() {
    return queue.poll();
  }
}
