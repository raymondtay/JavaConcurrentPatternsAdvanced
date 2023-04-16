package sg.airlab.reentrant;

//
// The [producer consumer problem](https://en.wikipedia.org/wiki/Producer%E2%80%93consumer_problem) is
// a classical problem which was designed around the idea of two tasks where one
// task only does producing of elements and the other task only does the
// consumption of elements.
// 
// The interesting aspect of ANY solution that tries to solve this problem is
// that it needs to be correct when the CPU core count is between 1 and n; this
// implies that the algorithm needs to be correct in the two (2) circumstances 
// - Sequential execution 
// - Concurrent execution
//
// Question: This program does not work well, can you fix the problem? When you applied the fix,
//           do you understand and know why the fix works ?
// Bonus question: Re-design this such that you have at least two (2) concurrent
//                 producers while maintaining one (1) consumer. Make sure all
//                 threads are executing concurrently. 
//                 Can you write a test that verifies this ?
//
public class ProducerConsumer {

  // 3rd party lock object
  private static Object lock = new Object();

  // the shared variable that's contended by all
  // executing threads; encapsulated via private fields
  private static int[] buffer;
  private static int count;

  static class Producer {

    void produce() throws InterruptedException {
      // Design question: A lock must always protect the critical section ; can you
      // understand why this design is correct? Conversely, what happens when you just
      // protect either `count` or `buffer`?
      synchronized (lock) {
        while (isFull(buffer))
          lock.wait(); // Question: Spinning on the CPU to verify a condition and waits on a lock - is
                       // the call to wait() idempotent ?
        buffer[count++] = 1;
        lock.notifyAll();
      }
    }
  }

  static class Consumer {

    void consume() throws InterruptedException {
      synchronized (lock) {
        while (isEmpty(buffer))
          lock.wait(); // Question: Spinning on the CPU to verify a condition and waits on a lock - is
                       // the call to wait() idempotent ?
        buffer[--count] = 0;
      }
    }
  }

  static boolean isEmpty(int[] buffer) {
    return count == 0;
  }

  static boolean isFull(int[] buffer) {
    return count == buffer.length;
  }

  // Question: Do you agree if the design is correct? Can you explain why?
  public static void main(String... strings) throws InterruptedException {

    buffer = new int[10];
    count = 0;

    Producer producer = new Producer();
    Consumer consumer = new Consumer();

    Runnable produceTask = () -> {
      for (int i = 0; i < 50; i++) {
        try {
          producer.produce();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      System.out.println("Done producing");
    };
    Runnable consumeTask = () -> {
      for (int i = 0; i < 45; i++) {
        try {
          consumer.consume();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      System.out.println("Done consuming");
    };

    Thread consumerThread = new Thread(consumeTask);
    Thread producerThread = new Thread(produceTask);

    consumerThread.start();
    producerThread.start();

    consumerThread.join();
    producerThread.join();

    System.out.println("Data in the buffer: " + count);
  }
}
