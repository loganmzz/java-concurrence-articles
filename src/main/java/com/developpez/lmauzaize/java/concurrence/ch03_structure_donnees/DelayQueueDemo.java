package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class DelayQueueDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
class Différé implements Delayed {
  long expiration;
  String toString;
  Différé(long temps, TimeUnit unité) {
    long now   = System.nanoTime();
    long nanos = unité.toNanos(temps);
    expiration = now + nanos;
    toString   = temps + " " + unité;
  }
  @Override
  public long getDelay(TimeUnit unité) {
    long now   = System.nanoTime();
    long temps = expiration - now;
    long délai = unité.convert(temps, TimeUnit.NANOSECONDS);
    return délai;
  }
  @Override
  public int compareTo(Delayed o) {
    Différé d = (Différé) o;
    long diff = this.expiration - d.expiration;
    if (diff > Integer.MAX_VALUE) return Integer.MAX_VALUE;
    if (diff < Integer.MIN_VALUE) return Integer.MIN_VALUE;
    return (int) diff;
  }
  @Override
  public String toString() {
    return toString;
  }
}
DelayQueue<Différé> file = new DelayQueue<>();
for (long délai : new long[] { 5, 2, 1, 3, 4, 6 }) {
  Différé d = new Différé(délai, TimeUnit.SECONDS);
  file.add(d);
}

Runnable afficherFile = () -> {
  Logger.println("File : %s", file);
  Logger.println("   %-10s= %s", "size" , file.size());
  Logger.println("   %-10s= %s", "iterator", new ArrayList<>(file));
  Logger.println("   %-10s= %s", "array", Arrays.toString(file.toArray()));
  Logger.println("   %-10s= %s", "peek" , file.peek());
  Logger.println("   %-10s= %s", "element" , file.element());
  Logger.println("   ***");
  Logger.println("   %-10s= %s", "poll" , file.poll());
  Object remove;
  try {
    remove = file.remove();
  } catch (NoSuchElementException e) {
    remove = e;
  }
  Logger.println("   %-10s= %s", "remove" , remove);
  List<Différé> liste = new ArrayList<>();
  file.drainTo(liste);
  Logger.println("   %-10s= %s", "drainTo", liste);

};


afficherFile.run();
Logger.println("");
Logger.println("Sleep");
Logger.println("");
TimeUnit.SECONDS.sleep(5);
afficherFile.run();
//////////////////////////////////////////
}
}
