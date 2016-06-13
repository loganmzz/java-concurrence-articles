package com.developpez.lmauzaize.java.concurrence.api_native;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class WordTearing {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final byte[] bytes = new byte[10_000];
class Tache extends Thread {
  private int index;
  public Tache(int index) {
    super("Tache-" + index);
    this.index = index;
  }
  @Override
  public void run() {
    bytes[index] = (byte) index;
  }
};
Tache[] taches = new Tache[bytes.length];
for (int i = 0; i < taches.length; i++) {
  taches[i] = new Tache(i);
}
for (Tache tache : taches) {
  tache.start();
}
for (Tache tache : taches) {
  tache.join();
}
int erreur = 0;
for (int i = 0; i < bytes.length; i++) {
  byte val = (byte) i;
  if (bytes[i] != val) {
    erreur++;
    Logger.println("bytes[%02d]=%02d", i, bytes[i]);
  }
}
Logger.println("Résultat: %02d erreur(s)", erreur);
//////////////////////////////////////////
  }
}
