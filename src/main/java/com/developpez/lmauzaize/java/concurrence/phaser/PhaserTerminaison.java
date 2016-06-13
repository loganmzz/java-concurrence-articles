package com.developpez.lmauzaize.java.concurrence.phaser;

import java.util.Arrays;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.Phaser;
import java.util.stream.IntStream;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class PhaserTerminaison {

  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final Phaser phaser = new Phaser();
final OfInt séquence = IntStream.iterate(0, i -> i+1).iterator();
class Standard extends Thread {
  int numéro = séquence.nextInt();
  int sleep = numéro * 200;
  {
    setName(getClass().getSimpleName() + "-" + numéro);
    phaser.register();
  }

  void pause() throws InterruptedException {
    Logger.println("Pause % 4dms", sleep);
    sleep(sleep);
  }

  void arrive() throws InterruptedException {
    pause();
    int étape = phaser.arrive();
    Logger.println("Étape %02d terminée", étape);
    étape = phaser.awaitAdvance(étape);
    Logger.println("Étape %02d atteinte", étape);
  }

  public void run() {
    try {
      arrive();
      arrive();
      Logger.println("Fin");
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}
class Erreur extends Standard {
  public void run() {
    try {
      arrive();
      pause();
      Logger.println("Erreur rencontrée");
      phaser.forceTermination();
      Logger.println("Fin");
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
};
for (Standard tâche : Arrays.asList(new Standard(), new Erreur(), new Standard())) {
  tâche.start();
}
//////////////////////////////////////////
  }
}
