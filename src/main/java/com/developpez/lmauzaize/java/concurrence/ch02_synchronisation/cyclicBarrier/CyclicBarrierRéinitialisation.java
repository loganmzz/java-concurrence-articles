package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CyclicBarrierRéinitialisation {
  public static void main(String[] args) throws InterruptedException,BrokenBarrierException {
//////////////////////////////////////////
int nbThread = 4;
final CyclicBarrier barrière = new CyclicBarrier(nbThread+1);
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
for (int i = 0; i < nbThread; i++) new Thread(tache).start();
Thread.sleep(2_000);
barrière.reset();
Logger.println("Est cassée=%s", barrière.isBroken());
barrière.await();
//////////////////////////////////////////
  }
}
