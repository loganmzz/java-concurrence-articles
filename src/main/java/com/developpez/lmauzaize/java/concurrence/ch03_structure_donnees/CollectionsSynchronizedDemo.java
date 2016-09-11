package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CollectionsSynchronizedDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
class Tache implements Runnable {
  int nbThreads;
  int nbPerThread;
  List<String> values;

  Tache(int nbThreads, int nbPerThread) {
    this.nbThreads   = nbThreads;
    this.nbPerThread = nbPerThread;
  }

  public void run() {
    for (int i = 0; i < nbPerThread; i++) {
      values.add(Thread.currentThread().getName());
    }
  }

  public void execute(boolean sync) throws InterruptedException {
    values = new ArrayList<>(1);
    if (sync) {
      values = Collections.synchronizedList(values);
    }
    ExecutorService executor = Executors.newCachedThreadPool();
    for (int i = 0; i < nbThreads; i++) {
      executor.submit(this);
    }
    executor.shutdown();
    if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
      throw new CancellationException();
    }
    Logger.println("Synchronisé=%-5b  Attendu=%,06d, Réel=%,06d", sync, nbThreads*nbPerThread, values.size());
  }
};
Tache tache = new Tache(20_000, 10);
tache.execute(false);
tache.execute(true);
//////////////////////////////////////////
  }
}
