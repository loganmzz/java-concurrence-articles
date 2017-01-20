package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

import com.developpez.lmauzaize.java.concurrence.Logger;



public class StampedLockTryInterruption {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
//////////////////////////////////////////
final StampedLock verrou  = new StampedLock();
Runnable tache = new Runnable() {
  public void run() {
    long tampon = 0;
    try {
      tampon = verrou.tryWriteLock(1, TimeUnit.MINUTES);
      Logger.println("Vérrouillage (%d)", tampon);
    } catch (InterruptedException e) {
      Logger.println("Interrompu");
    } finally {
      Logger.println("Déverrouillage (%d)", tampon);
      verrou.tryConvertToOptimisticRead(tampon);
    }
  }
};

verrou.writeLock();
Thread thread = new Thread(tache);
thread.setDaemon(true);

Logger.println("Démarrage du thread");
thread.start();

Thread.sleep(200L);
Logger.println("Interruption");
thread.interrupt();

Thread.sleep(200L);
Logger.println("Thread state=%s", thread.getState());
//////////////////////////////////////////
  }
}

