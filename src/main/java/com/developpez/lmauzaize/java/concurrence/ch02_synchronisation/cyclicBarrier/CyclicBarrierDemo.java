package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CyclicBarrierDemo {
  public static void main(String[] args) throws InterruptedException,BrokenBarrierException {
//////////////////////////////////////////
final int nbThread = 5;
final CyclicBarrier barrière = new CyclicBarrier(nbThread + 1);
Runnable tache = new Runnable() {
  public void run() {
    Logger.println("Début");
    int index;
    try {
      index = nbThread - barrière.await();
    } catch (InterruptedException | BrokenBarrierException e) {
      index = -1;
      Logger.printStackTrace(e);
    }
    Logger.println("Fin (%d)", index);
  }
};
for (int i = 0; i < nbThread; i++) {
  new Thread(tache).start();
  Thread.sleep(500);
}
barrière.await();
//////////////////////////////////////////
  }
}
