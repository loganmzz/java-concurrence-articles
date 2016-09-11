package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.countDownLatch;

import java.util.concurrent.CountDownLatch;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CountDownLatchEtape {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
// Signaux
final CountDownLatch pif  = new CountDownLatch(1);
final CountDownLatch paf  = new CountDownLatch(1);
final CountDownLatch pouf = new CountDownLatch(1);


class Tache extends Thread {
  public Tache(String nom) {
    super(nom);
  }

  // Signaux précédents
  CountDownLatch[] avant;
  Tache avant(CountDownLatch... avant) {
    this.avant = avant;
    return this;
  }
  // Signal à envoyer
  CountDownLatch signal;
  Tache signal(CountDownLatch signal) {
    this.signal = signal;
    return this;
  }
  // Signaux suivants
  CountDownLatch[] après;
  Tache après(CountDownLatch... après) {
    this.après = après;
    return this;
  }

  public void run() {
    try {
      // avant
      for (CountDownLatch loquet : avant) {
        loquet.await();
      }
      // signal
      Thread.sleep(1_000);
      Logger.println(getName());
      signal.countDown();
      // après
      for (CountDownLatch loquet : après) {
        loquet.await();
      }
      Logger.println("Fin");
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    } finally {
      signal.countDown();
    }
  }
}
new Tache("Pif  !").avant(        ).signal(pif ).après(paf, pouf).start();
new Tache("Paf  !").avant(pif     ).signal(paf ).après(pouf     ).start();
new Tache("Pouf !").avant(pif, paf).signal(pouf).après(         ).start();
//////////////////////////////////////////
  }
}
