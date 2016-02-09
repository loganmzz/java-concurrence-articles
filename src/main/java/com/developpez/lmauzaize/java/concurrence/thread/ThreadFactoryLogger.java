package com.developpez.lmauzaize.java.concurrence.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ThreadFactoryLogger implements ThreadFactory {
  private ThreadFactory threadFactory = Executors.defaultThreadFactory();

  protected void début() {
    Logger.println("Début");
  }
  protected void fin() {
    Logger.println("Fin");
  }

  @Override
  public Thread newThread(final Runnable r) {
    class Enveloppe implements Runnable {
      @Override
      public void run() {
        try {
          début();
          r.run();
        } finally {
          fin();
        }
      }
    }
    return threadFactory.newThread(new Enveloppe());
  }
}
