package com.developpez.lmauzaize.java.concurrence.api_native;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class IllegalMonitorState {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final Object verrou = new Object();
Runnable tache = new Runnable() {
  public void run() {
    boolean tryWait;
    try {
      verrou.wait(100);
      tryWait = true;
    } catch (IllegalMonitorStateException e) {
      tryWait = false;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    boolean tryNotify;
    try {
      verrou.notify();
      tryNotify = true;
    } catch (IllegalMonitorStateException e) {
      tryNotify = false;
    }
    Logger.println("Synchronized (%-5b): wait: %-5b, notifty: %-5b", Thread.holdsLock(verrou), tryWait, tryNotify);
  }
};

tache.run();
synchronized (verrou) {
  tache.run();
}
//////////////////////////////////////////
  }
}
