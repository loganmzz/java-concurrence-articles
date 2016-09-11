package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.semaphore;

import java.util.concurrent.Semaphore;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class SemaphoreDeadlock {
public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final Semaphore semaphore = new Semaphore(1);
Thread thread = new Thread("Deadlock") {
  public void run() {
    Logger.println("début");
    try {
      Logger.println("acquisition #1");
      Logger.println("disponibilité: %02d", semaphore.availablePermits());
      semaphore.acquire();
      Logger.println("acquisition #2");
      Logger.println("disponibilité: %02d", semaphore.availablePermits());
      semaphore.acquire();
      Logger.println("acquisitions terminées");
      Logger.println("disponibilité: %02d", semaphore.availablePermits());
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
      Logger.println("disponibilité %02d", semaphore.availablePermits());
    }
    Logger.println("fin");
  }
};
thread.start();
thread.join(2_000);
if (thread.isAlive()) {
  Logger.println("disponibilité: %02d", semaphore.availablePermits());
  Logger.println("Thread toujours vivant, deadlock présumé");
  thread.interrupt();
  semaphore.release();
}
//////////////////////////////////////////
}
}
