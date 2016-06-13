package com.developpez.lmauzaize.java.concurrence.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CyclicBarrierAction {
  public static void main(String[] args) throws InterruptedException,BrokenBarrierException {
//////////////////////////////////////////
int nbThread = 4;
final CyclicBarrier barrière = new CyclicBarrier(nbThread, () -> Logger.println("Action"));
Runnable tache = new Runnable() {
  public void run() {
    try {
      Logger.println("Attente");
      barrière.await();
      Logger.println("Fin");
    } catch (Exception e) {
      Logger.printStackTrace(e);
    }
  }
};
for (int i = 0; i < nbThread; i++) {
  new Thread(tache).start();
  Thread.sleep(500);
}
//////////////////////////////////////////
  }
}
