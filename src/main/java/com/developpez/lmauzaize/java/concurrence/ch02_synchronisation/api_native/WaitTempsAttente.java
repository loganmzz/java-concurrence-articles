package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.api_native;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class WaitTempsAttente {

  public static void main(String[] args) {
//////////////////////////////////////////
new Thread() {
  public void run() {
    Object verrou = new Object();
    Logger.println("Synchronisation");
    synchronized (verrou) {
      Logger.println("Attente - début");
      try {
        verrou.wait(1_000);
        Logger.println("Attente - fin");
      } catch (InterruptedException e) {
        Logger.printStackTrace(e);
      }
    }
  }
}.start();
//////////////////////////////////////////
  }
}
