package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.Arrays;
import java.util.Date;
import java.util.PrimitiveIterator.OfInt;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

import com.developpez.lmauzaize.java.concurrence.Logger;

public abstract class BlockingQueueLinkedVsArray {

  static {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  private static int timeout = 10, itération = 10, produit = 2_000, producteur = 1_000, consommateur = 1_000, total = produit * producteur;

  protected String action;

  //Actions
  //// Remplir/vider la file
  void remplir() {
    ajouter(total);
    terminerFile();
  }
  void vider() throws Exception {
    int size = queue.size();
    for (int i = 0; i < size; i++) {
      queue.take();
    }
  }
  //// Lance/attend des threads qui lisent la file jusqu'à épuisement.
  private ExecutorService consommateurs;
  private Object fin = new Object();
  protected void lancerConsommateurs() {
    OfInt séquence = IntStream.iterate(0, i -> i+1).iterator();
    ThreadFactory threadFactory = target -> new Thread(target, String.format("%s-%s-Consommateur-%04d", action, queue.getClass().getSimpleName(), séquence.nextInt()));
    consommateurs = Executors.newCachedThreadPool(threadFactory);
    Runnable tâche = () -> {
        try {
          while (queue.take() != fin);
          queue.add(fin);
        } catch (InterruptedException e) {

        } catch (Throwable e) {
          Logger.printStackTrace(e);
        }
    };
    for (int i = 0; i < consommateur; i++) {
      consommateurs.submit(tâche);
    }
  }
  protected void attendreConsommateurs() throws Exception {
    consommateurs.shutdown();
    if (!consommateurs.awaitTermination(timeout, TimeUnit.SECONDS)) {
      consommateurs.shutdownNow();
      if (!queue.isEmpty()) {
        throw new TimeoutException();
      }
    }
  }
  //// Lance/attend des threads qui remplissent la file
  private ExecutorService producteurs;
  protected void lancerProducteurs() {
    OfInt séquence = IntStream.iterate(0, i -> i+1).iterator();
    ThreadFactory threadFactory = target -> new Thread(target, String.format("Producteur-%s-%04d", action, séquence.nextInt()));
    producteurs = Executors.newCachedThreadPool(threadFactory);
    Runnable tâche = () -> ajouter(produit);
    for (int i = 0; i < producteur; i++) {
      producteurs.submit(tâche);
    }
  }
  protected void attendreProducteurs() throws Exception {
    try {
      producteurs.shutdown();
      if (!producteurs.awaitTermination(timeout, TimeUnit.SECONDS)) {
        producteurs.shutdownNow();
        throw new TimeoutException();
      }
    } finally {
      terminerFile();
    }
  }
  // Bench
  //// Exécute des actions avant/après chaque mesure
  void avant() throws Exception {}
  void après()  throws Exception {
    queue.clear();
    consommateurs = null;
    producteurs   = null;
  }
  //// Action à mesurer
  abstract void run() throws Exception;
  //// Joue le scénario plusieurs fois pour chaque implémentation, puis affiche le temps d'exécution total
  public void bench() throws Exception {
    for (BlockingQueue<Object> queue : Arrays.asList(newLinkedBlockingQueue(), newArrayBlockinQueue())) {
      this.queue = queue;
      long cumul = 0;
      for (int i = 0; i < itération; i++) {
        long spent = call();
        cumul += spent;
      }
      System.out.printf(
          "Test: %-17s, File: %20s, Temps: %tT.%<tL%n",
          action, queue.getClass().getSimpleName(), new Date(cumul / itération));
    }
    System.out.println();
  }

  private long call() throws Exception {
    avant();
    long start = System.currentTimeMillis();
    run();
    long spent = System.currentTimeMillis() - start;
    après();
    return spent;
  }

  private void ajouter(int n) {
    for (int i = 0; i < n; i++) {
      queue.add(i);
    }
  }
  private void terminerFile() {
    queue.add(fin);
  }

  private BlockingQueue<Object> queue;
  private BlockingQueue<Object> newLinkedBlockingQueue() {
    return new LinkedBlockingQueue<>();
  }
  private BlockingQueue<Object> newArrayBlockinQueue() {
    return new ArrayBlockingQueue<>(total+1);
  }



  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
System.out
  .printf("Paramètres :%n")
  .printf("  - Itérations par implémentation : %,9d%n", itération)
  .printf("  - Nombre de producteurs         : %,9d%n", producteur)
  .printf("  - Nombre de consommateurs       : %,9d%n", consommateur)
  .printf("  - Nombre total d'éléments       : %,9d%n", total)
  .printf("%n");

// Remplit la file en utilisant un seul thread
new BlockingQueueLinkedVsArray() {
  { action = "AjoutSéquentiel"; }
  void run() {
    remplir();
  }
}.bench();

// Remplit la file en utilisant plusieurs threads
new BlockingQueueLinkedVsArray() {
  { action = "AjoutParallèle"; }
  void run() throws Exception {
    lancerProducteurs();
    attendreProducteurs();
  }
}.bench();

// Vide la file en utilisant un seul thread
new BlockingQueueLinkedVsArray() {
  { action = "RetraitSéquentiel"; }
  void avant() {
    remplir();
  }
  void run() throws Exception {
    vider();
  }
}.bench();

// Vide la file en utilisant plusieurs threads
new BlockingQueueLinkedVsArray() {
  { action = "RetraitParallèle"; }
  void avant() {
    remplir();
  }
  void run() throws Exception {
    lancerConsommateurs();
    attendreConsommateurs();
  }
}.bench();

// Vide et remplit la file en utilisant plusieurs threads
new BlockingQueueLinkedVsArray() {
  { action = "Parallèle"; }
  void run() throws Exception {
    lancerConsommateurs();
    lancerProducteurs();
    attendreProducteurs();
    attendreConsommateurs();
  }
}.bench();
//////////////////////////////////////////
  }
}
