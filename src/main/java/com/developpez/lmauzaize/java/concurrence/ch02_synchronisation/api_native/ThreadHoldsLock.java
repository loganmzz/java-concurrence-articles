package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.api_native;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ThreadHoldsLock {
  public static void main(String[] args) {
//////////////////////////////////////////
Object verrou = new Object();

Logger.println("Avant  : %b", Thread.holdsLock(verrou));
synchronized (verrou) {
  Logger.println("Dedans : %b", Thread.holdsLock(verrou));
}
Logger.println("Après  : %b", Thread.holdsLock(verrou));
//////////////////////////////////////////
  }
}
