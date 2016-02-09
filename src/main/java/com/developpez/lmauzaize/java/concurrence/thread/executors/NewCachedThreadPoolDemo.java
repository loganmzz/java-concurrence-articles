package com.developpez.lmauzaize.java.concurrence.thread.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;
import com.developpez.lmauzaize.java.concurrence.thread.ThreadFactoryLogger;

public class NewCachedThreadPoolDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
int parallele = 4;

ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactoryLogger());
for (int i = 1; i <= parallele; i++) {
  final String nom = String.format("Tache-%02d", i);
  Runnable tache = new Runnable() {
    public void run() {
      try {
        Logger.println("[%s] Début", nom);
        int pause = ThreadLocalRandom.current().nextInt(2_000) + 200;
        Logger.println("[%s] Pause %dms", nom, pause);
        TimeUnit.MILLISECONDS.sleep(pause);
        Logger.println("[%s] Fin", nom);
      } catch (InterruptedException e) {
        Logger.printStackTrace(e);
      }
    }
  };
  executor.submit(tache);
}
//////////////////////////////////////////
  }
}
