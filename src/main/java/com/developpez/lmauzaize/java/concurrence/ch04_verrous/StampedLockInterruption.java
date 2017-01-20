package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.StampedLock;

import com.developpez.lmauzaize.java.concurrence.Logger;



public class StampedLockInterruption {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
//////////////////////////////////////////
final StampedLock verrou  = new StampedLock();
long tampon = verrou.writeLock();
Thread thread = new Thread() {
  public void run() {
    long tampon = verrou.writeLock();
    try {
      Logger.println("Vérrouillage (%d)", tampon);
      Logger.println("Interrompu? %b", isInterrupted());
    } finally {
      verrou.unlockWrite(tampon);
    }
  }
};
thread.setDaemon(true);
Logger.println("Démarrage du thread");
thread.start();

Thread.sleep(200L);
Logger.println("Interruption");
thread.interrupt();

Thread.sleep(200L);
Logger.println("Déverrouillage");
verrou.unlock(tampon);

Thread.sleep(200L);
Logger.println("Thread state=%s", thread.getState());
//////////////////////////////////////////
  }
}

