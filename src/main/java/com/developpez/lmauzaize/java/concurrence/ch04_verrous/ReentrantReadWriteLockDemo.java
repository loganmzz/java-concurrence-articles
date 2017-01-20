package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.developpez.lmauzaize.java.concurrence.Logger;




public class ReentrantReadWriteLockDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final boolean parite = false;
class Cache {
  Map<Integer, Entry<String,String>> contenu = new TreeMap<>();
  ReentrantReadWriteLock verrou = new ReentrantReadWriteLock(parite);

  String get(Integer cle) {
    verrou.readLock().lock();
    try {
      Entry<String,String> valeur = contenu.get(cle);
      if (valeur == null) {
        verrou.readLock().unlock();
        valeur = forcerChargement(cle);
      }
      return valeur.getKey();
    } finally {
      if (verrou.getReadHoldCount() > 0) {
        verrou.readLock().unlock();
      }
    }
  }

  private Entry<String,String> forcerChargement(Integer cle) {
    Entry<String,String> valeur;
    try {
      verrou.writeLock().lock();
      valeur = contenu.get(cle);
      if (valeur == null) {
        valeur = new SimpleImmutableEntry<>(cle.toString(), Thread.currentThread().getName());
        contenu.put(cle, valeur);
        Logger.println("Chargement de %d", cle);
      } else {
        Logger.println("Chargement annulé de %d", cle);
      }
    } finally {
      verrou.writeLock().unlock();
    }
    return valeur;
  }

  @Override
  public String toString() {
    verrou.readLock().lock();
    try {
      return contenu.toString();
    } finally {
      verrou.readLock().unlock();
    }
  }
}
final Cache cache = new Cache();
final Callable<Void> tache = new Callable<Void>() {
  public Void call() {
    for (int i = 0; i < 15; i++) {
      cache.get(i);
    }
    return null;
  }
};
ExecutorService executor = Executors.newCachedThreadPool();
try {
  executor.invokeAll(Collections.nCopies(5, tache));
} finally {
  executor.shutdown();
}
Logger.println("%s", cache);
//////////////////////////////////////////
  }
}

