Sample to show allocations in Java Flight Recording
=================================================

This program checks whether a number is prime.

Run the program without any arguments and also make a profiling recording.

For example:
`java -Xms64m -Xmx64m -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=30s,name=Allocations,filename=allocations.jfr -XX:FlightRecorderOptions=loglevel=info -jar target/allocations.jar`

In Memory -> Allocation tab, you should see a high allocation rate.

Also see Memory -> Garbage Collections tab and check the frequency of 

Try the program again after changing `Long` types to primitive `long`

Run the program without locks.
`java -Xms64m -Xmx64m -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=30s,name=Allocations,filename=allocations-fixed.jfr -XX:FlightRecorderOptions=loglevel=info -jar target/allocations.jar`
