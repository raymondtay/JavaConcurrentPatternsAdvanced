package sg.airlab;

// Can you see why there is a dead lock ?
public class Deadlock {

  private Object key1 = new Object();
  private Object key2 = new Object();

  public void a() {
    synchronized (key1) {
      System.out.println("[" + Thread.currentThread().getName() + "] I am in a()");
      b();
    }
  }

  public void b() {
    synchronized (key2) {
      System.out.println("[" + Thread.currentThread().getName() + "] I am in b()");
      c();
    }
  }

  public void c() {
    synchronized (key1) {
      System.out.println("[" + Thread.currentThread().getName() + "] I am in c()");
    }
  }

  public static void main(String[] args) {
    Deadlock demo = new Deadlock();
    Runnable aTask = () -> demo.a();
    Runnable bTask = () -> demo.b();
    Runnable cTask = () -> demo.c();

    Thread t1 = new Thread(aTask);
    Thread t2 = new Thread(bTask);
    Thread t3 = new Thread(cTask);

    t1.start();
    t2.start();
    t3.start();

  }

}
