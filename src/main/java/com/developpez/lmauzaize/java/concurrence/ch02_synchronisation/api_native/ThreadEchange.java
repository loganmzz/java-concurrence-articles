package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.api_native;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ThreadEchange {

static class Tâche extends Thread {
  Object[] échange;
  int src;
  public Tâche(Object[] échange, int src) {
    super("Tâche-" + src);
    this.échange = échange;
    this.src = src;
  }

  @Override
  public void run() {
    long pause = 500 * src;
    Logger.println("Pause - %04dms", pause);
    try {
      sleep(pause);

      synchronized (échange) {
        // Envoi
        int dst = (src + 1) % échange.length;
        échange[dst] = getName();
        échange.notifyAll();
        Logger.println("Envoi vers Tâche-%d", dst);

        // Réception
        while (échange[src] == null) {
          échange.wait(5_000);
        }
        Logger.println("Réception de %-7s", échange[src]);
      }
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
};

  public static void main(String[] args) {
//////////////////////////////////////////
Object[] échange = new Object[4];
for (int i = 0; i < échange.length; i++) {
  new Tâche(échange, i).start();
}
//////////////////////////////////////////
  }
}
