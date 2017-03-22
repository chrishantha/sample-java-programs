Sample to show latencies in Java Flight Recording
=================================================

This program has two threads: Even and Odd to print Even and Odd numbers.

Run the program without any arguments and also make a profiling recording.

For example:
`java -jar -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=30s,name=Latencies,filename=latencies.jfr -XX:FlightRecorderOptions=loglevel=info target/latencies.jar`

In Threads -> Latencies tab, you should see that the program has many blocked events.

You should also see red blocks in Events -> Graph tab. Red blocks are not good as the thread is just waiting to acquire a lock.
 
See Threads -> Contention tab to see the Locks and also see Threads -> Lock Instances to check whether both threads share the same lock instances.

Since there is no shared resource, you can avoid the lock and improve the performance.

Remove the `synchronized` keyword in `isEven` method.

Run the program without locks.
`java -jar -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=30s,name=Latencies,filename=latencies-fixed.jfr -XX:FlightRecorderOptions=loglevel=info target/latencies.jar`
