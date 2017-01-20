package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.concurrent.locks.StampedLock;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class StampedLockConversionEcriture {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
StampedLock verrou = new StampedLock();

long optimiste = verrou.tryOptimisticRead();
Logger.println("Lecture optimiste (%s)", optimiste);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
long ecriture = verrou.tryConvertToWriteLock(optimiste);
Logger.println("Conversion en écriture de (%s) en (%s)", optimiste, ecriture);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
Logger.println("Libération écriture (%s)", ecriture);
verrou.unlockWrite(ecriture);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");

Logger.println("");

long lecture1 = verrou.tryReadLock();
Logger.println("Lecture (%s)", lecture1);
long lecture2 = verrou.tryReadLock();
Logger.println("Lecture (%s)", lecture2);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
ecriture = verrou.tryConvertToWriteLock(lecture1);
Logger.println("Conversion en écriture de (%s) en (%s)", lecture1, ecriture);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");

Logger.println("");

Logger.println("Libération écriture (%s)", lecture2);
verrou.unlockRead(lecture2);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
ecriture = verrou.tryConvertToWriteLock(lecture1);
Logger.println("Conversion en écriture de (%s) en (%s)", lecture1, ecriture);
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");

Logger.println("");

Logger.println("Conversion en écriture de (%s) en (%s)", ecriture, verrou.tryConvertToWriteLock(ecriture));
Logger.println("Verrous lecture(%s) écriture(%s)", verrou.getReadLockCount(), verrou.isWriteLocked() ? "1" : "0");
//////////////////////////////////////////
  }
}

