package sg.airlab;

// Solution #1 to fix the false-sharing problem: storing the variables in
// different objects. This deserves more explanation ... Essentially, the
// program does exactly what it did as illustrated in the program
// `FalseSharingExample.java` and the oddity here is to create 2 different
// objects and have each thread work on the different variables and this
// violates essentially every aspect of what every programmer learnt when it
// comes to software encapsulation and abstraction. So, what can we do next ?
// There are a couple of solutions available to Java programmers and the best
// one is to leverage tooling already available in the JDK (the caveat here is
// that not everyone knows this) illustrated in `FalseSharingExampleFinal.java`
public class FalseSharingExample2 {
  public static void main(String[] args) {

    // Notice this violates the way programmers designed the intended solution.
    Counter counter1 = new Counter();
    Counter counter2 = new Counter();

    long iterations = 1_000_000_000; // billion iterations

    Thread thread1 = new Thread(() -> {
      long startTime = System.currentTimeMillis();
      for (long i = 0; i < iterations; i++) {
        counter1.count1++;
      }
      long endTime = System.currentTimeMillis();
      System.out.println("total time: " + (endTime - startTime));
    });
    Thread thread2 = new Thread(() -> {
      long startTime = System.currentTimeMillis();
      for (long i = 0; i < iterations; i++) {
        counter2.count2++;
      }
      long endTime = System.currentTimeMillis();
      System.out.println("total time: " + (endTime - startTime));
    });

    thread1.start();
    thread2.start();
  }
}
