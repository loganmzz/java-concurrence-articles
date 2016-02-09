package com.developpez.lmauzaize.java.concurrence.thread;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class FilleThread {
  public static void main(String[] args) {
//////////////////////////////////////////
new Thread() {
  public void run() {
    Logger.println("Je suis un nouveau thread !");
  }
}.start();
//////////////////////////////////////////
  }
}
