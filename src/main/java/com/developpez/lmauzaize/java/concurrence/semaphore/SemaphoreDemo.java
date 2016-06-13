package com.developpez.lmauzaize.java.concurrence.semaphore;

import java.util.concurrent.Semaphore;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class SemaphoreDemo {
public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
class Action {
  final Semaphore semaphore = new Semaphore(10);

  public void disponibilité() {
    Logger.println("%-15s %02d", "disponibilité", semaphore.availablePermits());
  }
  public void acquisition(int count) {
    try {
      Logger.println("%-15s %02d", "acquisition", count);
      semaphore.acquire(count);
      disponibilité();
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
  public void libération(int count) {
    Logger.println("%-15s %02d", "libération", count);
    semaphore.release(count);
    disponibilité();
  }
};
final Action action = new Action();

action.disponibilité();
action.libération(5);
action.acquisition(15);

new Thread("Acquisiteur") {
  public void run() {
    action.acquisition(15);
  }
}.start();
new Thread("Libérateur") {
  public void run() {
    try {
      sleep(500);
      action.disponibilité();
      for (int i = 0; i < 3; i++) {
        action.libération(5);
        sleep(500);
      }
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}.start();
//////////////////////////////////////////
}
}
