package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ConcurrentHashMapConcurrence {
  
  static Map<String, String> map;

  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

// Paramètres
int thread =        16;
int taille = 2_000_000;
int limite =     1_024;

// Génération des valeurs possibles
String[] cache = IntStream.range(0, limite).mapToObj(String::valueOf).toArray(String[]::new);

// Génère une liste fixe de tâches pour tous les tests
List<Callable<String>> tasks = new ArrayList<>(thread);
Random random = new Random();
for (int i = 0; i < thread; i++) {
  List<String> list = random.ints(0, limite).limit(taille).mapToObj(key -> cache[key]).collect(Collectors.toCollection(() -> new ArrayList<>(taille)));
  Callable<String> task = () -> {
    String name = Thread.currentThread().getName();
    for (String item : list) {
      map.put(item, name);
    }
    return name;
  };
  tasks.add(task);
}

ExecutorService threadpool = Executors.newFixedThreadPool(thread);
for (int concurrence = 1; concurrence <= 256; concurrence*=2) {
  map = new ConcurrentHashMap<>(limite, 1.0f, concurrence);
  int count = 0;
  int total = 0;
  for (int i = 0; i < 50; i++) {
    map.clear();
    long start = System.currentTimeMillis();
    threadpool.invokeAll(tasks);
    long spent = System.currentTimeMillis() - start;
    if (i >= 20) {
      count++;
      total += spent;
    }
  }
  Logger.println("test: %04d, classe: %17s temps: %tT.%<tL", concurrence, map.getClass().getSimpleName(), new Date(total / count));
}
threadpool.shutdownNow();
//////////////////////////////////////////
}

}
