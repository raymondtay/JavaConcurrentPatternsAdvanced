package sg.airlab.reentrant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// This is an interesting pattern to consider that is also "correct"
// Note: It looks like the wait-notify pattern but it does not use 
//       synchronization over locks.
//
// A "Condition" object is used to park and awake threads in this pattern
public class ProducerConsumerFinal {
  // the shared variable that's contended by all
  // executing threads; encapsulated via private fields
  private static int[] buffer;
  private static int count;

  private static Lock lock = new ReentrantLock();
  private static Condition notFull = lock.newCondition();
  private static Condition notEmpty = lock.newCondition();

  static class Producer {
    public void produce() throws InterruptedException {
      try {
        lock.lock();
        while (isFull(buffer))
          notFull.await();

        buffer[count++] = 1;
        notEmpty.signal();
      } finally {
        lock.unlock();
      }
    }
  }

  static class Consumer {
    public void consume() throws InterruptedException {
      try {
        lock.lock();
        while (isEmpty(buffer))
          notEmpty.await();

        buffer[--count] = 0;
        notFull.signal();
      } finally {
        lock.unlock();
      }
    }
  }

  static boolean isEmpty(int[] buffer) {
    return count == 0;
  }

  static boolean isFull(int[] buffer) {
    return count == buffer.length;
  }

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
      for (int i = 0; i < 50; i++) {
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
