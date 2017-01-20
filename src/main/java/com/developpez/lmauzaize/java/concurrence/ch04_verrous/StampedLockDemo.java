package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class StampedLockDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
class Contexte implements UncaughtExceptionHandler {
  StampedLock verrou = new StampedLock();
  long tampon;

  void executer(String nom, Runnable action) {
    Thread thread = new Thread(action, nom);
    thread.setUncaughtExceptionHandler(this);
    thread.setDaemon(true);
    thread.start();
    try {
      TimeUnit.MINUTES.timedJoin(thread, 1);
    } catch (InterruptedException e) {
      CancellationException ex = new CancellationException();
      ex.initCause(e);
      throw ex;
    }
  }
  @Override
  public void uncaughtException(Thread t, Throwable e) {
    Logger.println("Erreur rencontrée sur le thread [%s]", t.getName());
    Logger.printStackTrace(e);
  }
}
Contexte c = new Contexte();

c.executer("readLock", () -> {
  c.tampon = c.verrou.readLock();
  Logger.println("Verrouiller en lecture (%d)", c.tampon);
});

c.executer("invalidUnlock", () -> {
  long tampon = c.tampon-1;
  try {
    c.verrou.unlock(tampon);
  } catch (IllegalMonitorStateException e) {
    Logger.println("Impossible de déverrouiller (%d)", tampon);
  }
});

c.executer("unlockWrite", () -> {
  try {
    c.verrou.unlockWrite(c.tampon);
  } catch  (IllegalMonitorStateException e) {
    Logger.println("Impossible de déverrouiller en écriture (%d)", c.tampon);
  }
});

c.executer("unlock", () -> {
  c.verrou.unlock(c.tampon);
  Logger.println("Déverrouiller (%d)", c.tampon);
});
//////////////////////////////////////////
  }
}

