package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

import com.developpez.lmauzaize.java.concurrence.Logger;



public class TransferQueueDémo {

  TransferQueue<String> file = new LinkedTransferQueue<>();

  void tryTransfer(String valeur) {
    Logger.println("   résultat=%b", file.tryTransfer(valeur));
  };
  void tryTransferAttente(String valeur) throws InterruptedException {
    Logger.println("   résultat=%b", file.tryTransfer(valeur, 5, TimeUnit.SECONDS));
  };
  void transfer(String valeur) throws InterruptedException {
    file.transfer(valeur);
    Logger.println("   transfert terminé");
  };

  void avecConsommateur() {
    new Thread("Consommateur") {
      public void run() {
        try {
          Logger.println("   pause");
          TimeUnit.SECONDS.sleep(2);
          Logger.println("   lecture: %s", file.take());
        } catch (InterruptedException e) {
          Logger.printStackTrace(e);
        }
      }
    }.start();
  };

  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
TransferQueueDémo démo = new TransferQueueDémo();

// tryTransfer - immédiat
Logger.println("tryTransfer: A");
démo.tryTransfer("A");
Logger.println("");

// tryTransfer - expiration
Logger.println("tryTransfer: B, attente 5s");
démo.tryTransferAttente("B");
Logger.println("");

// tryTransfer - attente
Logger.println("tryTransfer: C, attente 5s");
démo.avecConsommateur();
démo.tryTransferAttente("C");
Logger.println("");

// transfer
Logger.println("transfer: D");
démo.avecConsommateur();
démo.transfer("D");
//////////////////////////////////////////
}
}
