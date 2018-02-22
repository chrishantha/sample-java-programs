Sample to a deadlock in Java Application
===========================================

This sample application shows an example of a deadlock. Following is the definition of "deadlock" according to
[Java Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html)

> Deadlock describes a situation where two or more threads are blocked forever, waiting for each other.

In this application, there are two threads trying to acquire two locks.

### How to run

The application will throw Out of Memory error after some time when you run following command

`java -Xms1g -Xmx1g -jar target/deadlock.jar`

### How to detect a deadlock

Take a thread dump (using `jstack <pid>` command or `jcmd <pid> Thread.print`).

The thread dump should show the Java level deadlock details as follows.

```
Found one Java-level deadlock:
=============================
"Thread Group  1-2":
  waiting to lock monitor 0x00007f8ab4003a78 (object 0x00000000eaca1488, a java.lang.Object),
  which is held by "Thread Group  1-1"
"Thread Group  1-1":
  waiting to lock monitor 0x00007f8ab4006518 (object 0x00000000eaca1498, a java.lang.Object),
  which is held by "Thread Group  1-2"

Java stack information for the threads listed above:
===================================================
"Thread Group  1-2":
        at com.github.chrishantha.sample.deadlock.DeadlockApplication$SampleLockThread.run(DeadlockApplication.java:54)
        - waiting to lock <0x00000000eaca1488> (a java.lang.Object)
        - locked <0x00000000eaca1498> (a java.lang.Object)
"Thread Group  1-1":
        at com.github.chrishantha.sample.deadlock.DeadlockApplication$SampleLockThread.run(DeadlockApplication.java:54)
        - waiting to lock <0x00000000eaca1498> (a java.lang.Object)
        - locked <0x00000000eaca1488> (a java.lang.Object)

Found 1 deadlock.
```