package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.executors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.developpez.lmauzaize.java.concurrence.Logger;
import com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.ThreadFactoryLogger;

public class SingleThreadPoolDemo {

  private static void afficherMembres(Class<?> clazz) {
    Logger.println("%s", clazz.getTypeName().replaceAll(".*\\.", ""));
    Field[] champs = clazz.getFields();
    for (Field champ : champs) {
      Logger.println(" --> %s", champ);
    }
    Method[] méthodes = clazz.getMethods();
    for (Method méthode : méthodes) {
      Logger.println(" --> %s %s(%s)", méthode.getReturnType().getSimpleName(), méthode.getName(), Arrays.stream(méthode.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(",")));
    }
  }

  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactoryLogger());

Logger.println("Description du service:");
afficherMembres(executor.getClass());

try {
  Logger.println("");
  final Random random = new Random();
  for (int i = 1; i <= 4; i++) {
    final String nom = String.format("Tache-%02d", i);
    Runnable tache = new Runnable() {
      public void run() {
        try {
          int pause = random.nextInt(2_000);
          Logger.println("[%s] Pause %dms", nom, pause);
          TimeUnit.MILLISECONDS.sleep(pause);
          Logger.println("[%s] Fin", nom);
        } catch (InterruptedException e) {
          Logger.printStackTrace(e);
        }
      }
    };
    executor.submit(tache);
  }
} finally {
  executor.shutdown();
}

//////////////////////////////////////////
  }
}
