package com.developpez.lmauzaize.java.concurrence.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CyclicBarrierInterruption {
  public static void main(String[] args) throws InterruptedException,BrokenBarrierException {
//////////////////////////////////////////
final CyclicBarrier barrière = new CyclicBarrier(10);
Runnable tache = new Runnable() {
  public void run() {
    try {
      barrière.await();
    } catch (Exception e) {
      Logger.println("1. %s", e);
    }
    Logger.println("Est cassée=%s", barrière.isBroken());
    try {
      barrière.await();
    } catch (Exception e) {
      Logger.println("2. %s", e);
    }
  }
};
Thread thread = new Thread(tache, "Interruption");
new Thread(tache).start();
new Thread(tache).start();
new Thread(tache).start();
thread.start();
Thread.sleep(2_000);
thread.interrupt();
Thread.sleep(1_000);
Logger.println("Est cassée=%s", barrière.isBroken());
//////////////////////////////////////////
  }
}
