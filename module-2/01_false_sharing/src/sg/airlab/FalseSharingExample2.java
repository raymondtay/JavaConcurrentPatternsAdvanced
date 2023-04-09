package sg.airlab;

// Solution #1 to fix the false-sharing problem: storing the variables in
// different objects.
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
