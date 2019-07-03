Sample to show latencies in a Java Flight Recording
===================================================

This program has two threads: Even and Odd to print Even and Odd numbers.

Run the program and also make a profiling recording.

### How to run
`java -Xms64m -Xmx64m -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=30s,name=Latencies,filename=latencies.jfr -XX:FlightRecorderOptions=loglevel=info -jar target/latencies.jar`

### Analyzing Java Flight Recording

In Threads -> Latencies tab, you should see that the program has many blocked events.

You should also see red blocks in Events -> Graph tab. Red blocks are not good as the thread is just waiting to acquire a lock.
 
See Threads -> Contention tab to see the Locks and also see Threads -> Lock Instances to check whether both threads share the same lock instances.

### Improving Performance

Since there is no shared resource to protect, you can avoid the synchronized keyword and improve the performance.

Try the program again after removing the `synchronized` keyword in `isEven` method.
