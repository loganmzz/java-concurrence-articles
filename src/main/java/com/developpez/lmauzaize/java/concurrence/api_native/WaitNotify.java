package com.developpez.lmauzaize.java.concurrence.api_native;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class WaitNotify {
  public static void main(String[] args) {
//////////////////////////////////////////
final Object verrou = new Object();
abstract class Tache implements Runnable {
  public void run() {
    Logger.println("Synchronisation");
    synchronized (verrou) {
      try {
        action();
      } catch (InterruptedException e) {
        Logger.printStackTrace(e);
      }
    }
  }

  abstract void action() throws InterruptedException;
};
Tache patient = new Tache() {
  void action() throws InterruptedException {
    Logger.println("Attente - début");
    verrou.wait(5_000);
    Logger.println("Attente - fin");
  }
};
Tache reveil = new Tache() {
  void action() throws InterruptedException {
    Logger.println("Pause");
    Thread.sleep(500);
    Logger.println("Réveil");
    verrou.notify();
  }
};
new Thread(patient, "Patient").start();
new Thread(reveil , "Réveil").start();
//////////////////////////////////////////
  }
}
