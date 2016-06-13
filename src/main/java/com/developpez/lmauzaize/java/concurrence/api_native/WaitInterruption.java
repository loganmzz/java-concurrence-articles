package com.developpez.lmauzaize.java.concurrence.api_native;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class WaitInterruption {
  public static void main(String[] args) {
//////////////////////////////////////////
final Object verrou = new Object();
final Thread patient = new Thread("Patient") {
  public void run() {
    Logger.println("Synchronisation");
    synchronized (verrou) {
      try {
        Logger.println("Attente - début");
        verrou.wait(5_000);
        Logger.println("Attente - fin");
      } catch (InterruptedException e) {
        Logger.printStackTrace(e);
      }
    }
  }
};
Thread reveil = new Thread("Réveil") {
  public void run() {
    Logger.println("Pause");
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
    Logger.println("Réveil");
    patient.interrupt();
  }
};
patient.start();
reveil.start();
//////////////////////////////////////////
  }
}
