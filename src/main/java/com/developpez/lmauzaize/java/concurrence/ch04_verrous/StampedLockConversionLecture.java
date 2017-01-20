package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.concurrent.locks.StampedLock;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class StampedLockConversionLecture {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
StampedLock verrou = new StampedLock();

long optimiste = verrou.tryOptimisticRead();
Logger.println("Lecture optimiste (%s)", optimiste);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
long lecture1 = verrou.tryConvertToReadLock(optimiste);
Logger.println("Conversion en lecture de (%s) en (%s)", optimiste, lecture1);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");

Logger.println("");

long lecture2 = verrou.tryReadLock();
Logger.println("Lecture (%s)", lecture2);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
Logger.println("Conversion en lecture de (%s) en (%s)", lecture2, verrou.tryConvertToReadLock(lecture2));
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");

Logger.println("");

Logger.println("Libération lecture (%s, %s)", lecture1, lecture2);
verrou.unlockRead(lecture1);
verrou.unlockRead(lecture2);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
long ecriture = verrou.tryWriteLock();
Logger.println("Écriture (%s)", ecriture);
Logger.println("Conversion en lecture de (%s) en (%s)", ecriture, verrou.tryConvertToReadLock(ecriture));
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
//////////////////////////////////////////
  }
}

