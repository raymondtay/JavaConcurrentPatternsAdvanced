package sg.airlab;

public class FalseSharing {

  public static int NUM_THREADS_MAX = 4;
  public final static long ITERATIONS = 50_000_000L;

  private static LongPadded[] paddedLongs;
  private static LongUnPadded[] unPaddedLongs;

  public final static class LongPadded {
    public long q1, q2, q3, q4, q5, q6;
    public volatile long value = 0L;
    public long q11, q12, q13, q14, q15, q16;

  }

  public final static class LongUnPadded {
    public volatile long value = 0L;
  }

  static {
    paddedLongs = new LongPadded[NUM_THREADS_MAX];
    for (int i = 0; i < paddedLongs.length; i++) {
      paddedLongs[i] = new LongPadded();
    }
    unPaddedLongs = new LongUnPadded[NUM_THREADS_MAX];
    for (int i = 0; i < unPaddedLongs.length; i++) {
      unPaddedLongs[i] = new LongUnPadded();
    }
  }

  public static void main(final String[] args) throws Exception {
    runBenchmark();
  }

  private static void runBenchmark() throws InterruptedException {

    long begin, end;

    for (int n = 1; n <= NUM_THREADS_MAX; n++) {

      Thread[] threads = new Thread[n];

      for (int j = 0; j < threads.length; j++) {
        threads[j] = new Thread(createPaddedRunnable(j));
      }

      begin = System.currentTimeMillis();
      for (Thread t : threads) {
        t.start();
      }
      for (Thread t : threads) {
        t.join();
      }
      end = System.currentTimeMillis();
      System.out.printf("   Padded # threads %d - T = %dms\n", n, end - begin);

      for (int j = 0; j < threads.length; j++) {
        threads[j] = new Thread(createUnpaddedRunnable(j));
      }

      begin = System.currentTimeMillis();
      for (Thread t : threads) {
        t.start();
      }
      for (Thread t : threads) {
        t.join();
      }
      end = System.currentTimeMillis();
      System.out.printf(" UnPadded # threads %d - T = %dms\n\n", n, end - begin);
    }
  }

  private static Runnable createUnpaddedRunnable(final int k) {
    return () -> {
      long i = ITERATIONS + 1;
      while (0 != --i) {
        unPaddedLongs[k].value = i;
      }
    };
  }

  private static Runnable createPaddedRunnable(final int k) {
    Runnable paddedTouch = () -> {
      long i = ITERATIONS + 1;
      while (0 != --i) {
        paddedLongs[k].value = i;
      }
    };
    return paddedTouch;
  }
}
