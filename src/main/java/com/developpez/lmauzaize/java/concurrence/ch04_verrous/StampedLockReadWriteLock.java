package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.StampedLock;

import com.developpez.lmauzaize.java.concurrence.Logger;



public class StampedLockReadWriteLock {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
//////////////////////////////////////////
class CompteBancaire {
  StampedLock verrou = new StampedLock();
  long solde = 0;

  void modifier(long montant) {
    long tampon = verrou.writeLock();
    try {
      solde += montant;
    } finally {
      verrou.unlockWrite(tampon);
    }
  }

  long consulter() {
    long tampon = verrou.readLock();
    try {
      return solde;
    } finally {
      verrou.unlockRead(tampon);
    }
  }
}
final CompteBancaire compte = new CompteBancaire();


Runnable curieux = () -> {
  while (true) {
    compte.consulter();
    Thread.yield();
  }
};
for (int i = 0; i < 2; i++) {
  Thread thread = new Thread(curieux, "Curieux-" + i);
  thread.setDaemon(true);
  thread.start();
}


Callable<long[]> operateur = () -> {
  long[] montants = new Random().longs(-300, 700).limit(256).toArray();
  for (long montant : montants) {
    compte.modifier(montant);
    Thread.yield();
  }
  return montants;
};
ExecutorService executeur = Executors.newCachedThreadPool();
try {
  List<Future<long[]>> resultats = executeur.invokeAll(Collections.nCopies(4, operateur));

  long solde = 0;
  for (Future<long[]> resultat : resultats) {
    for (long montant : resultat.get()) {
      solde += montant;
    }
  }
  Logger.println("Solde réel=%d, attendu=%d", compte.consulter(), solde);
} finally {
  executeur.shutdown();
}
//////////////////////////////////////////
  }
}

