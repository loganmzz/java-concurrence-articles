package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.threadfactory;

import static com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.threadfactory.DefaultThreadFactoryDemo.affiche;
import static com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.threadfactory.DefaultThreadFactoryDemo.créerThreadLimité;

import java.io.FilePermission;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.DomainCombiner;
import java.security.Permission;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class PrivilegedThreadFactoryDemo {

// Gestion permission
static Permission perm = new FilePermission("/test.txt", "write");

static AccessControlContext accessControlContext;
static {
  perm = new FilePermission("/test.txt", "write");
  Permissions permissions = new Permissions();
  permissions.add(perm);

  ProtectionDomain[] domains  = { new ProtectionDomain(null, permissions) };
  DomainCombiner combiner = (ProtectionDomain[] currentDomains, ProtectionDomain[] assignedDomains) -> domains;
  accessControlContext = new AccessControlContext(AccessController.getContext(), combiner);
}

// Vérifie si le thread possède les permissions
static boolean possèdePermission() {
  boolean permission;
  try {
    AccessController.getContext().checkPermission(perm);
    permission = true;
  } catch (AccessControlException e) {
    permission = false;
  }
  return permission;
}

// Effectue une action avec les permissions
static <T> T avecPermission(PrivilegedAction<T> action) {
  return AccessController.doPrivileged(action, accessControlContext);
}


public static void afficheAvecPermission(String cas) {
    affiche(cas, Thread.currentThread());
    Logger.println("  permis  =%s", possèdePermission());
  }

  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
class Executeur implements Runnable {

  Map<String, ThreadFactory> threadFactories = new LinkedHashMap<>();
  Executeur() {
    threadFactories.put("Thread.new"             , Thread::new);
    threadFactories.put("defaultThreadFactory"   , Executors.defaultThreadFactory());
    threadFactories.put("privilegedThreadFactory", Executors.privilegedThreadFactory());
  }

  public void run() {
    for (Entry<String, ThreadFactory> threadFactory : threadFactories.entrySet()) {
      Thread thread = threadFactory.getValue().newThread(() -> afficheAvecPermission(threadFactory.getKey()));
      thread.start();
      try {
        thread.join();
      } catch (InterruptedException e) {
        Logger.printStackTrace(e);
      }
    }
  }
}

Logger.println("");
Logger.println("");
Logger.println("******************************");
Logger.println("Sans permission");
Thread.currentThread().setUncaughtExceptionHandler(Logger.exceptionHandler);
new Executeur().run();

Logger.println("");
Logger.println("");
Logger.println("******************************");
Logger.println("Avec permission");
Executeur executeur = avecPermission(Executeur::new);

Thread thread = créerThreadLimité(executeur, "tâche");
thread.start();
thread.join();

//////////////////////////////////////////
  }
}
