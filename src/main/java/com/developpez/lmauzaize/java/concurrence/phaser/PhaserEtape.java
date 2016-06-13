package com.developpez.lmauzaize.java.concurrence.phaser;

import java.util.concurrent.Phaser;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class PhaserEtape {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final String[] parties = { "Pif !", "Paf !", "Pouf !" };
final Phaser phaser = new Phaser();
class Tache extends Thread {
  private int étape;
  public Tache(int étape) {
    super(parties[étape]);
    this.étape = étape;
  }

  public void run() {
    try {
      for (int i = 0; i < étape; i++) {
        phaser.arriveAndAwaitAdvance();
      }
      Thread.sleep(1_000);
      Logger.println("[%d] %s", phaser.arriveAndAwaitAdvance(), getName());
      for (int i = étape; i < parties.length; i++) {
        phaser.arriveAndAwaitAdvance();
      }
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}
phaser.bulkRegister(parties.length);
for (int i = parties.length-1; i >= 0; i--) {
  new Tache(i).start();
  Thread.sleep(200);
}
//////////////////////////////////////////
  }
}
