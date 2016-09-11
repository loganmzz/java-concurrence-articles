package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class CollectionsSynchronizedInstanceVsBloc {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
class Tache implements Runnable {
  int nbThreads;
  int nbPerThread;
  List<Integer> values;
  boolean sync;

  Tache(int nbThreads, int nbPerThread) {
    this.nbThreads   = nbThreads;
    this.nbPerThread = nbPerThread;
  }

  public void run() {
    for (int i = 0; i < nbPerThread; i++) {
      if (sync) {
        syncAdd();
      } else {
        add();
      }
    }
  }

  void add() {
    values.add(values.size());
  }
  void syncAdd() {
    synchronized (values) {
      add();
    }
  }
  void newList() {
    values = new ArrayList<>(1);
    if (!sync) {
      values = Collections.synchronizedList(values);
    }
  }
  int check() {
    int check = values.size();
    for (int i = 0; i < values.size(); i++) {
      if (i != values.get(i)) {
        check--;
      }
    }
    return check;
  }

  Tache execute(boolean sync) throws InterruptedException {
    this.sync = sync;
    newList();
    ExecutorService executor = Executors.newCachedThreadPool();
    for (int i = 0; i < nbThreads; i++) {
      executor.submit(this);
    }
    executor.shutdown();
    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
      throw new CancellationException();
    }
    Logger.println("Synchronisé=%-5b  Attendu=%,06d, Réel=%,06d/%,06d", sync, nbThreads*nbPerThread, check(), values.size());
    return this;
  }
};
new Tache(20_000, 10).execute(false).execute(true);
//////////////////////////////////////////
  }
}
