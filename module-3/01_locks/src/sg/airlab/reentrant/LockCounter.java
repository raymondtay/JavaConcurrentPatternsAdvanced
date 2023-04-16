package sg.airlab.reentrant;

// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

// Since Java 5, circa 2004
public final class LockCounter extends Counter {
    public String name() {
        return "Lock";
    }

    private final ReentrantLock _lock = new ReentrantLock();
    private long _cnt;

    public long get() {
        return _cnt;
    }

    public void add(long x) {
        try {
            _lock.lock();
            _cnt += x;
        } finally {
            _lock.unlock();
        }
    }
}
