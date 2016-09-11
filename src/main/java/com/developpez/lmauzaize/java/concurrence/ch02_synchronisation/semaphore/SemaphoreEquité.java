package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class SemaphoreEquité {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final boolean équité    = false;
final int     size      = 2_250;
final int     iteration = 200;

final Semaphore semaphore = new Semaphore(1, équité);
final List<Thread> actuals   = new ArrayList<>(size);
final List<Thread> expecteds = new ArrayList<>(size);
final Runnable tache = new Runnable() {
  public void run() {
    try {
      semaphore.acquire();
      actuals.add(Thread.currentThread());
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    } finally {
      semaphore.release();
    }
  }
};

int erreur = 0;
int j = 0;
for (; j < iteration; j++) {

  // Créé de nouveaux threads et vérifie qu'il soit mis en attente avant de passer à la suite
  semaphore.acquire();
  for (int i = 0; i < size; i++) {
    Thread thread = new Thread(tache);
    thread.start();
    expecteds.add(thread);
    while (expecteds.size() != semaphore.getQueueLength()) {
      Thread.yield();
    }
  }

  // Débloque les threads et attend qu'ils soient tous terminés.
  semaphore.release();
  for (Thread thread : expecteds) {
    thread.join();
  }

  // Vérifie les différences d'ordre
  int difference = 0;
  for (int i = 0; i < size; i++) {
    Thread expected = expecteds.get(i);
    Thread actual   = actuals.get(i);
    if (expected != actual) {
      difference++;
      Logger.println("Expected: %-15s Actual: %-15s", expected.getName(), actual.getName());
    }
  }
  actuals.clear();
  expecteds.clear();
  Logger.println("Itération %03d : %03d erreur(s)", j, difference);
  erreur += difference;
}
Logger.println("%03d erreur(s) en %03d itérations", erreur, iteration);
//////////////////////////////////////////
  }
}
