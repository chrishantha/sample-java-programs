Sample to show hot methods in a Java Flight Recording
=====================================================

This program checks whether a given random number is primitive or not.

Run the program and also make a profiling recording.

### How to run
```bash
java -Xms64m -Xmx64m -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=60s,name=Hotmethods,filename=hotmethods.jfr -Xlog:jfr=info -jar target/hotmethods.jar
```

### Analyzing Java Flight Recording

In Code -> Hot Methods tab, you should see that the program has spent a lot of time in the LinkedList.indexOf(Object) method, which is called from contains method.

### Improving Performance

By changing LinkedList to a HashSet, you can make the program run a lot faster.

The contains methods in Linked List checks all the items. Algorithm run time is O(n). However the HashSet contains algorithm's run time is O(1).

In the program, change `Collection<Integer> primeNumbers = new LinkedList<>()` to `Collection<Integer> primeNumbers = new HashSet<>()` to use a HashSet for Prime Numbers

Check the runtime using HashSet.

Use `time` command in Linux to measure the time.
