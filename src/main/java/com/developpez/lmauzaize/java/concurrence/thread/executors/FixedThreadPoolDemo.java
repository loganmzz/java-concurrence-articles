package com.developpez.lmauzaize.java.concurrence.thread.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;
import com.developpez.lmauzaize.java.concurrence.thread.ThreadFactoryLogger;

public class FixedThreadPoolDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
int parallele = 4;

Logger.println("Lancement du pool");
ExecutorService executor = Executors.newFixedThreadPool(parallele, new ThreadFactoryLogger());
try {
  for (int i = 1; i <= parallele * 2; i++) {
    final String nom = String.format("Tache-%02d", i);
    Logger.println("Soumission de la tâche %s", nom);
    final int nb = i;
    Runnable tache = new Runnable() {
      public void run() {
        try {
          int pause = ThreadLocalRandom.current().nextInt(2_000) + 1_500 + (parallele*2-nb)*500;
          Logger.println("[%s] Pause %dms", nom, pause);
          TimeUnit.MILLISECONDS.sleep(pause);
        } catch (InterruptedException e) {
          Logger.printStackTrace(e);
        } finally {
          Logger.println("[%s] Fin", nom);
        }
      }
    };
    executor.submit(tache);
    TimeUnit.MILLISECONDS.sleep(500);
  }
  TimeUnit.SECONDS.sleep(12);
} finally {
  executor.shutdown();
}
//////////////////////////////////////////
  }
}
