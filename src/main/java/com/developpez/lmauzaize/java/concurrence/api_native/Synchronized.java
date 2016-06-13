package com.developpez.lmauzaize.java.concurrence.api_native;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class Synchronized {
  public static void main(String[] args) {
//////////////////////////////////////////
final Object verrou = new Object();

new Thread() {
  public void run() {
    Logger.println("Pause");
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
    Logger.println("Verrouillage");
    synchronized (verrou) {
      Logger.println("Verrou posé");
    }
    Logger.println("Verrou libéré");
  }
}.start();

new Thread() {
  public void run() {
    Logger.println("Verrouillage");
    synchronized (verrou) {
      Logger.println("Verrou posé");
      Logger.println("Pause");
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        Logger.printStackTrace(e);
      }
    }
    Logger.println("Verrou libéré");
  }
}.start();
//////////////////////////////////////////
  }
}
