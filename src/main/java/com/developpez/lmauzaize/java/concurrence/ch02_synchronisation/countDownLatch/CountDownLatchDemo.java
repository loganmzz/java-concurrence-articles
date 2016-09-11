package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.countDownLatch;

import java.util.concurrent.CountDownLatch;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CountDownLatchDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
int nbThread = 5;
final CountDownLatch loquet = new CountDownLatch(nbThread);
Runnable tache = new Runnable() {
  public void run() {
    Logger.println("Début");
    loquet.countDown();
    try {
      loquet.await();
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
    Logger.println("Fin");
  }
};
for (int i = 0; i < nbThread; i++) {
  new Thread(tache).start();
  Thread.sleep(500);
}
//////////////////////////////////////////
  }
}
