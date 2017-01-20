package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.concurrent.locks.StampedLock;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class StampedLockConversionOptimiste {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
StampedLock verrou = new StampedLock();

long optimiste = verrou.tryOptimisticRead();
Logger.println("Lecture optimiste (%s)", optimiste);
Logger.println("Conversion en lecture optimiste de (%s) en (%s)", optimiste, verrou.tryConvertToOptimisticRead(optimiste));

Logger.println("");

long lecture = verrou.tryReadLock();
Logger.println("Lecture (%s)", lecture);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
Logger.println("Validité (%s) ? %s", optimiste, verrou.validate(optimiste));
Logger.println("Conversion en lecture optimiste de (%s) en (%s)", optimiste, verrou.tryConvertToOptimisticRead(optimiste));
optimiste = verrou.tryConvertToOptimisticRead(lecture);
Logger.println("Conversion en lecture optimiste de (%s) en (%s)", lecture, optimiste);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");

Logger.println("");

long ecriture = verrou.tryWriteLock();
Logger.println("Écriture (%s)", ecriture);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
Logger.println("Validité (%s) ? %s", optimiste, verrou.validate(optimiste));
Logger.println("Conversion en lecture optimiste de (%s) en (%s)", optimiste, verrou.tryConvertToOptimisticRead(optimiste));
optimiste = verrou.tryConvertToOptimisticRead(ecriture);
Logger.println("Conversion en lecture optimiste de (%s) en (%s)", ecriture, optimiste);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
//////////////////////////////////////////
  }
}

