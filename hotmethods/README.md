Sample to show hot methods in Java Flight Recording
===================================================

This program checks whether a given random number is primitive or not.

Run the program without any arguments and also make a profiling recording.

For example:
`java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=60s,name=Hotmethods,filename=hotmethods.jfr -XX:FlightRecorderOptions=loglevel=info -jar target/hotmethods.jar`

In Code -> Hot Methods tab, you should see that the program has spent a lot of time in the LinkedList.indexOf(Object) method, which is called from contains method.

By changing LinkedList to a HashSet, you can make the program run a lot faster.

The contains methods in Linked List checks all the items. Algorithm run time is O(n). However the HashSet contains algorithm's run time is O(1).

Check the runtime using HashSet.
`java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=60s,name=Hotmethods,filename=hotmethods.jfr -XX:FlightRecorderOptions=loglevel=info -jar target/hotmethods.jar --use-set`

Use `time` command in Linux to measure the time.