package sg.airlab;

// TODO: Fix the build, for now use the following instructions
// 1. Install JDK 17 and export it as the default JDK
// 2. Compile using `javac --add-exports java.base/jdk.internal.vm.annotation=ALL-UNNAMED -d classes *.java`
// 3. Run using `java -classpath classes --add-exports java.base/jdk.internal.vm.annotation=ALL-UNNAMED sg.airlab.FalseSharingExampleFinal`
// 4. Without the "--add-exports ..." statement, the runtime is close to 54 seconds
public class FalseSharingExampleFinal {
  public static void main(String[] args) {

    Counter1 counter1 = new Counter1();
    Counter1 counter2 = counter1;

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
