Locks
=

Types of Locks
==

There are the following types:
* [Intrinsic Lock](https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html)
* [Dedicated Lock Object](https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html)
  * **Note** We discussed this in a earlier session using a separate `Object` instead of a instance or class lock!
* [Reentrant Lock](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/locks/ReentrantLock.html)

Reentrant Lock
===

The default intrinsic locks (instance and class locks aka _synchronized_)
provided by the Java Language is not flexible enough to support many kinds of
concurrency pattern. 

The general structure looks like this:
```java
Lock lock = new ReentrantLock();
try {
    lock.lock();
    // Work on some code
} finally {
    lock.unlock();
}
```

It is flexible that it allows us to create the following ideas
* Interruptible Pattern
```java
Lock lock = new ReentrantLock();
try {
    lock.lockInterruptibly();
    // Work on some code
} finally {
    lock.unlock();
}
```
* Timed Lock Pattern If a thread is already working in the _critical section_
and another thread tries to enter the CS via the `tryLock` it will "slide"
through w/o executing the CS and executes the "else"-statement. This is useful
in scenarios where you need to ensure at most 1 thread executes the CS but not
all threads (e.g. the JDK supports a "race" of all threads)
```java
Lock lock = new ReentrantLock();
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try {
        // Work on some code
    } finally {
        lock.unlock();
    }
} else { ... } //
```
* Fair Lock Pattern _Fairness_ here means that the first thread to enter the
wait line/queue is the first to enter the critical section (aka CS). 

When several threads are waiting on a lock (regardless its an intrinsic or
explicit lock), the first one to enter the CS is chosen _randomly_.





