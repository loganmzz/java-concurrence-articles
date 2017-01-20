package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.concurrent.locks.StampedLock;

public class StampedLockExample {
  static StampedLock lock = new StampedLock();

  static void println(String message, Object... args) {
    System.out.printf(message, args);
    System.out.println();
  }

  static void printReadLockCount() {
    println("Lock count=%d", lock.getReadLockCount());
  }

  static long tryReadLock() {
    long stamp = lock.tryReadLock();
    println("Gets read lock (%d)", stamp);
    printReadLockCount();
    return stamp;
  }

  static long tryWriteLock() {
    long stamp = lock.tryWriteLock();
    println("Gets write lock (%d)", stamp);
    return stamp;
  }

  static long tryConvertToReadLock(long stamp) {
    long newOne = lock.tryConvertToReadLock(stamp);
    println("Gets read lock (%d -> %d)", stamp, newOne);
    printReadLockCount();
    return newOne;
  }

  static void tryUnlock(long stamp) {
    try {
      lock.unlock(stamp);
      println("Unlock (%d) successfully", stamp);
    } catch (IllegalMonitorStateException e) {
      println("Unlock (%d) failed", stamp);
    }
    printReadLockCount();
  }

  public static void main(String[] args) {
    println("%n--- Gets two read locks ---");
    long stamp1 = tryReadLock();
    long stamp2 = tryReadLock();
    long min = Math.min(stamp1, stamp2);
    long max = Math.max(stamp1, stamp2);

    println("%n--- Tries unlock (-1 / +2 / +4) ---");
    tryUnlock(min - 1);
    tryUnlock(max + 2);
    tryUnlock(max + 4);

    println("%n--- Gets write lock ---");
    long stamp3 = tryWriteLock();

    println("%n--- Tries unlock (-1 / +1) ---");
    tryUnlock(stamp3 - 1);
    tryUnlock(stamp3 + 1);

    println("%n--- Tries write > read conversion ---");
    long stamp4 = tryConvertToReadLock(stamp3);

    println("%n--- Tries unlock last write stamp (-1 / 0 / +1) ---");
    tryUnlock(stamp3-1);
    tryUnlock(stamp3);
    tryUnlock(stamp3+1);

    println("%n--- Tries unlock (-1 / +1) ---");
    tryUnlock(stamp4 - 1);
    tryUnlock(stamp4 + 1);
  }

}
