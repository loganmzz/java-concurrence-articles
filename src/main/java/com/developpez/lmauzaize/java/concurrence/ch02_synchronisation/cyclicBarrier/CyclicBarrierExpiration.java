package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CyclicBarrierExpiration {
  public static void main(String[] args) throws InterruptedException,BrokenBarrierException {
//////////////////////////////////////////
final CyclicBarrier barrière = new CyclicBarrier(10);
class Tache extends Thread {
  private long attente;
  Tache(String nom, long attente) {
    super(nom);
    this.attente = attente;
  }

  public void run() {
    try {
      barrière.await(attente, TimeUnit.MILLISECONDS);
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
new Tache("Patient #1", 60_000).start();
new Tache("Patient #2", 60_000).start();
new Tache("Patient #3", 60_000).start();
new Tache("Expiration",  2_000).start();
Thread.sleep(3_000);
Logger.println("Est cassée=%s", barrière.isBroken());
//////////////////////////////////////////
  }
}
