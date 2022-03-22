Sample to show allocations in a Java Flight Recording
=====================================================

This program checks whether a number is prime.

Run the program and also make a profiling recording.

### How to run
```bash
java -Xms64m -Xmx64m -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=30s,name=Allocations,filename=allocations.jfr -Xlog:jfr=info -jar target/allocations.jar
```

### Analyzing Java Flight Recording

In Memory -> Allocation tab, you should see a high allocation rate.

Also, see Memory -> Garbage Collections tab and check the frequency of GC events.

### Improving Performance

Try the program again after changing `Long` types to primitive `long`.
