package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.concurrent.locks.StampedLock;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class StampedLockLectureOptimiste {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
StampedLock verrou = new StampedLock();

long optimiste = verrou.tryOptimisticRead();
Logger.println("Essai de lecture optimiste (%s)", optimiste);
Logger.println("Validité (%s) ? %s", optimiste, verrou.validate(optimiste));

Logger.println("");

long lecture   = verrou.tryReadLock();
Logger.println("Essai de lecture (%s)", lecture);
Logger.println("Validité (%s) ? %s", optimiste, verrou.validate(optimiste));
Logger.println("Validité (%s) ? %s", lecture  , verrou.validate(lecture));

Logger.println("");

Logger.println("Essai d'écriture (%s)", verrou.tryWriteLock());
Logger.println("Validité (%s) ? %s"   , optimiste, verrou.validate(optimiste));
Logger.println("Validité (%s) ? %s"   , lecture  , verrou.validate(lecture));

Logger.println("");

Logger.println("Déverrouillage en lecture de (%s)", lecture);
verrou.unlockRead(lecture);
Logger.println("Validité (%s) ? %s"   , optimiste, verrou.validate(optimiste));
Logger.println("Validité (%s) ? %s"   , lecture  , verrou.validate(lecture));

Logger.println("");

long ecriture  = verrou.tryWriteLock();
Logger.println("Essai d'écriture (%s)", ecriture);
Logger.println("Validité (%s) ? %s"   , optimiste, verrou.validate(optimiste));
Logger.println("Validité (%s) ? %s"   , lecture  , verrou.validate(lecture));

Logger.println("");

Logger.println("Essai de lecture optimiste (%s)", verrou.tryOptimisticRead());

Logger.println("");

Logger.println("Déverrouillage en écriture de (%s)", ecriture);
verrou.unlockWrite(ecriture);
Logger.println("Validité (%s) ? %s"   , optimiste, verrou.validate(optimiste));
Logger.println("Validité (%s) ? %s"   , lecture  , verrou.validate(lecture));

Logger.println("");

optimiste = verrou.tryOptimisticRead();
Logger.println("Essai de lecture optimiste (%s)", optimiste);
Logger.println("Validité (%s) ? %s", optimiste, verrou.validate(optimiste));
Logger.println("Validité (%s) ? %s", lecture  , verrou.validate(lecture));
//////////////////////////////////////////
  }
}

