package com.developpez.lmauzaize.java.concurrence.countDownLatch;

import java.util.concurrent.CountDownLatch;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CountDownLatchAttenteSousTaches {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
long[] pauses = { 4_000, 5_000, 2_000, 1_000 };
final CountDownLatch latch = new CountDownLatch(pauses.length);
class Tache extends Thread {
  long pause;
  Tache(long pause) {
    this.pause = pause;
  }
  public void run() {
    Logger.println("Pause %04dms", pause);
    try {
      Thread.sleep(pause);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
    Logger.println("Fin");
    latch.countDown();
  }
};

for (long pause : pauses) {
  new Tache(pause).start();
}
latch.await();
Logger.println("Fin du processus");
//////////////////////////////////////////
  }
}
