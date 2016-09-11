package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ConcurrentHashMapDemo {
  // Type de map utilisable
  static enum TypeMap {
    CONCURRENT, HASHTABLE;
  }
  TypeMap[] typeMaps = TypeMap.values();

  // Paramètres des maps
  int taille = 16, concurrence = 16;
  float charge = 0.75f;

  // Tâches
  // - Ajoute les clés spécifiés
  Runnable ajout(final List<String> clés) {
    Tache tache = new Tache("put", clés.size()) {
      void forEach(int i, Random random ) {
        map.put(clés.get(i), Thread.currentThread().getName());
      }
    };
    return tache;
  }
  // - Ajoute n clés au hasard
  Runnable ajout(int n) {
    Tache tache = new Tache("put", n) {
      void forEach(int i, Random random ) {
        map.put(String.valueOf(random.nextInt(maxKey)), Thread.currentThread().getName());
      }
    };
    return tache;
  }
  // - Lit n clés au hasard
  Runnable lecture(int n) {
    Tache tache = new Tache("get", n) {
      void forEach(int i, Random random) {
        String key = String.valueOf(random.nextInt(maxKey));
        map.get(key);
      }
    };
    return tache;
  }

  // Programme la tâche donnée pour s'exécuter sur n threads
  void tache(int n, Runnable tache) {
    taches.addAll(Collections.nCopies(n, tache));
    StringBuilder string = format(new StringBuilder(), n).append("*").append(tache);
    test.add(string.toString());
  }

  // Exécute la liste de tâches programmées en plusieurs itérations et indique le temps d'exécution moyen
  void bench(String test) throws InterruptedException {
    for (TypeMap typeMap : typeMaps) {
      map = créerMap(typeMap);
      long total = 0;
      long count = 0;
      for (int i = 0; i < max; i++) {
        map.clear();
        long start = System.currentTimeMillis();
        latch = new CountDownLatch(taches.size());
        for (Runnable tache : taches) {
          new Thread(tache).start();
        }
        latch.await();
        latch = null;
        if (i > min) {
          long spent = System.currentTimeMillis() - start;
          total += spent;
          count++;
        }
      }
      Logger.println("test: %s, classe: %17s temps: %tT.%<tL", test, map.getClass().getSimpleName(), new Date(total / count));
    }
    taches.clear();
    this.test.clear();
  }
  // Idem que précédemment mais le nom est généré à partir du scénario.
  void bench() throws InterruptedException {
    bench(test.toString());
  }


  private Map<String,String> créerMap(TypeMap type) {
    switch (type) {
      case CONCURRENT: return new ConcurrentHashMap<>(maxKey, charge, concurrence);
      case HASHTABLE : return new Hashtable<String, String>(maxKey, charge);
      default: throw new UnsupportedOperationException();
    }
  }

  private abstract class Tache implements Runnable {
    private String nom;
    private int    iteration;
    Tache(String nom, int iteration) {
      this.nom       = nom;
      this.iteration = iteration;
    }
    public final void run() {
      try {
        Random random = new Random();
        for (int i = 0; i < iteration; i++) {
          forEach(i, random);
        }
      } catch (Exception e) {
        Logger.printStackTrace(e);
      } finally {
        latch.countDown();
      }
    }
    public String toString() {
      return format(new StringBuilder().append(nom).append("("), iteration).append(")").toString();
    }

    abstract void forEach(int i, Random random) throws Exception;
  }

  // Number format
  private static String[] unités_nom = { "M", "k" };
  private static long[]   unités_valeur = { 1_000_000, 1_000 };
  private static StringBuilder format(StringBuilder builder, int nb) {
    boolean append = false;
    for (int i = 0; i < unités_nom.length; i++) {
      long unité = unités_valeur[i];
      if (nb >= unité) {
        append = true;
        builder.append(nb / unité).append(unités_nom[i]);
        nb %= unité;
      }
    }
    if (!append || nb > 0) {
      builder.append(nb);
    }
    return builder;
  }

  // Map
  private Map<String,String> map;

  // Tâches
  private List<Runnable> taches = new ArrayList<>();
  private List<String> test = new ArrayList<>();
  private int min = 20, max = 50;
  private CountDownLatch latch;
  private int maxKey = 1_000_000;
}
