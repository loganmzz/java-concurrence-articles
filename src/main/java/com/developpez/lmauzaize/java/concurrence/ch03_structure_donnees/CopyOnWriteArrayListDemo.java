package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class CopyOnWriteArrayListDemo {
//////////////////////////////////////////
  int itération = 10;
  Queue<Integer> file;


  int remplissage = 0;
  long call() throws InterruptedException {
    ArrayList<Integer> temp = new ArrayList<>(remplissage);
    for (int i = 0; i < remplissage; i++) {
      temp.add(i);
    }
    file.addAll(temp);
    long début = System.currentTimeMillis();
    ExecutorService executor = Executors.newCachedThreadPool();
    for (Runnable scenario : scenarii) {
      executor.submit(scenario);
    }
    executor.shutdown();
    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
      executor.shutdownNow();
      throw new CancellationException();
    }
    long call = System.currentTimeMillis() - début;
    file.clear();
    return call;
  }

  // Pour chaque implémentation, initialise une instance, puis joue le scénario 10 fois et enfin affiche le temps moyen par itération.
  public void test(int remplissage, String action) throws Exception {
    this.remplissage = remplissage;
    for (Queue<Integer> queue : Arrays.asList(new CopyOnWriteArrayQueue<Integer>(), new ArrayBlockingQueue<Integer>(10_000))) {
      this.file = queue;
      long cumul = 0;
      for (int i = 0; i < itération; i++) {
        long spent = call();
        cumul += spent;
      }
      System.out.printf(
          "Test: %-17s, File: %21s, Temps: %tT.%<tL%n",
          action, queue.getClass().getSimpleName(), newUtcDate(cumul / itération));
    }
    System.out.println();
    file = null;
    scenarii.clear();
    remplissage = 0;
  }

  // Actions : créer une tâche qui sera exécutée N fois en parallèle avec une pause optionnelle avant.
  ArrayList<Runnable> scenarii = new ArrayList<>();
  CopyOnWriteArrayListDemo scenario(int thread, Runnable action) throws Exception {
    scenarii.ensureCapacity(scenarii.size() + thread);
    for (int i = 0; i < thread; i++) {
      scenarii.add(action);
    }
    return this;
  }
  abstract class InterruptibleTask implements Runnable {
    final public void run() {
      try {
        unsafe();
      } catch (InterruptedException e) {
      }
    }
    abstract void unsafe() throws InterruptedException;
  }
  //  - Effectue "n" insertions par thread
  public CopyOnWriteArrayListDemo add(int thread, final int n, final int délai) throws Exception {
    class Action extends InterruptibleTask {
      void unsafe() throws InterruptedException {
        if (délai > 0) {
          TimeUnit.MILLISECONDS.sleep(délai);
        }
        for (int i = 0; i < n; i++) {
          file.add(i);
        }
      }
    }
    return scenario(thread, new Action());
  }
  //  - Itère la collection à chaque thread
  public CopyOnWriteArrayListDemo iterator(int thread, final int délai) throws Exception {
    class Action extends InterruptibleTask {
      void unsafe() throws InterruptedException {
        if (délai > 0) {
          TimeUnit.MILLISECONDS.sleep(délai);
        }
        for (Integer i : file) {
        }
      }
    }
    return scenario(thread, new Action());
  }
  //  - Effectue "n" retraits par thread
  public CopyOnWriteArrayListDemo remove(int thread, final int quantité, final int délai) throws Exception {
    class Action extends InterruptibleTask {
      void unsafe() throws InterruptedException {
        if (délai > 0) {
          TimeUnit.MILLISECONDS.sleep(délai);
        }
        for (int i = 0; i < quantité; i++) {
          file.remove();
        }
      }
    }
    return scenario(thread, new Action());
  }
//////////////////////////////////////////
static class CopyOnWriteArrayQueue<E> implements Queue<E> {
  CopyOnWriteArrayList<E> list = new CopyOnWriteArrayList<>();
  public boolean add(E e) {
    return list.add(e);
  }
  public Iterator<E> iterator()     {
    return list.iterator();
  }
  public E remove() {
    return list.remove(0);
  }
  public void clear() {
    list.clear();
  }
  public boolean addAll(Collection<? extends E> c) {
    return list.addAll(c);
  }

  public int      size()                            { throw new UnsupportedOperationException(); }
  public boolean  isEmpty()                         { throw new UnsupportedOperationException(); }
  public boolean  contains(Object o)                { throw new UnsupportedOperationException(); }
  public Object[] toArray()                         { throw new UnsupportedOperationException(); }
  public <T> T[]  toArray(T[] a)                    { throw new UnsupportedOperationException(); }
  public boolean  remove(Object o)                  { throw new UnsupportedOperationException(); }
  public boolean  containsAll(Collection<?> c)      { throw new UnsupportedOperationException(); }
  public boolean  removeAll(Collection<?> c)        { throw new UnsupportedOperationException(); }
  public boolean  retainAll(Collection<?> c)        { throw new UnsupportedOperationException(); }
  public boolean  offer(E e)                        { throw new UnsupportedOperationException(); }
  public E        poll()                            { throw new UnsupportedOperationException(); }
  public E        element()                         { throw new UnsupportedOperationException(); }
  public E        peek()                            { throw new UnsupportedOperationException(); }
}
//////////////////////////////////////////
static TimeZone UTC = TimeZone.getTimeZone("UTC");
static Calendar newUtcDate(long millis) {
  Calendar cal = Calendar.getInstance();
  cal.setTimeZone(UTC);
  cal.setTimeInMillis(millis);
  return cal;
}
//////////////////////////////////////////
  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
final CopyOnWriteArrayListDemo démo = new CopyOnWriteArrayListDemo();
int thread, quantité, remplissage, délai = 0;

démo.add(thread=  1, quantité=10_000, délai).test(remplissage=0, "Ajout linéaire");
démo.add(thread=100, quantité=   100, délai).test(remplissage=0, "Ajout parallèle");

démo.iterator(thread=1_000, délai).test(remplissage=1_000, "Parcours [1k/1k]");
démo.iterator(thread=5_000, délai).test(remplissage=1_000, "Parcours [5k/1k]");
démo.iterator(thread=1_000, délai).test(remplissage=3_000, "Parcours [1k/3k]");
démo.iterator(thread=3_000, délai).test(remplissage=3_000, "Parcours [3k/3k]");

for (int i = 0; i < 3; i++) {
  délai = i*100;
  démo
    .add     (thread=  100, quantité=10, délai)
    .iterator(thread=1_000,              délai)
    .remove  (thread=  100, quantité= 5, délai);
}
démo.test(remplissage=1_000, "Concurrence");
//////////////////////////////////////////
}
}
